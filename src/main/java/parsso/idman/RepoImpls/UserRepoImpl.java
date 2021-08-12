package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.Group.GroupsChecks;
import parsso.idman.Helpers.User.*;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.*;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserRepoImpl implements UserRepo {


    public ZoneId zoneId = ZoneId.of(Variables.ZONE);
    String model = "User";
    String userExtraInfoCollection = Variables.col_usersExtraInfo;
    @Value("${user.profile.access}")
    String profileAccessiblity;
    @Value("${skyroom.api.key}")
    String skyRoomApiKey;
    @Autowired
    ExcelAnalyzer excelAnalyzer;
    @Autowired
    SkyroomRepo skyroomRepo;
    @Autowired
    GroupRepo groupRepo;
    @Autowired
    SystemRefresh systemRefresh;
    @Autowired
    GroupsChecks groupsChecks;
    @Autowired
    Operations operations;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    ExpirePassword expirePassword;
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${get.users.time.interval}")
    private int apiHours;
    @Value("${user.profile.access}")
    private String access;
    @Value("${default.user.password}")
    private String defaultPassword;
    @Value("${profile.photo.path}")
    private String uploadedFilesPath;
    @Value("${skyroom.enable}")
    private String skyroomEnable;

    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private BuildAttributes buildAttributes;
    @Autowired
    private Token tokenClass;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserAttributeMapper userAttributeMapper;
    @Autowired
    private SimpleUserAttributeMapper simpleUserAttributeMapper;
    @Autowired
    private BuildDnUser buildDnUser;
    @Autowired
    private DashboardData dashboardData;
    @Autowired
    private ImportUsers importUsers;
    @Autowired
    private EmailService emailService;


    @Override
    public JSONObject create(String doerID, User p) {

        p.setUserId(p.getUserId().toLowerCase());

        Logger logger = LogManager.getLogger(doerID);

        UsersExtraInfo usersExtraInfo = null;

        if (p.getUserId() == null || p.getUserId() == "") {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invalidGroups", p.getUserId());
            return jsonObject;

        }

        User user = retrieveUsers(p.getUserId());

        try {
            if (user == null || user.getUserId() == null) {

                if (p.getDisplayName() == null || p.getDisplayName() == "" ||
                        p.getMail() == null || p.getMail() == "" || p.getStatus() == null || p.getStatus() == "") {
                    logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "failed", "essential parameter not exist").toString());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", p.getUserId());
                    if (p.getDisplayName() == null || p.getDisplayName() == "")
                        jsonObject.put("invalidParameter", "ِDisplayName");
                    if (p.getMail() == null || p.getMail() == "")
                        jsonObject.put("invalidParameter", "Mail");
                    if (p.getStatus() == null || p.getStatus() == "")
                        jsonObject.put("invalidParameter", "Status");
                    return jsonObject;
                }

                if (groupsChecks.checkGroup(p.getMemberOf())) {

                    //create user in ldap
                    Name dn = buildDnUser.buildDn(p.getUserId());
                    ldapTemplate.bind(dn, null, buildAttributes.BuildAttributes(p));

                    if (p.getStatus() != null)
                        if (p.getStatus().equals("disable"))
                            operations.disable(doerID, p.getUserId());


                    usersExtraInfo = new UsersExtraInfo(p, p.getPhoto(), p.isUnDeletable());
                    mongoTemplate.save(usersExtraInfo, userExtraInfoCollection);

                    logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "success", "").toString());

                    return new JSONObject();
                } else {
                    logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "failed", "group not exist").toString());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", p.getUserId());
                    jsonObject.put("invalidGroups", groupsChecks.invalidGroups(p.getMemberOf()));
                    return jsonObject;
                }
            } else {
                logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "failed", "already exist").toString());
                return importUsers.compareUsers(user, p);
            }
        } catch (Exception e) {
            if (p.getUserId() != null || !p.getUserId().equals("")) {
                logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "failed", "unknown reason").toString());
            } else
                logger.warn(new ReportMessage(model, "", "", "create", "failed", "UserId is empty").toString());
            return null;
        }
    }

    @Override
    public int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        return (int) mongoTemplate.count(queryBuilder(groupFilter, searchUid, searchDisplayName, userStatus), userExtraInfoCollection);
    }

    @Override
    public HttpStatus update(String doerID, String usid, User p) {


        Logger logger = LogManager.getLogger(doerID);

        p.setUserId(usid.trim());
        Name dn = buildDnUser.buildDn(p.getUserId());

        User user = retrieveUsers(p.getUserId());

        if (!retrieveUsers(usid).getRole().equals("USER") &&
                user.getRole().equals("USER") && access.equalsIgnoreCase("false"))
            return HttpStatus.FORBIDDEN;

        DirContextOperations context;

        //remove current pwdEndTime
        if (p.getEndTime() == null ||
                p.getEndTime().equals("")
                && user.getEndTime() != null)
            removeCurrentEndTime(p.getUserId());

        context = buildAttributes.buildAttributes(doerID, usid, p, dn);
        Query query = new Query(Criteria.where("userId").is(p.getUserId().toLowerCase()));
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, userExtraInfoCollection);

        try {
            usersExtraInfo.setUnDeletable(p.isUnDeletable());
        } catch (Exception e) {
            user.setUnDeletable(p.isUnDeletable());
        }

        if (p.getCStatus() != null) {
            if (p.getCStatus().equals("unlock") || p.getCStatus().equals("enable"))
                p.setStatus("enable");
            else if (p.getCStatus().equals("disable"))
                p.setStatus("disable");
            usersExtraInfo.setStatus(p.getStatus());
        } else
            usersExtraInfo.setStatus("enable");


        if (p.getMemberOf() != null)
            usersExtraInfo.setMemberOf(p.getMemberOf());


        if (p.getDisplayName() != null)
            usersExtraInfo.setDisplayName(p.getDisplayName().trim());

        if (p.getPhoto() != null)
            usersExtraInfo.setPhotoName(p.getPhoto());

        if (p.isUnDeletable())
            usersExtraInfo.setUnDeletable(true);

        if (p.getUserPassword() != null && !p.getUserPassword().equals(""))
            context.setAttributeValue("userPassword", p.getUserPassword());

        try {

            ldapTemplate.modifyAttributes(context);

            mongoTemplate.save(usersExtraInfo, userExtraInfoCollection);

            logger.warn(new ReportMessage(model, usid, "", "update", "success", "").toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(new ReportMessage(model, usid, "", "update", "failed", "Writing to DB").toString());

        }

        return HttpStatus.OK;
    }

    @Override
    public JSONObject createUserImport(String doerID, User p) {

        if (p.getUserPassword() == null)
            p.setUserPassword(defaultPassword);

        return create(doerID, p);
    }

    @Override
    public List<String> remove(String doer, JSONObject jsonObject) {

        Logger logger = LogManager.getLogger(doer);

        List<User> people = new LinkedList<>();
        List<String> undeletables = new LinkedList<>();
        if (jsonObject.size() == 0)
            people = retrieveUsersFull();
        else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            Iterator<String> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                User user = retrieveUsers(iterator.next());
                if (user != null)
                    people.add(user);
            }
        }

        if (people != null)
            for (User user : people) {
                if (user.isUnDeletable()) {
                    undeletables.add(user.getUserId());
                    continue;
                }
                Name dn = buildDnUser.buildDn(user.getUserId());
                Query query = new Query(new Criteria("userId").is(user.getUserId()));

                try {
                    ldapTemplate.unbind(dn);
                    mongoTemplate.remove(query, UsersExtraInfo.class, userExtraInfoCollection);
                    logger.warn(new ReportMessage(model, user.getUserId(), "", "remove", "success", "").toString());

                } catch (Exception e) {
                    logger.warn(new ReportMessage(model, user.getUserId(), "", "remove", "failed", "unknown reason").toString());

                }

            }

        return undeletables;
    }

    public User setRole(User p) {
        String role;

        try {
            role = p.getUsersExtraInfo().getRole();

        } catch (Exception e) {
            role = "USER";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = null;

        if (auth != null) {
            updatedAuthorities = new ArrayList<>(auth.getAuthorities());

            if (auth != null && auth.getName().equals(p.getUserId())) {
                updatedAuthorities.remove(0);

                updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
        p.setRole(role);

        return p;

    }

    @Override
    public HttpStatus changePassword(String uId, String oldPassword, String newPassword, String token) {

        Logger logger = LogManager.getLogger(uId);

        User user = retrieveUsers(uId);

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", uId));


        if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), oldPassword)) {
            if (token != null) {
                if (tokenClass.checkToken(uId, token) == HttpStatus.OK) {

                    DirContextOperations contextUser;

                    contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
                    contextUser.setAttributeValue("userPassword", newPassword);

                    try {
                        ldapTemplate.modifyAttributes(contextUser);
                        logger.warn(new ReportMessage(model, uId, "password", "change", "success", "").toString());
                        return HttpStatus.OK;
                    } catch (Exception e) {
                        logger.warn(new ReportMessage(model, uId, "password", "change", "failed", "writing to LDAP").toString());
                        return HttpStatus.BAD_REQUEST;
                    }

                } else
                    return HttpStatus.METHOD_NOT_ALLOWED;
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        } else
            return HttpStatus.FORBIDDEN;
    }

    @Override
    public String showProfilePic(HttpServletResponse response, User user) {
        if (user.getPhoto() == null)
            return "NotExist";

        File file = new File(uploadedFilesPath + user.getPhoto());

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
        File file = new File(uploadedFilesPath + user.getUsersExtraInfo().getPhotoName());
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

        Logger logger = LogManager.getLogger(name);


        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());

        String s = timeStamp + file.getOriginalFilename();

        storageService.saveProfilePhoto(file, s);

        User user = retrieveUsers(name);

        //remove old pic
        File oldPic = new File(uploadedFilesPath + user.getPhoto());

        user.setPhoto(s);
        logger.warn(new ReportMessage(model, name, "profile image", "change", "success", "").toString());
        if (update(user.getUserId(), user.getUserId(), user) == HttpStatus.OK) {
            oldPic.delete();

            return HttpStatus.OK;

        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public List<UsersExtraInfo> retrieveUsersMain(int page, int number) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String[] array = {"uid", "displayName", "ou", "createtimestamp", "pwdAccountLockedTime"};
        searchControls.setReturningAttributes(array);

        int limit = number;
        int skip = (page - 1) * limit;

        List<UsersExtraInfo> usersExtraInfos = null;

        if (page == -1 && number == -1)
            usersExtraInfos = mongoTemplate.find(new Query(), UsersExtraInfo.class, userExtraInfoCollection);
        else
            usersExtraInfos = mongoTemplate.find(new Query().skip(skip).limit(limit), UsersExtraInfo.class, userExtraInfoCollection);

        OrFilter orFilter = new OrFilter();

        for (int i = 0; i < usersExtraInfos.size(); i++)
            orFilter.or(new EqualsFilter("uid", usersExtraInfos.get(i).getUserId()));

        return ldapTemplate.search("ou=People," + BASE_DN, orFilter.encode(), searchControls, new SimpleUserAttributeMapper());

    }

    @Override
    public String getByMobile(String mobile) {
        EqualsFilter equalsFilter = new EqualsFilter("mobile", mobile);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String s = null;
        try {
            s = ldapTemplate.search("ou=People," + BASE_DN, equalsFilter.encode(), searchControls, new AttributesMapper<String>() {
                public String mapFromAttributes(Attributes attrs)
                        throws NamingException {
                    if (attrs.get("uid") != null)
                        return attrs.get("uid").get().toString();

                    return "";
                }
                }).get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    @Override
    public ListUsers retrieveUsersMain(int page, int nCount, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        Thread thread = new Thread() {
            public void run() {
                systemRefresh.refreshLockedUsers();
            }
        };
        thread.start();

        int limit = nCount;
        int skip = (page - 1) * limit;

        Query query = queryBuilder(groupFilter, searchUid, searchDisplayName, userStatus);


        if (sortType.equals(""))
            query.with(Sort.by(Sort.Direction.ASC, "userId"));


        else if (sortType.equals("uid_m2M"))
            query.with(Sort.by(Sort.Direction.ASC, "userId"));

        else if (sortType.equals("uid_M2m"))
            query.with(Sort.by(Sort.Direction.DESC, "userId"));

        else if (sortType.equals("displayName_m2M"))
            query.with(Sort.by(Sort.Direction.ASC, "displayName"));

        else if (sortType.equals("displayName_M2m"))
            query.with(Sort.by(Sort.Direction.DESC, "displayName"));

        query.with(Sort.by(Sort.Direction.DESC, "_id"));

        List<UsersExtraInfo> userList = mongoTemplate.find(query.skip(skip).limit(limit),
                UsersExtraInfo.class, userExtraInfoCollection);

        int size = retrieveUsersSize(groupFilter, searchUid, searchDisplayName, userStatus);

        return new ListUsers(size, userList, (int) Math.ceil((double) size / (double) nCount));
    }

    private Query queryBuilder(String groupFilter, String searchUid, String searchDisplayName, String userStatus) {
        Query query = new Query();
        if (!searchUid.equals(""))
            query.addCriteria(Criteria.where("userId").regex(searchUid));
        if (!searchDisplayName.equals(""))
            query.addCriteria(Criteria.where("displayName").regex(searchDisplayName));
        if (!userStatus.equals("")) {
            query.addCriteria(Criteria.where("status").is(userStatus));

        }
        if (!groupFilter.equals(""))
            query.addCriteria(Criteria.where("memberOf").all(groupFilter));
        return query;

    }

    @Override
    public User getName(String uid, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        if (tokenClass.checkToken(uid, token) == HttpStatus.OK)
            return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", uid).encode(), searchControls,
                    userAttributeMapper).get(0);
        return null;
    }

    @Override
    public List<User> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));

        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls,
                userAttributeMapper);
        List<User> relatedPeople = new LinkedList<>();

        for (User user : people) {
            if (user != null && user.getDisplayName() != null) {
                relatedPeople.add(user);
            }

        }

        Collections.sort(relatedPeople);

        return relatedPeople;
    }

    @Override
    public List<User> getUsersOfOu(String ou) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("ou", ou));

        List<User> users = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls,
                userAttributeMapper);

        for (User user : users)
            user.setUsersExtraInfo(mongoTemplate.findOne(new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        return users;
    }

    @Override
    public HttpStatus updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou) {
        Logger logger = LogManager.getLogger(doerID);

        try {

            for (User user : getUsersOfOu(old_ou)) {

                DirContextOperations context = buildAttributes.buildAttributes(doerID, user.getUserId(), user, buildDnUser.buildDn(user.getUserId()));

                context.removeAttributeValue("ou", old_ou);
                context.addAttributeValue("ou", new_ou);

                try {
                    ldapTemplate.modifyAttributes(context);
                } catch (Exception e) {
                    logger.warn(new ReportMessage(model, user.getUserId(), "group", "update", "failed", "writing to ldap").toString());

                }
            }
            logger.warn(new ReportMessage(model, doerID, "groups", "update", "success", "").toString());

            return HttpStatus.OK;
        } catch (Exception e) {
            logger.warn(new ReportMessage(model, doerID, "groups", "update", "failed", "writing to ldap").toString());

            return HttpStatus.FORBIDDEN;
        }
    }


    @Override
    public User retrieveUsers(String userId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        User user = new User();
        UsersExtraInfo usersExtraInfo = null;
        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", userId).encode(), searchControls, userAttributeMapper);

        if (people.size() != 0) {
            user = people.get(0);
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection);
            user.setUsersExtraInfo(mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection));
            try {
                user.setUnDeletable(usersExtraInfo.isUnDeletable());
            } catch (Exception e) {
                user.setUnDeletable(false);
            }

        }

        user.setSkyroomAccess(skyRoomAccess(user));


        if (user.getRole() == null)
            user = setRole(user);
        if (user.getRole().equals("USER") && profileAccessiblity.equalsIgnoreCase("FALSE"))
            user.setProfileInaccessibility(true);

        if (user.getUserId() == null)
            return null;

        else return user;
    }

    private Boolean skyRoomAccess(User user) {
        Boolean isEnable = skyroomEnable.equalsIgnoreCase("true") ? true :false;

        boolean accessRole = false;
        if (user.getUsersExtraInfo().getRole().equalsIgnoreCase("superadmin") ||
                user.getUsersExtraInfo().getRole().equalsIgnoreCase("admin") ||
                user.getUsersExtraInfo().getRole().equalsIgnoreCase("supporter") ||
                user.getUsersExtraInfo().getRole().equalsIgnoreCase("presenter"))
            accessRole = true;

        return isEnable & accessRole;
    }

    @Override
    public List<UsersExtraInfo> retrieveGroupsUsers(String groupId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("ou", groupId).encode(), searchControls, simpleUserAttributeMapper);
    }


    public HttpStatus removeCurrentEndTime(String uid) {

        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUsers(uid);

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
    public int requestToken(User user) {
        return tokenClass.requestToken(user);
    }

    @Override
    public JSONObject massUpdate(String doerID, List<User> users) {
        int nCount = users.size();
        int nSuccessful = 0;
        for (User user : users) {
            if (user != null && user.getUserId() != null)
                try {
                    update(doerID, user.getUserId(), user);
                    nSuccessful++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nCount", nCount);
        jsonObject.put("nSuccessful", nSuccessful);
        jsonObject.put("nUnSuccessful", nCount - nSuccessful);

        return jsonObject;
    }

    @Override
    public ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int number) {

        List<UsersExtraInfo> users = retrieveGroupsUsers(groupId);

        CollectionUtils.filter(users, PredicateUtils.notNullPredicate());

        int n = (page) * number > users.size() ? users.size() : (page) * number;

        int size = users.size();
        int start = (page - 1) * number;

        List<UsersExtraInfo> relativeUsers = new LinkedList<>();

        for (int i = start; i < n; i++)
            relativeUsers.add(users.get(i));

        CollectionUtils.filter(relativeUsers, PredicateUtils.notNullPredicate());

        return new ListUsers(size, relativeUsers, (int) Math.ceil((double) size / (double) number));

    }

    @Override
    public HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) {
        List<String> add = (List<String>) gu.get("add");
        List<String> remove = (List<String>) gu.get("remove");
        List<String> groups = new LinkedList<>();
        for (String uid : add) {
            User user = retrieveUsers(uid);
            if (user.getMemberOf() != null) {
                if (!user.getMemberOf().contains(groupId))
                    user.getMemberOf().add(groupId);
            } else {
                groups.add(groupId);
                user.setMemberOf(groups);
            }


            update(doerID, uid, user);
        }
        for (String uid : remove) {
            User user = retrieveUsers(uid);
            if (user.getMemberOf().contains(groupId)) {
                user.getMemberOf().remove(groupId);
                update(doerID, uid, user);
            }
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus syncUsersDBs() {
        List<UsersExtraInfo> simpleUsers = retrieveUsersMain(-1, -1);

        if (!mongoTemplate.collectionExists(userExtraInfoCollection))
            mongoTemplate.createCollection(userExtraInfoCollection);

        for (UsersExtraInfo simpleUser : simpleUsers) {
            mongoTemplate.remove(new Query(Criteria.where("userId").is(simpleUser.getUserId())), userExtraInfoCollection);
            mongoTemplate.save(simpleUser, userExtraInfoCollection);
        }

        return HttpStatus.OK;
    }

    @Override
    public int sendEmail(String email, String uid, String cid, String answer) {
        if (uid != null)
            return emailService.sendMail(email, uid, cid, answer);
        return emailService.sendMail(email, cid, answer);
    }

    public String createUrl(String userId, String token) {
        return BASE_URL + /*"" +*/  "/api/public/validateEmailToken/" + userId + "/" + token;
    }

    public HttpStatus resetPassword(String userId, String pass, String token) {

        Logger logger = LogManager.getLogger(userId);

        User user = retrieveUsers(userId);



        user = setRole(user);

        try {
            operations.unlock("SYSTEM", userId);
        } catch (Exception e) {
        }

        HttpStatus httpStatus;
        if (token.equals("ParssoIdman"))
            httpStatus = HttpStatus.OK;
        else
            httpStatus = tokenClass.checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            DirContextOperations contextUser;

            contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
            contextUser.setAttributeValue("userPassword", pass);

            try{
                removeCurrentEndTime(userId);
                ldapTemplate.modifyAttributes(contextUser);

                logger.warn(new ReportMessage(model, userId, "password", "reset", "success", "").toString());

            }catch (Exception e){
                logger.warn(new ReportMessage(model, userId, "password", "reset", "failed", "writing to ldap").toString());
            }
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
        InputStream insfile = file.getInputStream();

        if (file.getOriginalFilename().endsWith(".xlsx")) {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbookXLSX = null;
            workbookXLSX = new XSSFWorkbook(insfile);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbookXLSX.getSheetAt(0);

            return excelAnalyzer.excelSheetAnalyze(doer, sheet, ou, true);

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS = null;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            return excelAnalyzer.excelSheetAnalyze(doer, xlssheet, ou, true);

        } else if (file.getOriginalFilename().endsWith(".csv")) {

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            return excelAnalyzer.csvSheetAnalyzer(doer, csvReader, ou, true);
        }

        return null;
    }

    @Override
    public List<String> expirePassword(String doer, JSONObject jsonObject) {

        List<UsersExtraInfo> users = new LinkedList<>();

        if (((List) jsonObject.get("names")).size() == 0) {
            users.addAll(mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        } else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            for (Object temp : jsonArray)
                users.add(mongoTemplate.findOne(new Query(Criteria.where("userId").is(temp.toString())), UsersExtraInfo.class, Variables.col_usersExtraInfo));
        }

        return expirePassword.expire(doer, users);
    }
}

