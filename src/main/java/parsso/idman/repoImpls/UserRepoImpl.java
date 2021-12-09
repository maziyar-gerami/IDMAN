package parsso.idman.repoImpls;


import lombok.val;
import net.minidev.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import parsso.idman.helpers.TimeHelper;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.group.GroupsChecks;
import parsso.idman.helpers.oneTimeTasks.RunOneTime;
import parsso.idman.helpers.user.*;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.ListUsers;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UserLoggedIn;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.postConstruct.LogsTime;
import parsso.idman.repos.*;

import javax.annotation.PostConstruct;
import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class UserRepoImpl implements UserRepo {
    @Value("${user.profile.access}")
    String profileAccessiblity;
    @Value("${skyroom.api.key}")
    String skyRoomApiKey;
    @Autowired
    ExcelAnalyzer excelAnalyzer;
    @Autowired
    UniformLogger uniformLogger;
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
    @Autowired
    LogsRepo.TranscriptRepo transcriptRepo;
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${user.profile.access}")
    private String access;
    @Value("${default.user.password}")
    private String defaultPassword;
    @Value("${profile.photo.path}")
    private String uploadedFilesPath;
    @Value("${skyroom.enable}")
    private String skyroomEnable;
    @Value("${password.change.notification}")
    private String passChangeNotification;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private BuildAttributes buildAttributes;
    @Autowired
    private Token tokenClass;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    UserAttributeMapper userAttributeMapper;
    @Autowired
    private BuildDnUser buildDnUser;
    @Autowired
    private ImportUsers importUsers;
    @Autowired
    private EmailService emailService;
    @Autowired
    InstantMessage instantMessage;

    @Override
    public JSONObject create(String doerID, User p) {

        p.setUserId(p.getUserId().toLowerCase());

        UsersExtraInfo usersExtraInfo;

        if (p.getUserId() == null || p.getUserId().equals("")) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invalidGroups", p.getUserId());
            return jsonObject;

        }
        User user;
        try {
            user = retrieveUsers(p.getUserId());

        } catch (Exception e) {
            user = null;
        }


        try {
            if (user == null || user.getUserId() == null) {

                if (p.getDisplayName() == null || p.getDisplayName().equals("") ||
                        p.getMail() == null || p.getMail().equals("") || p.getStatus() == null || p.getStatus().equals("")) {
                    uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.getUserId(), "", Variables.ACTION_CREATE,
                            Variables.RESULT_FAILED, "essential parameter not exist"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", p.getUserId());
                    if (p.getDisplayName() == null || p.getDisplayName().equals(""))
                        jsonObject.put("invalidParameter", "ِDisplayName");
                    if (p.getMail() == null || p.getMail().equals(""))
                        jsonObject.put("invalidParameter", "Mail");
                    if (p.getStatus() == null || p.getStatus().equals(""))
                        jsonObject.put("invalidParameter", "Status");
                    return jsonObject;
                }

                if (groupsChecks.checkGroup(p.getMemberOf())) {

                    //create user in ldap
                    Name dn = buildDnUser.buildDn(p.getUserId(),BASE_DN);
                    ldapTemplate.bind(dn, null, buildAttributes.build(p));

                    if (p.getStatus() != null)
                        if (p.getStatus().equals("disable"))
                            operations.disable(doerID, p.getUserId());

                    usersExtraInfo = new UsersExtraInfo(p, p.getPhoto(), p.isUnDeletable());
                    mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);

                    uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, p.getUserId(), "", Variables.ACTION_CREATE,
                            Variables.RESULT_SUCCESS, ""));

                    return new JSONObject();
                } else {
                    uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.getUserId(), "", Variables.ACTION_CREATE,
                            Variables.RESULT_FAILED, "group not exist"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", p.getUserId());
                    jsonObject.put("invalidGroups", groupsChecks.invalidGroups(p.getMemberOf()));
                    return jsonObject;
                }
            } else {
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.getUserId(), "", Variables.ACTION_CREATE,
                        Variables.RESULT_FAILED, "already exist"));
                return importUsers.compareUsers(user, p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.getUserId(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Unknown reason"));
            return null;
        }
    }


    public int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        return (int) mongoTemplate.count(queryBuilder(groupFilter, searchUid, searchDisplayName, userStatus), Variables.col_usersExtraInfo);
    }

    @Override
    @Cacheable("userPhoto")
    public HttpStatus update(String doerID, String usid, User p) {

        p.setUserId(usid.trim());
        Name dn = buildDnUser.buildDn(p.getUserId(),BASE_DN);

        User user = retrieveUsers(p.getUserId());

        if (!retrieveUsers(usid).getRole().equals("USER") &&
                user.getRole().equals("USER") && access.equalsIgnoreCase("false"))
            return HttpStatus.FORBIDDEN;

        DirContextOperations context;

        //remove current pwdEndTime
        if (p.getExpiredTime() == null ||
                p.getExpiredTime().equals("")
                        && user.getExpiredTime() != null)
            removeCurrentEndTime(p.getUserId());

        context = buildAttributes.buildAttributes(doerID, usid, p, dn);
        Query query = new Query(Criteria.where("userId").is(p.getUserId().toLowerCase()));
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);

        try {
            Objects.requireNonNull(usersExtraInfo).setUnDeletable(p.isUnDeletable());
        } catch (Exception e) {
            user.setUnDeletable(p.isUnDeletable());
        }

        if (p.getCStatus() != null) {
            if (p.getCStatus().equals("unlock") || p.getCStatus().equals("enable"))
                p.setStatus("enable");
            else if (p.getCStatus().equals("disable"))
                p.setStatus("disable");
            Objects.requireNonNull(usersExtraInfo).setStatus(p.getStatus());
        } else
            Objects.requireNonNull(usersExtraInfo).setStatus("enable");

        if (p.getMemberOf() != null)
            usersExtraInfo.setMemberOf(p.getMemberOf());

        if (p.getDisplayName() != null)
            usersExtraInfo.setDisplayName(p.getDisplayName().trim());

        if (p.getPhoto() != null)
            usersExtraInfo.setPhotoName(p.getPhoto());

        if (p.isUnDeletable())
            usersExtraInfo.setUnDeletable(true);

        usersExtraInfo.setTimeStamp(new Date().getTime());

        if (p.getUserPassword() != null && !p.getUserPassword().equals("")) {
            context.setAttributeValue("userPassword", p.getUserPassword());
            p.setPasswordChangedTime(Long.parseLong(TimeHelper.epochToDateLdapFormat(new Date().getTime()).substring(0, 14)));


        } else
            p.setPasswordChangedTime(user.getPasswordChangedTime());

        try {
            ldapTemplate.modifyAttributes(context);
            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
        }catch (org.springframework.ldap.InvalidAttributeValueException e){
            uniformLogger.warn(p.getUserId(), new ReportMessage(Variables.MODEL_USER, p.getUserId(), Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
            return HttpStatus.FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Writing to DB"));
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

        List<User> people = new LinkedList<>();
        List<String> undeletables = new LinkedList<>();
        if (jsonObject.size() == 0)
            people = retrieveUsersFull();
        else {
            val jsonArray = (ArrayList<String>) jsonObject.get("names");
            for (String s : jsonArray) {
                User user = retrieveUsers(s);
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
                Name dn = buildDnUser.buildDn(user.getUserId(),BASE_DN);
                Query query = new Query(new Criteria("userId").is(user.getUserId()));

                try {
                    ldapTemplate.unbind(dn);
                    mongoTemplate.remove(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
                    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.getUserId(), "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

                } catch (Exception e) {
                    e.printStackTrace();
                    uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.getUserId(), "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, "unknown reason"));

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

        List<GrantedAuthority> updatedAuthorities;

        if (auth != null) {
            updatedAuthorities = new ArrayList<>(auth.getAuthorities());

            if (auth.getName().equals(p.getUserId())) {
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
    public HttpStatus changePassword(String uId, String newPassword, String token) {

        User user = retrieveUsers(uId);

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", uId));

        //Checking previous password, disabled
        //if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), oldPassword)) {
        if (token != null) {
            if (tokenClass.checkToken(uId, token) == HttpStatus.OK) {

                DirContextOperations contextUser;
                contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId(),BASE_DN));
                contextUser.setAttributeValue("userPassword", newPassword);

                try {
                    ldapTemplate.modifyAttributes(contextUser);
                    uniformLogger.info(uId, new ReportMessage(Variables.MODEL_USER, uId, Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
                    if (passChangeNotification.equals("on"))
                        instantMessage.sendPasswordChangeNotif(user);

                    return HttpStatus.OK;
                } catch (org.springframework.ldap.InvalidAttributeValueException e){
                    uniformLogger.warn(uId, new ReportMessage(Variables.MODEL_USER, uId, Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
                    return HttpStatus.FOUND;
                }

            } else
                return HttpStatus.METHOD_NOT_ALLOWED;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
        //} else
        //return HttpStatus.FORBIDDEN;
    }
    @Override
    @Cacheable(value = "currentPic", key = "user.userId")
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
                FileInputStream out = new FileInputStream(file);
                // copy from in to out
                media = IOUtils.toByteArray(out);
                out.close();
                return media;
            } catch (Exception e) {
                return media;
            }
        }
        return null;
    }

    @Override
    public boolean uploadProfilePic(MultipartFile file, String name) {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());

        String s = timeStamp + file.getOriginalFilename();

        storageService.saveProfilePhoto(file, s);

        User userUpdate = retrieveUsers(name);

        //remove old pic
        File oldPic = new File(uploadedFilesPath + userUpdate.getPhoto());

        userUpdate.setPhoto(s);
        if (update(userUpdate.getUserId(), userUpdate.getUserId(), userUpdate) != null) {
            oldPic.delete();
            return true;
        }
        return false;
    }

    @Override
    public List<UsersExtraInfo> retrieveUsersMain(int page, int number) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String[] array = {"uid", "displayName", "ou", "createtimestamp", "pwdAccountLockedTime"};
        searchControls.setReturningAttributes(array);

        int skip = (page - 1) * number;

        List<UsersExtraInfo> usersExtraInfos;

        if (page == -1 && number == -1)
            usersExtraInfos = mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo);
        else
            usersExtraInfos = mongoTemplate.find(new Query().skip(skip).limit(number), UsersExtraInfo.class, Variables.col_usersExtraInfo);

        OrFilter orFilter = new OrFilter();

        for (UsersExtraInfo usersExtraInfo : usersExtraInfos)
            orFilter.or(new EqualsFilter("uid", usersExtraInfo.getUserId()));

        return ldapTemplate.search("ou=People," + BASE_DN, orFilter.encode(), searchControls, new SimpleUserAttributeMapper());

    }
    @Override
    public HttpStatus changePasswordPublic(String userId, String currentPassword, String newPassword) {

        User user = retrieveUsers(userId);

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", userId));


        UsersExtraInfo usersExtraInfo = retrieveUserMain(userId);
        if (usersExtraInfo==null)
            return HttpStatus.NOT_FOUND;

        if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), currentPassword)) {
            DirContextOperations contextUser;

            if (usersExtraInfo.isLoggedIn())
                return HttpStatus.FORBIDDEN;

            contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId(),BASE_DN));
            contextUser.setAttributeValue("userPassword", newPassword);
            try {
                System.out.println("try");
                ldapTemplate.modifyAttributes(contextUser);
                System.out.println("modified");
                usersExtraInfo.setLoggedIn(true);
                System.out.println("setLoggedn");
                mongoTemplate.remove(new Query(Criteria.where("userId").is(userId)),Variables.col_usersExtraInfo);
                System.out.println(userId +" removed");
                mongoTemplate.save(usersExtraInfo,Variables.col_usersExtraInfo);
                System.out.println(userId + " saved");
            }catch (org.springframework.ldap.InvalidAttributeValueException e){
                return HttpStatus.FOUND;
            }catch (Exception e){
                return HttpStatus.EXPECTATION_FAILED;
            }

        } else {
            return  HttpStatus.NOT_FOUND;
        }

        return HttpStatus.OK;
    }

    @Override
    public int authenticate(String userId, String password) {
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", userId));

        if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), password)) {
            if (retrieveUserMain(userId).isLoggedIn())
                return 1;
            else
                return 2;
        }else
            return 0;
        }

    @Override
    public boolean deleteProfilePic(User user) {

        File oldPic = new File(uploadedFilesPath + user.getPhoto());
        user.getUsersExtraInfo().setPhotoName(null);
        update(user.getUserId(),user.getUserId(),user);
        return oldPic.delete();
    }

    @Override
    public void setIfLoggedIn() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String[] array = {"uid","pwdHistory"};
        searchControls.setReturningAttributes(array);
        
        EqualsFilter equalsFilter = new EqualsFilter("objectclass","person");
        
        List<UserLoggedIn> usersLoggedIn = ldapTemplate.search("ou=People," + BASE_DN, equalsFilter.encode(), searchControls, new SimpleUserAttributeMapper.LoggedInUserAttributeMapper());

        int c=0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        for (UserLoggedIn userLoggedIn:usersLoggedIn ) {
            UsersExtraInfo usersExtraInfo = retrieveUserMain(userLoggedIn.getUserId());
            try {
                assert usersExtraInfo != null;
                usersExtraInfo.setLoggedIn(userLoggedIn.isLoggedIn());
            }catch (NullPointerException e){
                continue;
            }
            try {
                mongoTemplate.save(usersExtraInfo,Variables.col_usersExtraInfo);
                ModificationItem[] modificationItems;
                modificationItems = new ModificationItem[1];
                if (usersExtraInfo.isLoggedIn())
                modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdReset","FALSE"));
                else
                    modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdReset","TRUE"));

                try {
                    ldapTemplate.modifyAttributes(buildDnUser.buildDn(userLoggedIn.getUserId(),BASE_DN), modificationItems);
                }catch (Exception ignore){

                }

            }catch (Exception e){
                uniformLogger.info("System", new ReportMessage(Variables.MODEL_USER, userLoggedIn.getUserId(), Variables.ATTR_LOGGEDIN, Variables.ACTION_SET, Variables.RESULT_FAILED, "Writing to DB"));
            }
            int i =(++c*100/usersLoggedIn.size());

            System.out.print("Processing: " + i + "% " + animationChars[i % 4] + "\r");

        }
        System.out.println("Processing: Done!");

    }

    @Override
    public String getByMobile(String mobile) {
        EqualsFilter equalsFilter = new EqualsFilter("mobile", mobile);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String s = null;
        try {
            s = ldapTemplate.search("ou=People," + BASE_DN, equalsFilter.encode(), searchControls, (AttributesMapper<String>) attrs -> {
                if (attrs.get("uid") != null)
                    return attrs.get("uid").get().toString();

                return "";
            }).get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public ListUsers retrieveUsersMain(int page, int nCount, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        new Thread(() -> systemRefresh.refreshLockedUsers()).start();

        int skip = (page - 1) * nCount;

        Query query = queryBuilder(groupFilter, searchUid, searchDisplayName, userStatus);

        switch (sortType) {
            case "":
            case "uid_m2M":
                query.with(Sort.by(Sort.Direction.ASC, "userId"));
                break;
            case "uid_M2m":
                query.with(Sort.by(Sort.Direction.DESC, "userId"));
                break;
            case "displayName_m2M":
                query.with(Sort.by(Sort.Direction.ASC, "displayName"));
                break;
            case "displayName_M2m":
                query.with(Sort.by(Sort.Direction.DESC, "displayName"));
                break;
        }

        query.with(Sort.by(Sort.Direction.DESC, "_id"));

        List<UsersExtraInfo> userList = mongoTemplate.find(query.skip(skip).limit(nCount),
                UsersExtraInfo.class, Variables.col_usersExtraInfo);

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
            return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", uid).encode(), searchControls,userAttributeMapper
                    ).get(0);
        return null;
    }

    @Override
    public List<User> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));

        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls,userAttributeMapper
                );
        List<User> relatedPeople = new LinkedList<>();

        for (User user : people) {
            if (user != null && user.getDisplayName() != null) {
                relatedPeople.add(user);
            }

        }

        Collections.sort(relatedPeople);

        return relatedPeople;
    }

    public List<User> getUsersOfOu(String ou) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("ou", ou));

        List<User> users = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls,userAttributeMapper
                );

        for (User user : users)
            user.setUsersExtraInfo(mongoTemplate.findOne(new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        return users;
    }

    @Override
    public void updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou) {

        try {

            for (User user : getUsersOfOu(old_ou)) {

                DirContextOperations context = buildAttributes.buildAttributes(doerID, user.getUserId(), user, buildDnUser.buildDn(user.getUserId(),BASE_DN));

                context.removeAttributeValue("ou", old_ou);
                context.addAttributeValue("ou", new_ou);

                try {
                    ldapTemplate.modifyAttributes(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), Variables.MODEL_GROUP, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to ldap"));

                }
            }
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, doerID, Variables.MODEL_GROUP,
                    Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, old_ou, new_ou, ""));

        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, doerID, Variables.MODEL_GROUP,
                    Variables.ACTION_UPDATE, Variables.RESULT_FAILED, new_ou, "writing to ldap"));

        }
    }

    @Override
    @Cacheable(value = "currentUser", key = "userId")
    public User retrieveUsers(String userId) {
        userId = userId.toLowerCase();

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        User user = new User();
        UsersExtraInfo usersExtraInfo;
        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", userId).encode(), searchControls, userAttributeMapper);

        if (people.size() != 0) {
            user = people.get(0);
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection);
            user.setUsersExtraInfo(mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection));
            try {
                user.setUnDeletable(Objects.requireNonNull(usersExtraInfo).isUnDeletable());
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

    @Override
    public User retrieveUsersWithLicensed(String userId) {

        User user = retrieveUsers(userId);

        user.setServices(transcriptRepo.servicesOfUser(userId));

        return user;
    }

    @Override
    public UsersExtraInfo retrieveUserMain(String userId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", userId).encode(), searchControls, new SimpleUserAttributeMapper()).get(0);
    }

    private Boolean skyRoomAccess(User user) {
        boolean isEnable = skyroomEnable.equalsIgnoreCase("true");

        boolean accessRole;
        try {
            if (user.getUsersExtraInfo() == null) {
                //noinspection ConstantConditions
                Objects.requireNonNull(user.getUsersExtraInfo());
            }
            accessRole = true;
        } catch (Exception e) {
            accessRole = false;
        }

        return isEnable & accessRole;
    }

    @Override
    public List<UsersExtraInfo> retrieveGroupsUsers(String groupId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("ou", groupId).encode(), searchControls, new SimpleUserAttributeMapper());
    }

    public void removeCurrentEndTime(String uid) {

        Name dn = buildDnUser.buildDn(uid,BASE_DN);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUsers(uid);

        if (user.getExpiredTime() != null) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);

            } catch (Exception e) {
                e.printStackTrace();
            }
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

        int n = Math.min((page) * number, users.size());

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
        val add = (List<String>) gu.get("add");
        List<String> remove;
        remove = (List<String>) gu.get("remove");
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
    public int sendEmail(String email, String uid, String cid, String answer) {
        if (uid != null)
            return emailService.sendMail(email, uid, cid, answer);
        return emailService.sendMail(email, cid, answer);
    }

    public String createUrl(String userId, String token) {
        return BASE_URL + /*"" +*/  "/api/public/validateEmailToken/" + userId + "/" + token;
    }

    public HttpStatus resetPassword(String userId, String pass, String token, int pwdin) {

        User user;
        try {
            user = retrieveUsers(userId);

        } catch (NullPointerException e) {
            return HttpStatus.FORBIDDEN;
        }

        user = setRole(user);

        try {
            operations.unlock("SYSTEM", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus httpStatus = tokenClass.checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            DirContextOperations contextUser;

            contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId(),BASE_DN));
            contextUser.setAttributeValue("userPassword", pass);

            try {
                removeCurrentEndTime(userId);
                ldapTemplate.modifyAttributes(contextUser);

                uniformLogger.info(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                        Variables.ACTION_RESET, Variables.RESULT_SUCCESS, ""));

                if (passChangeNotification.equals("on"))
                    instantMessage.sendPasswordChangeNotif(user);

            }catch (org.springframework.ldap.InvalidAttributeValueException e){
                uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
                UsersExtraInfo usersExtraInfo = user.getUsersExtraInfo();
                long extraTime = Long.parseLong(usersExtraInfo.getResetPassToken())+((pwdin/2) * 60000L);
                Update update = new Update();
                update.set("resetPassToken",extraTime);
                mongoTemplate.upsert(new Query(Criteria.where("userId").is(userId)),update,Variables.col_usersExtraInfo);
                return HttpStatus.FOUND;
            }
            catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                        Variables.ACTION_RESET, Variables.RESULT_FAILED, "writing to ldap"));

            }
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
        List<String> result = null;
        InputStream insfile = file.getInputStream();

        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbookXLSX;
            workbookXLSX = new XSSFWorkbook(insfile);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbookXLSX.getSheetAt(0);

            result = excelAnalyzer.excelSheetAnalyze(doer, sheet, ou, true);

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            result = excelAnalyzer.excelSheetAnalyze(doer, xlssheet, ou, true);

        } else if (file.getOriginalFilename().endsWith(".csv")) {

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            result = excelAnalyzer.csvSheetAnalyzer(doer, csvReader, ou, true);

        }

        return result;
    }

    @Override
    public List<String> expirePassword(String doer, JSONObject jsonObject) {

        List<UsersExtraInfo> users = new LinkedList<>();

        if (((List<String>) jsonObject.get("names")).size() == 0) {
            users.addAll(mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        } else {
            final ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
            for (String temp : jsonArray)
                users.add(mongoTemplate.findOne(new Query(Criteria.where("userId").is(temp)), UsersExtraInfo.class, Variables.col_usersExtraInfo));
        }

        return expirePassword.expire(doer, users);
    }

    @Override
    public int retrieveUsersLDAPSize() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("objectClass", "person").encode(), searchControls, new SimpleUserAttributeMapper()).size();

    }

    @Override
    public Boolean SAtoSU() {
        List<UsersExtraInfo> usersExtraInfos =  mongoTemplate.find(new Query(Criteria.where("role").is("SUPERADMIN")),UsersExtraInfo.class,Variables.col_usersExtraInfo);
        for (UsersExtraInfo usersExtraInfo :usersExtraInfos){
            usersExtraInfo.setRole("SUPERUSER");
            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
        }
        uniformLogger.info("System",new ReportMessage("Convert",Variables.RESULT_SUCCESS,"SuperAdmin to SuperUser"));
        return true;
    }

    @PostConstruct
    public void postConstruct(){
        Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
        lt.start();

        new RunOneTime(ldapTemplate, mongoTemplate, uniformLogger,BASE_DN).postConstruct();

    }

    @Override
    public Boolean retrieveUsersDevice(String username) {
        return mongoTemplate.count(new Query(Criteria.where("username").is(username)), Variables.col_GoogleAuthDevice) > 0;
    }

}

