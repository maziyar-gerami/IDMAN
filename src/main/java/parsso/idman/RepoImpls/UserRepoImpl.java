package parsso.idman.RepoImpls;


import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.apache.commons.compress.utils.IOUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Communicate.Email;
import parsso.idman.Helpers.Communicate.Message;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.User.*;
import parsso.idman.Models.ListUsers;
import parsso.idman.Models.ServicesSubModel.ExtraInfo;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.Tokens;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserRepoImpl implements UserRepo {


    Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);
    @Autowired
    FilesStorageService storageService;
    @Autowired
    private Message message;
    @Autowired
    private UserRepo userRepo;
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${email.controller}")
    private String EMAILCONTROLLER;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${api.get.users}")
    private String apiAddress;
    @Value("${get.users.time.interval}")
    private int apiHours;
    private final int apiMiliseconds = apiHours * 60 * 60000;

    @Autowired
    private LdapTemplate ldapTemplate;
    @Value("${pwd.expire.warning}")
    private String pwdExpireWarning;
    @Value("${pwd.failure.count.interval}")
    private String pwdFaiilureCount;
    @Value("${pwd.in.history}")
    private String pwdInHistory;
    @Value("${pwd.check.quality}")
    private String pwdCheckQuality;
    @Value("${default.user.password}")
    private String defaultPassword;
    @Value("${profile.photo.path}")
    private String uploadedFilesPath;
    @Value("${administrator.ou.id}")
    private String adminId;
    @Autowired
    private BuildAttributes buildAttributes;
    @Autowired
    private Token tokenClass;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserAttributeMapper userAttributeMapper;
    @Autowired
    private Email emailClass;
    @Autowired
    private BuildDn buildDn;
    @Autowired
    private DashboardData dashboardData;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private ImportUsers importUsers;


    @Override
    public JsonObject create(User p) {

        User user = retrieveUser(p.getUserId());
        DirContextOperations context;

        try {
            if (user == null) {
                Name dn = buildDn.buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes.BuildAttributes(p));
                //logger.info("User "+user.getUserId() + " created");
                Tokens tokens = new Tokens();
                tokens.setUserId(p.getUserId());
                tokens.setQrToken(UUID.randomUUID().toString());
                Date date = new Date();
                tokens.setCreationTimeStamp(date.getTime());
                mongoTemplate.save(tokens, Token.collection);


                if (p.getCStatus() != null) {
                    if (p.getCStatus().equals("disable"))
                        disable(p.getUserId());
                }

                logger.info("User " + "\"" + p.getUserId() + "\"" + " in " + new Date() + " created successfully");
                return new JsonObject();
            } else {
                logger.warn("User " + "\"" + p.getUserId() + "\"" + " is exist. So it cannot be created");

                importUsers.compareUsers(user, p);
            }
        } catch (Exception e) {
            logger.warn("Creating user " + "\"" + p.getUserId() + "\"" + " was unsuccessful");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public JSONObject createUserImport(User p) {

        User user = retrieveUser(p.getUserId());
        if (p.getUserPassword() == null)
            p.setUserPassword(defaultPassword);

        try {
            if (user == null) {
                Name dn = buildDn.buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes.BuildAttributes(p));

                Tokens tokens = new Tokens();
                tokens.setUserId(p.getUserId());
                tokens.setQrToken(UUID.randomUUID().toString());
                Date date = new Date();
                tokens.setCreationTimeStamp(date.getTime());
                mongoTemplate.save(tokens, Token.collection);

                return new JSONObject();
            } else {

                return importUsers.compareUsers(user, p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpStatus update(String uid, User p) {
        Name dn = buildDn.buildDn(uid);

        setRole(uid, p);

        User user = retrieveUser(uid);

        DirContextOperations context;

        //remove current pwdEndTime
        if ((p.getEndTime()==null || p.getEndTime().equals(""))) {
            if ((user.getEndTime() != null || user.getEndTime() != ""))
                removeCurrentEndTime(uid);
        }else if(!(p.getEndTime().equals(user.getEndTime())))
                removeCurrentEndTime(uid);

        context = buildAttributes.buildAttributes(uid, p, dn);

        //context.setAttributeValue("createTimestamp", Long.valueOf(p.getTimeStamp()).toString().substring(0,14));


        try {
            ldapTemplate.modifyAttributes(context);

            logger.info("User " + "\"" + p.getUserId() + "\"" + "in " + new Date() + "updated successfully");
            return HttpStatus.OK;

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Updating user " + "\"" + p.getUserId() + "\"" + "was unsuccessful");
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus remove(JSONObject jsonObject) {
        List<User> people = new LinkedList<>();
        if (jsonObject.size() == 0)
            people = retrieveUsersFull();
        else {

            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            Iterator<String> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                User user = retrieveUser(iterator.next());
                if (user != null)
                    people.add(user);
            }
        }

        if (people != null)
            for (User user : people) {
                Name dn = buildDn.buildDn(user.getUserId());

                try {

                    ldapTemplate.unbind(dn);

                } catch (Exception e) {
                    logger.warn("Deleting User " + user.getUserId() + " was unsuccessfully");
                    e.printStackTrace();
                }

            }

        if (people.size() == 0)
            logger.info("All users removed successfully");
        if (people.size() == 1)
            logger.info("User " + people.get(0).getUserId() + " removed successfully");
        else
            logger.info("Selected users removed successfully");


        return HttpStatus.OK;
    }

    public void setRole(String uid, User p) {
        String role = null;

        if (uid.equals("su")) {
            role = "SUPERADMIN";

        } else {
            try {
                List<String> lst = p.getMemberOf();
                for (String id : lst) {

                    if (id.equals(adminId)) {
                        role = "ADMIN";
                        break;
                    }
                }
            } catch (Exception e) {

                if (uid.equals(adminId))
                    role = "ADMIN";
            }
        }

        if (role == null)
            role = "USER";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = null;

        if (auth != null){
            updatedAuthorities = new ArrayList<>(auth.getAuthorities());

        if (auth != null && auth.getName().equals(p.getUserId())) {
            updatedAuthorities.remove(0);

            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));

            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    }

    @Override
    public HttpStatus changePassword(String uId, String newPassword, String token) {

        //TODO:check current pass
        User user = retrieveUser(uId);


        if (true) {
            if (token != null) {
                if (tokenClass.checkToken(uId, token) == HttpStatus.OK) {
                    user.setUserPassword(newPassword);

                    Name dn = buildDn.buildDn(uId);
                    DirContextOperations context = buildAttributes.buildAttributes(uId, user, dn);

                    try {
                        ldapTemplate.modifyAttributes(context);
                        logger.info("Password for" + uId + " changed successfully");
                        return HttpStatus.OK;
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.warn("Changing password for " + "\"" + uId + "\"" + " was unsuccessfully");

                        return HttpStatus.BAD_REQUEST;
                    }

                } else
                    return HttpStatus.FORBIDDEN;
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        } else
            return HttpStatus.FORBIDDEN;
    }

    @Override
    public String showProfilePic(HttpServletResponse response, User user) {
        File file = new File(uploadedFilesPath + user.getPhotoName());

        if (file.exists()) {
            try {
                String contentType = "image/png";
                response.setContentType(contentType);
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                // copy from in to out
                IOUtils.copy(in, out);
                out.close();
                in.close();
                return "OK";
            } catch (Exception e) {
                return "Problem";

            }
        }
        return "NotExist";
    }


    @Override
    public byte[] showProfilePic(User user) {
        File file = new File(uploadedFilesPath + user.getPhotoName());
        byte[] media = null;


        if (file.exists()) {
            try {
                String contentType = "image/png";
                FileInputStream out = new FileInputStream(file);
                // copy from in to out
                media = IOUtils.toByteArray(out);
                out.close();
                return media;
            } catch (Exception e) {
                return media;
            }
        }
        return media;
    }


    @Override
    public HttpStatus uploadProfilePic(MultipartFile file, String name) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());

        String s = timeStamp + file.getOriginalFilename();

        storageService.saveProfilePhoto(file, s);

        User user = retrieveUser(name);

        //remove old pic
        File oldPic = new File(uploadedFilesPath + user.getPhotoName());

        user.setPhotoName(s);
        if (update(user.getUserId(), user) == HttpStatus.OK) {
            oldPic.delete();
            logger.info("Setting profile pic for" + name + " was successful");
            return HttpStatus.OK;

        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public List<SimpleUser> retrieveUsersMain() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<SimpleUser> people = ldapTemplate.search(query().attributes("uid", "displayName", "ou", "createtimestamp").where("objectClass").is("person"),
                new SimpleUserAttributeMapper());
        List relatedUsers = new LinkedList();
        for (SimpleUser user : people) {
            if (user.getDisplayName() != null && !user.getUserId().equals("su")&& !user.getDisplayName().equals("Directory Superuser")) {
                relatedUsers.add(user);
            }
        }
        Collections.sort(relatedUsers);
        return relatedUsers;
    }

    @Override
    public List<SimpleUser> retrieveUsersMain(String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus) {
        List<SimpleUser> users = retrieveUsersMain();
        List<SimpleUser> sortTypeUsers;
        if (!sortType.equals("")) {
            if (sortType.equals("uid_m2M"))
                Collections.sort(users, SimpleUser.uidMinToMaxComparator);
            else if (sortType.equals("uid_M2m"))
                Collections.sort(users, SimpleUser.uidMaxToMinComparator);
            else if (sortType.equals("displayName_m2M"))
                Collections.sort(users, SimpleUser.displayNameMinToMaxComparator);
            else if (sortType.equals("displayName_M2m"))
                Collections.sort(users, SimpleUser.displayNameMaxToMinComparator);
        }
        sortTypeUsers = users;


        List<SimpleUser> groupFilterUsers = new LinkedList<>();
        if (!groupFilter.equals("")) {

            for (SimpleUser user : sortTypeUsers) {

                if (user.getMemberOf().contains(groupFilter))
                    groupFilterUsers.add(user);
            }

        } else
            groupFilterUsers = sortTypeUsers;

        List<SimpleUser> searchUidUsers = new LinkedList<>();

        if (!searchUid.equals("")) {

            searchUid = searchUid.toLowerCase();

            for (SimpleUser user : groupFilterUsers) {

                if (user.getUserId().contains(searchUid))
                    searchUidUsers.add(user);

            }

        } else
            searchUidUsers = groupFilterUsers;


        List<SimpleUser> searchDisplayNameUsers = new LinkedList<>();

        if (!searchDisplayName.equals("")) {

            searchDisplayName = searchDisplayName.toLowerCase();

            for (SimpleUser user : searchUidUsers) {

                if (user.getDisplayName().contains(searchDisplayName))
                    searchDisplayNameUsers.add(user);

            }

        } else
            searchDisplayNameUsers = searchUidUsers;


        List<SimpleUser> userStatusUsers = new LinkedList<>();

        if (!userStatus.equals("")) {

            for (SimpleUser user : searchDisplayNameUsers) {

                if (user.getStatus().equals(userStatus))
                    userStatusUsers.add(user);

            }

        } else
            userStatusUsers = searchDisplayNameUsers;


        return userStatusUsers;
    }

    @Override
    public User getName(String uid, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        if (tokenClass.checkToken(uid, token) == HttpStatus.OK)
            return ldapTemplate.search(query().attributes("givenName", "sn", "displayName").where("uid").is(uid),
                    userAttributeMapper).get(0);
        return null;
    }

    @Override
    public List<User> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));

        List<User> people = ldapTemplate.search(BASE_DN, andFilter.toString(), searchControls,
                userAttributeMapper);
        List<User> relatedPeople = new LinkedList<>();

        for (User user : people) {
            if (user.getDisplayName() != null && !user.getUserId().equals("su")) {
                relatedPeople.add(user);
            }

        }

        Collections.sort(relatedPeople);


        return relatedPeople;
    }

    @Override
    public User retrieveUser(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = new User();
        Tokens tokens = null;
        ExtraInfo extraInfo=null;
        if (!((ldapTemplate.search(query().where("uid").is(userId), userAttributeMapper)).toString() == "[]")) {
            user = ldapTemplate.lookup(buildDn.buildDn(userId), new String[]{"*", "+"}, userAttributeMapper);
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            tokens = mongoTemplate.findOne(query, Tokens.class, Token.collection);
            user.setTokens(tokens);

        }
        setRole(userId, user);
        if (user.getUserId() == null) return null;
        else return user;
    }

    @Override
    public List<JSONObject> checkMail(String email) {
        return emailClass.checkMail(email);
    }

    @Override
    public HttpStatus sendEmail(JSONObject jsonObject) {
        return emailClass.sendEmail(jsonObject);
    }

    @Override
    public org.json.simple.JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException {
        return dashboardData.retrieveDashboardData();
    }

    @Override
    public HttpStatus enable(String uid) {

        Name dn = buildDn.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);
        Boolean status = user.isEnabled();

        if (!status) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.info("Enabling " + uid + " was successful");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Enabling " + uid + " was unsuccessful");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }


    public HttpStatus removeCurrentEndTime(String uid) {

        Name dn = buildDn.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);

        if (user.getEndTime() != null) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus disable(String uid) {

        Name dn = buildDn.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);

        if (user.isEnabled()) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.info("Disabling " + uid + " was successful");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Disabling " + uid + " was unsuccessful");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus unlock(String uid) {

        Name dn = buildDn.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[2];

        User user = retrieveUser(uid);
        Boolean locked = user.isLocked();

        if (locked) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));
            modificationItems[1] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdFailureTime"));


            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.warn("Unlocking" + uid + "was successful");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Unlocking user" + uid + " was successful");

                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }

    }

    @Override
    public int requestToken(User user) {
        return tokenClass.requestToken(user);
    }

    @Override
    public HttpStatus massUpdate(List<User> users) {
        for (User user:users)
            update(user.getUserId(),user);

        return HttpStatus.OK;
    }


    @Override
    public ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus) {
        List<SimpleUser> allUsers = retrieveUsersMain(sortType, groupFilter, searchUid, searchDisplayName, userStatus);
        int n = (page) * number;

        if (n > allUsers.size())
            n = allUsers.size();

        int size = allUsers.size();

        int pages = (int) Math.ceil(size/number);


        int start = (page - 1) * number;


        List<SimpleUser> relativeUsers = new LinkedList<>();

        for (int i = start; i < n; i++)
             relativeUsers.add(allUsers.get(i));

        ListUsers finalList = new ListUsers(size, relativeUsers, pages);

        return finalList;
    }

    @Override
    public int sendEmail(String email, String cid, String answer) {
        return emailClass.sendEmail(email, cid, answer);
    }

    @Override
    public int sendEmail(String email, String uid, String cid, String answer) {
        return emailClass.sendEmail(email, uid, cid, answer);
    }


    public String createUrl(String userId, String token) {
        return BASE_URL + /*"" +*/ EMAILCONTROLLER + userId + "/" + token;

    }

    public HttpStatus updatePass(String userId, String pass, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = ldapTemplate.search(query().where("uid").is(userId), userAttributeMapper).get(0);

        setRole(userId, user);

        HttpStatus httpStatus = tokenClass.checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            user.setUserPassword(pass);
            Name dn = buildDn.buildDn(user.getUserId());

            DirContextOperations context = buildAttributes.buildAttributes(userId, user, dn);
            ldapTemplate.modifyAttributes(context);
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public JSONObject importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException {
        return importUsers.importFileUsers(file, sequence, hasHeader);
    }

    @Bean
    @Deprecated
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }
}

