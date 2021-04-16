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
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Communicate.Email;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.User.*;
import parsso.idman.Models.DashboardData.Dashboard;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.SimpleUser;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserRepoImpl implements UserRepo {

    @Autowired
    FilesStorageService storageService;
    String userExtraInfoCollection = "IDMAN_UsersExtraInfo";
    String simpleCollection = "IDMAN_SimpleUsers";
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${email.controller}")
    private String EMAILCONTROLLER;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${get.users.time.interval}")
    private int apiHours;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Value("${default.user.password}")
    private String defaultPassword;
    @Value("${profile.photo.path}")
    private String uploadedFilesPath;
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
    private Email emailClass;
    @Autowired
    private BuildDnUser buildDnUser;
    @Autowired
    private DashboardData dashboardData;
    @Autowired
    private ImportUsers importUsers;
    @Autowired
    ExcelAnalyzer excelAnalyzer;
    String model = "User";

    @Autowired
    GroupRepo groupRepo;


    @Override
    public JSONObject create(String doerID,User p) {

        Logger logger = LogManager.getLogger(doerID);


        try {
            User user = retrieveUsers(p.getUserId());
            if (user == null || user.getUserId() == null) {
                if( checkGroup(p.getMemberOf())) {

                    Query query = new Query(Criteria.where("userId").is(p.getUserId()));
                    //create user in ldap
                    Name dn = buildDnUser.buildDn(p.getUserId());
                    ldapTemplate.bind(dn, null, buildAttributes.BuildAttributes(p));
                    mongoTemplate.save(new SimpleUser(p), simpleCollection);

                    //update it's first pwChangedTime in a new thread
                    Thread thread = new Thread() {
                        public void run() {
                            User userTemp = retrieveUsers(p.getUserId());
                            DirContextOperations context = ldapTemplate.lookupContext(dn);

                            context.setAttributeValue("pwdChangedTime", userTemp.getTimeStamp() + "Z");

                            try {
                                ldapTemplate.modifyAttributes(context);
                            } catch (Exception e) {

                                logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "failed", "due to writing to ldap").toString());
                            }

                        }
                    };

                    thread.start();

                    new Thread(() -> {
                        //logger.warn("User "+user.getUserId() + " created");
                        UsersExtraInfo usersExtraInfo = new UsersExtraInfo(p.getUserId(), p.getPhoto(), p.isUnDeletable());
                        mongoTemplate.save(usersExtraInfo, Token.collection);
                    }).start();

                    if (p.getCStatus() != null)
                        if (p.getCStatus().equals("disable"))
                            disable(doerID, p.getUserId());

                    logger.warn(new ReportMessage(model, p.getUserId(), "", "create", "success", "").toString());
                    return new JSONObject();
                } else {
                    logger.warn(new ReportMessage(model,p.getUserId(),"","create", "failed","group not exist").toString());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", p.getUserId() );
                    jsonObject.put("invalidGroups", invalidGroups(p.getMemberOf()));
                    return jsonObject;
                }
            } else {
                logger.warn(new ReportMessage(model,p.getUserId(),"","create", "failed","already exist").toString());
                return importUsers.compareUsers(user, p);
            }
        } catch (Exception e) {
            if(p.getUserId()!=null || !p.getUserId().equals(""))

                logger.warn(new ReportMessage(model,p.getUserId(),"","create", "failed","unknown reason").toString());
            return null;
        }
    }

    private  boolean checkGroup(List<String> groups){
        List<Group> realGroups = groupRepo.retrieve();
        List<String> realStrings = new LinkedList<>();
        for (Group group:realGroups) {
           realStrings.add(group.getId());
        }

        for (String group:groups) {
            if(!realStrings.contains(group))
                return false;

        }
        return true;
    }

    private List<String> invalidGroups (List<String> groups){
        List<Group> realGroups = groupRepo.retrieve();
        List<String> realStrings = new LinkedList<>();
        List<String> invalids = new LinkedList<>();
        for (Group group:realGroups) {
            realStrings.add(group.getId());
        }

        for (String group:groups) {
            if(!realStrings.contains(group))
                invalids.add(group);

        }
        return invalids;
    }

    @Override
    public int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        return (int) mongoTemplate.count(queryBuilder(groupFilter,searchUid,searchDisplayName,userStatus), simpleCollection);
    }

    @Override
    public HttpStatus update(String doerID,String usid, User p) {

        Logger logger = LogManager.getLogger(doerID);

        p.setUserId(usid);
        Name dn = buildDnUser.buildDn(p.getUserId());

        User user = retrieveUsers(p.getUserId());

        DirContextOperations context;

        //remove current pwdEndTime
        if ((p.getEndTime() != null && p.getEndTime().equals("")))
            removeCurrentEndTime(p.getUserId());
        else if (p.getEndTime() != null &&
                p.getEndTime().equals("")
                && user.getEndTime()!=null)
            removeCurrentEndTime(p.getUserId());

        context = buildAttributes.buildAttributes(doerID, usid, p, dn);
        Query query = new Query(Criteria.where("userId").is(p.getUserId()));
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, userExtraInfoCollection);
        usersExtraInfo.setUnDeletable(p.isUnDeletable());
        SimpleUser simpleUser = mongoTemplate.findOne(query,SimpleUser.class, simpleCollection);
        simpleUser.setStatus(p.getCStatus());
        simpleUser.setMemberOf(p.getMemberOf());


        if (p.getPhoto() != null)
            usersExtraInfo.setPhotoName(p.getPhoto());

        if (p.isUnDeletable())
            usersExtraInfo.setUnDeletable(true);


        try {

            ldapTemplate.modifyAttributes(context);

            mongoTemplate.remove(query, userExtraInfoCollection);
            mongoTemplate.save(usersExtraInfo, userExtraInfoCollection);

            mongoTemplate.remove(query, simpleCollection);
            mongoTemplate.save(simpleUser, simpleCollection);

            if(!user.getStatus().equals(p.getStatus()))
                logger.warn(new ReportMessage("User",usid,"Status","change", "success","from "+user.getStatus()+ " to "+p.getStatus()).toString());

            logger.warn(new ReportMessage(model,usid,"","update", "success","").toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(new ReportMessage(model,usid,"","update", "failed","Writing to DB").toString());

        }

        if(p.getUserPassword()!=null) {

            context.setAttributeValue("userPassword", p.getUserPassword());

            try {
                ldapTemplate.modifyAttributes(context);
                logger.warn(new ReportMessage(model,usid,"password","update", "success","").toString());

                return HttpStatus.OK;
            } catch (Exception e) {

                logger.warn(new ReportMessage(model,usid,"password","update", "failed","unknown error").toString());

                return HttpStatus.BAD_REQUEST;
            }

        }

        return  HttpStatus.OK;
    }


    @Override
    public JSONObject createUserImport(String doerID, User p) {


        if (p.getUserPassword() == null)
            p.setUserPassword(defaultPassword);


        return create(doerID,p);



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
                    mongoTemplate.remove(query, SimpleUser.class, simpleCollection);
                    logger.warn(new ReportMessage(model,user.getUserId(),"","remove", "success","").toString());

                } catch (Exception e) {
                    logger.warn(new ReportMessage(model,user.getUserId(),"","remove", "failed","unknown reason").toString());

                }

            }

        return undeletables;
    }

    public User setRole(User p) {
        String role;

        try {
            role = p.getUsersExtraInfo().getRole();

        }catch (Exception e) {
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
    public HttpStatus changePassword(String uId,String oldPassword, String newPassword, String token) {

        Logger logger = LogManager.getLogger(uId);

        //TODO:check current pass
        User user = retrieveUsers(uId);

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", uId));


        if (ldapTemplate.authenticate(BASE_DN,andFilter.toString(),oldPassword)) {
            if (token != null) {
                if (tokenClass.checkToken(uId, token) == HttpStatus.OK) {

                    DirContextOperations contextUser;

                    contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
                    contextUser.setAttributeValue("userPassword", newPassword);

                    try {
                        ldapTemplate.modifyAttributes(contextUser);
                        logger.warn(new ReportMessage(model,uId,"password","change", "success","").toString());
                        return HttpStatus.OK;
                    } catch (Exception e) {
                        logger.warn(new ReportMessage(model,uId,"password","change", "failed","writing to LDAP").toString());
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

        //TODO:Should consider
        user.setPhoto(s);
        logger.warn(new ReportMessage(model,name,"profile image","change", "success","").toString());
        if (update(user.getUserId(),user.getUserId(), user) == HttpStatus.OK) {
            oldPic.delete();

            return HttpStatus.OK;

        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public List<SimpleUser> retrieveUsersMain(int page, int number) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        int limit = number;
        int skip =(page-1)*limit;

        List<UsersExtraInfo> usersExtraInfos=null;

        if(page==-1 && number==-1)
            usersExtraInfos = mongoTemplate.find(new Query(),UsersExtraInfo.class, userExtraInfoCollection);
        else
            usersExtraInfos = mongoTemplate.find(new Query().skip(skip).limit(limit),UsersExtraInfo.class, userExtraInfoCollection);

        ContainerCriteria query = query().attributes("uid", "displayName", "ou", "createtimestamp", "pwdAccountLockedTime").where("uid").is(usersExtraInfos.get(0).getUserId());

        for (int i=1 ; i<usersExtraInfos.size();i++)
            query.or("uid").is(usersExtraInfos.get(i).getUserId());

        return ldapTemplate.search(query, new SimpleUserAttributeMapper());

    }

    @Override
    public ListUsers retrieveUsersMain(int page, int nCount, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        int limit = nCount;
        int skip =(page-1)*limit;

        Query query = queryBuilder(groupFilter, searchUid, searchDisplayName, userStatus);



        if (sortType.equals(""))
            query.with(Sort.by(Sort.Direction.ASC,"userId"));


        else if (sortType.equals("uid_m2M"))
            query.with(Sort.by(Sort.Direction.ASC,"userId"));

        else if (sortType.equals("uid_M2m"))
            query.with(Sort.by(Sort.Direction.DESC,"userId"));

        else if (sortType.equals("displayName_m2M"))
            query.with(Sort.by(Sort.Direction.ASC,"displayName"));

        else if (sortType.equals("displayName_M2m"))
            query.with(Sort.by(Sort.Direction.DESC,"displayName"));


        List<SimpleUser> userList = mongoTemplate.find(query.skip(skip).limit(limit),
                SimpleUser.class, simpleCollection);

        int size = retrieveUsersSize(groupFilter, searchUid, searchDisplayName, userStatus);

        return new ListUsers(size , userList, (int) Math.ceil(size/nCount));
    }

    private Query queryBuilder(String groupFilter, String searchUid, String searchDisplayName, String userStatus){
        Query query = new Query();
        if(!searchUid.equals(""))
            query.addCriteria(Criteria.where("userId").regex(searchUid));
        if(!searchDisplayName.equals(""))
            query.addCriteria(Criteria.where("displayName").regex(searchDisplayName));
        if(!userStatus.equals(""))
            query.addCriteria(Criteria.where("status").is(userStatus));
        if(!groupFilter.equals(""))
            query.addCriteria(Criteria.where("memberOf").all(groupFilter));
        return query;

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
            if (user.getDisplayName() != null && !user.getUsersExtraInfo().getRole().equals("SUPERADMIN")) {
                relatedPeople.add(user);
            }

        }

        Collections.sort(relatedPeople);

        return relatedPeople;
    }

    @Override
    public HttpStatus updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou) {
        Logger logger = LogManager.getLogger(doerID);

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("ou", old_ou));

        List<User> people = ldapTemplate.search(BASE_DN, andFilter.toString(), searchControls,
                userAttributeMapper);

        try {

            for (User user : people) {

                DirContextOperations context = buildAttributes.buildAttributes(doerID,user.getUserId(), user, buildDnUser.buildDn(user.getUserId()));

                context.removeAttributeValue("ou", old_ou);
                context.addAttributeValue("ou", new_ou);

                try {
                    ldapTemplate.modifyAttributes(context);
                }catch (Exception e){
                    logger.warn(new ReportMessage(model,user.getUserId(),"group","update", "failed","writing to ldap").toString());

                }
            }
            logger.warn(new ReportMessage(model,doerID,"groups","update", "success","").toString());

            return HttpStatus.OK;
        } catch (Exception e) {
            logger.warn(new ReportMessage(model,doerID,"groups","update", "failed","writing to ldap").toString());

            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public User retrieveUsers(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = new User();
        UsersExtraInfo usersExtraInfo = null;
        if (!((ldapTemplate.search(query().where("uid").is(userId), userAttributeMapper)).toString() == "[]")) {
            user = ldapTemplate.lookup(buildDnUser.buildDn(userId), new String[]{"*", "+"}, userAttributeMapper);
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection);
            user.setUsersExtraInfo(mongoTemplate.findOne(query, UsersExtraInfo.class, Token.collection));
            user.setUnDeletable(usersExtraInfo.isUnDeletable());
        }

        if(user.getRole()==null)
            return setRole(user);
        if (user.getUserId() == null) return null;
        else return user;
    }

    @Override
    public List<SimpleUser> retrieveGroupsUsers(String groupId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);


        return ldapTemplate.search(query().where("ou").is(groupId), simpleUserAttributeMapper);
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
    public Dashboard retrieveDashboardData() throws InterruptedException {



        return dashboardData.retrieveDashboardData();
    }

    @Override
    public HttpStatus enable(String doer,String uid) {

        Logger logger = LogManager.getLogger(doer);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUsers(uid);
        String status = user.getStatus();

        if (status.equalsIgnoreCase("disable")) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.warn(new ReportMessage(model,user.getUserId(),"","enable", "success","").toString());

                return HttpStatus.OK;

            } catch (Exception e) {
                logger.warn(new ReportMessage(model,user.getUserId(),"","enable", "failed","writing to ldap").toString());
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
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
    public HttpStatus disable(String doerID, String uid) {
        Logger logger = LogManager.getLogger(doerID);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUsers(uid);

        if (user.isEnabled()) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.warn(new ReportMessage(model,user.getUserId(),"","disable", "success","").toString());
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn(new ReportMessage(model,user.getUserId(),"","disable", "failed","writing to ldap").toString());
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus unlock(String doerID,String uid) {

        Logger logger = LogManager.getLogger(doerID);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUsers(uid);
        String locked = user.getStatus();

        if (locked.equalsIgnoreCase("lock")) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);

                try {
                    modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdFailureTime"));
                    ldapTemplate.modifyAttributes(dn, modificationItems);


                }catch (Exception e){

                }
                logger.warn(new ReportMessage(model,user.getUserId(),"","unlock", "success","").toString());
                return HttpStatus.OK;

            } catch (Exception e) {

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
        int nSuccessful=0;
        for (User user : users) {
            if (user != null && user.getUserId() != null)
                try {
                    update(doerID, user.getUserId(), user);
                    nSuccessful++;
                } catch (Exception e) {
                }
        }
        JSONObject jsonObject = new JSONObject();
                jsonObject.put("nCount", nCount);
                jsonObject.put("nSuccessful", nSuccessful);
                jsonObject.put("nUnSuccessful",nCount-nSuccessful);

                return jsonObject;
    }

    @Override
    public ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int number) {

        List<SimpleUser> users = retrieveGroupsUsers(groupId);

        CollectionUtils.filter(users, PredicateUtils.notNullPredicate());

        int n = (page) * number>users.size()?users.size():(page) * number;

        int size = users.size();
        int pages = (int) Math.ceil(size / number);
        int start = (page - 1) * number;

        List<SimpleUser> relativeUsers = new LinkedList<>();

        for (int i = start; i < n; i++)
            relativeUsers.add(users.get(i));

        CollectionUtils.filter(relativeUsers, PredicateUtils.notNullPredicate());

        return new ListUsers(size, relativeUsers, pages);

    }

    @Override
    public HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) {
        List<String> add = (List<String>) gu.get("add");
        List<String> remove = (List<String>) gu.get("remove");
        for (String uid : add) {
            User user = retrieveUsers(uid);
            if (user.getMemberOf()!=null&&!user.getMemberOf().contains(groupId)) {
                user.getMemberOf().add(groupId);
                update(doerID, uid, user);
            }

        }
        for (String uid : remove) {
            User user = retrieveUsers(uid);
            if (user.getMemberOf().contains(groupId)) {
                user.getMemberOf().remove(groupId);
                update(doerID,uid, user);
            }
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus syncUsersDBs() {
        List<SimpleUser> simpleUsers = retrieveUsersMain(-1,-1);

        if (!mongoTemplate.collectionExists(simpleCollection))
            mongoTemplate.createCollection(simpleCollection);

        for (SimpleUser simpleUser:simpleUsers)
            mongoTemplate.save(simpleUser, simpleCollection);

        return HttpStatus.OK;
    }

    @Override
    public int sendEmail(String email, String uid, String cid, String answer) {
        if (uid!=null)
            return emailClass.sendEmail(email, uid, cid, answer);
        return emailClass.sendEmail(email, cid, answer);
    }

    public String createUrl(String userId, String token) {
        return BASE_URL + /*"" +*/ EMAILCONTROLLER + userId + "/" + token;
    }

    public HttpStatus updatePass(String userId, String pass, String token) {

        //Logger logger = LogManager.getLogger(doerID);

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = retrieveUsers(userId);

        user=setRole(user);

        HttpStatus httpStatus = tokenClass.checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            DirContextOperations contextUser;

            contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
            contextUser.setAttributeValue("userPassword", pass);
            ldapTemplate.modifyAttributes(contextUser);

            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public JSONObject importFileUsers(String doerId, MultipartFile file, int[] sequence, boolean hasHeader) throws IOException {
        return importUsers.importFileUsers(doerId, file, sequence, hasHeader);
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

            return excelAnalyzer.excelSheetAnalyze(doer, sheet, ou,true);

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS = null;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            return excelAnalyzer.excelSheetAnalyze(doer,xlssheet, ou,true);

        } else if (file.getOriginalFilename().endsWith(".csv")) {

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            return excelAnalyzer.csvSheetAnalyzer(doer,csvReader, ou, true);
        }

        return null;
    }

}

