package parsso.idman.RepoImpls;

import net.minidev.json.JSONArray;
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
import parsso.idman.Models.*;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.*;
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


    @Override
    public JSONObject create(User p) {

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
                mongoTemplate.save(tokens,Token.collection);


                if (p.getCStatus() != null) {
                    if (p.getCStatus().equals("disable"))
                        disable(p.getUserId());
                }

                logger.info("User "+"\""+p.getUserId()+"\""+" in "+new  Date()+" created");
                return new JSONObject();
            } else {
                logger.warn("User "+"\""+p.getUserId()+"\""+" is exist. So it cannot be created");

                return new ImportUsers().compareUsers(user, p);
            }
        } catch (Exception e) {
            logger.warn("Creating user "+"\""+p.getUserId()+"\""+" faced a problem");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject createUserImport(User p) {

        User user = retrieveUser(p.getUserId());
        p.setUserPassword(defaultPassword);


        try {
            if (user == null) {
                Name dn = buildDn.buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes.BuildAttributes(p));
                //logger.info("User "+user.getUserId() + " created");
                return new JSONObject();
            } else {

                return new ImportUsers().compareUsers(user, p);
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
        if (user.getEndTime()!=null&&p.getEndTime()==null)
            removeCurrentEndTime(uid);


        context = buildAttributes.buildAttributes(uid, p, dn);


        try {
            ldapTemplate.modifyAttributes(context);

            logger.info("User "+"\""+p.getUserId()+"\"" + "in "+new  Date()+"updated");
            return HttpStatus.OK;

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("User "+"\""+p.getUserId()+"\"" + "faced a problem");
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public String remove(String userId) {
        Name dn = buildDn.buildDn(String.valueOf(userId));
        try {
            ldapTemplate.unbind(dn);
            Query query = new Query(Criteria.where("userId").is(userId));
            mongoTemplate.remove(query,Token.collection);
            logger.info("User "+userId+" in "+new  Date()+" removed");

        } catch (Exception e) {
            logger.info("Removing user "+"\""+userId+"\""+" faced a problem");

            e.printStackTrace();
        }
        return userId + " removed successfully";
    }

    @Override
    public String remove() {
        List<User> people = retrieveUsersFull();
        for (User user : people) {
            Name dn = buildDn.buildDn(user.getUserId());

            try {
                logger.info("All users removed");

                ldapTemplate.unbind(dn);

            } catch (Exception e) {
                logger.info("Removing all users faced a problem");
                e.printStackTrace();
            }


        }

        return "All users removed successfully";
    }

    public void setRole(String uid, User p) {
        String role = null;

        if (uid.equals("su")) {
            role = "SUPERADMIN";


        }
        else {
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
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());

        if (auth.getName().equals(p.getUserId())) {
            updatedAuthorities.remove(0);

            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));


            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(newAuth);
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
                        logger.info("Password for"+ uId+ " changed");
                        return HttpStatus.OK;
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.warn("Changing password for "+"\""+ uId+"\""+ " faced a problem");

                        return HttpStatus.EXPECTATION_FAILED;
                    }

                } else
                    return HttpStatus.FORBIDDEN;
            } else {
                user.setUserPassword(newPassword);
                Name dn = buildDn.buildDn(uId);
                DirContextOperations context = buildAttributes.buildAttributes(uId, user, dn);

                try {
                    ldapTemplate.modifyAttributes(context);
                    return HttpStatus.OK;
                } catch (Exception e) {
                    e.printStackTrace();
                    return HttpStatus.EXPECTATION_FAILED;
                }
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
    public HttpStatus uploadProfilePic(MultipartFile file, String name) {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());

        String s = timeStamp + file.getOriginalFilename();

        storageService.save(file, s);

        User user = retrieveUser(name);

        //remove old pic
        File oldPic = new File(uploadedFilesPath + user.getPhotoName());

        user.setPhotoName(s);
        if (update(user.getUserId(), user) == HttpStatus.OK) {
            oldPic.delete();
            logger.info("profile pic for"+name+" is set");
            return HttpStatus.OK;

        }
        return HttpStatus.BAD_REQUEST;
    }


    @Override
    public List<SimpleUser> retrieveUsersMain() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<SimpleUser> people = ldapTemplate.search(query().attributes("uid", "displayName", "ou").where("objectClass").is("person"),
                new SimpleUserAttributeMapper());
        List relatedUsers = new LinkedList();
        for (SimpleUser user : people) {
            if (!(user.getUserId().equals("admin")) && user.getDisplayName() != null&& !user.getUserId().equals("su")) {
                relatedUsers.add(user);
            }
        }
        return relatedUsers;
    }

    @Override
    public User getName(String uid, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        if (tokenClass.checkToken(uid, token) == HttpStatus.OK)
            return ldapTemplate.search(query().attributes("givenName", "sn", "displayName").where("uid").is(uid)
                    ,
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
            if (user.getDisplayName() != null&& !user.getUserId().equals("su")) {
                relatedPeople.add(user);
            }

        }


        return relatedPeople;
    }

    @Override
    public User retrieveUser(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = new User();
        Tokens tokens = new Tokens();
        if (!((ldapTemplate.search(query().where("uid").is(userId), userAttributeMapper)).toString() == "[]")) {
            user = ldapTemplate.lookup(buildDn.buildDn(userId), new String[]{"*", "+"}, userAttributeMapper);
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            tokens = mongoTemplate.findOne(query,Tokens.class, Token.collection);
            user.setTokens(tokens);
        }
        setRole(userId,user);
        if (user.getUserId() == null) return null;
        else return user;
    }

    @Override
    public List<JSONObject> checkMail(String email) {
        return emailClass.checkMail(email);
    }

    @Override
    public HttpStatus sendEmail(String email, String uid) {
        return emailClass.sendEmail(email,uid);
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
                logger.info(uid + " is enabled");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("A problem occurred for enabling "+uid);
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
                logger.info(uid + " is disabled");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("A problem occurred while disabling "+uid);
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }


    public HttpStatus addEndTime(String uid, String date) {

        Name dn = buildDn.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);


        if (user.isEnabled()) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime", Time.convertDateTimeJalali(date)));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.info("EndTime for "+ uid + " set");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Adding EndTime for "+ uid + " faced a problem");
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
                logger.warn(uid + "unlocking faced a problem");
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Unlocking user"+ uid + " faced a problem");

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
    public List<SimpleUser> retrieveUsersPagination(int page, int number) {
        List<SimpleUser> allUsers = retrieveUsersMain();

        int n = (page)*number;

        if (n>allUsers.size())
            n = allUsers.size();

        List<SimpleUser> relativeUsers= new LinkedList<SimpleUser>();

        int start = (page-1)*number;

        for (int i=start; i<n; i++)
            relativeUsers.add(allUsers.get(i));

        return relativeUsers;
    }

    @Override
    public int sendEmail(String email, String cid, String answer) {
            return emailClass.sendEmail(email,cid,answer);
    }

    @Override
    public int sendEmail(String email, String uid, String cid, String answer) {
        return emailClass.sendEmail(email,uid,cid,answer);
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
    public JSONArray importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException {
        return new ImportUsers().importFileUsers(file, sequence, hasHeader);
    }

    @Bean
    @Deprecated
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }
}

