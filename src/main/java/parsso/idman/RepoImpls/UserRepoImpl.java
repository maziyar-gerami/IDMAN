package parsso.idman.RepoImpls;

import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.*;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Event;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.utils.Convertor.DateConverter;
import parsso.idman.utils.Email.EmailSend;
import parsso.idman.utils.SMS.sdk.KavenegarApi;
import parsso.idman.utils.SMS.sdk.excepctions.ApiException;
import parsso.idman.utils.SMS.sdk.excepctions.HttpException;
import org.apache.poi.ss.usermodel.DataFormatter;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserRepoImpl implements UserRepo {

    //public static final String BASE_DN = "dc=partition1,dc=com";

    Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);
    @Autowired
    FilesStorageService storageService;
    @Qualifier("groupRepoImpl")
    @Autowired
    private GroupRepo groupRepo;
    @Value("${sms.api.key}")
    private String SMS_API_KEY;
    @Value("${sms.sender.number}")
    private String SENDER;
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${email.controller}")
    private String EMAILCONTROLLER;
    @Value("${token.valid.email}")
    private int EMAIL_VALID_TIME;
    @Value("${token.valid.SMS}")
    private int SMS_VALID_TIME;
    @Value("${sms.validation.digits}")
    private int SMS_VALIDATION_DIGITS;
    @Value("${api.get.users}")
    private String apiAddress;
    @Value("${get.users.time.interval}")
    private int apiHours;
    private final int apiMiliseconds = apiHours * 60 * 60000;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
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
    private  String adminId;

    public UserRepoImpl(){

    }


    @Override
    public JSONObject create(User p) {

        User user = retrieveUser(p.getUserId());
        DirContextOperations context;

        try {
            if (user == null) {
                Name dn = buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes(p));
                //logger.info("User "+user.getUserId() + " created");
                if(p.getEndTime()!=null)
                    addEndTime(p.getUserId(),convertDateTime(p.getEndTime()));

                if (p.getCStatus()!=null) {
                    System.out.println(p.getCStatus());
                    if (p.getCStatus().equals("disable"))
                        disable(p.getUserId());
                }

                return new JSONObject();
            } else {

                return compareUsers(user, p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject createUserImport(User p) {

        User user = retrieveUser(p.getUserId());
        p.setUserPassword(defaultPassword);
        DirContextOperations context;

        try {
            if (user == null) {
                Name dn = buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes(p));
                //logger.info("User "+user.getUserId() + " created");
                return new JSONObject();
            } else {

                return compareUsers(user, p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public  void  setRole(String uid, User p){
        String role = null;
        try {
            List<String> lst = p.getMemberOf();
            for (String id:lst) {
                if (id.equals(adminId)) {
                    role = "ADMIN";
                    break;
                }
            }
        } catch (Exception e) {

            if (uid.equals(adminId))
                role = "ADMIN";

        }

        if(role == null)
            role = "USER";



        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());


        updatedAuthorities.remove(0);

        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role));


        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);

    }

    @Override
    public HttpStatus update(String uid, User p) {
        Name dn = buildDn(uid);

        setRole(uid,p);

        DirContextOperations context = buildAttributes(uid, p, dn);

        try {
            ldapTemplate.modifyAttributes(context);
            setRole(uid, p);
            if(p.getEndTime()!=null){
                try {
                    addEndTime(p.getUserId(),convertDateTime(p.getEndTime()));
                    return HttpStatus.OK;

                } catch (Exception e) {
                    e.printStackTrace();
                    return HttpStatus.FORBIDDEN;

                }
            }
            return HttpStatus.OK;

        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public String remove(String userId) {
        Name dn = buildDn(String.valueOf(userId));
        try {
            ldapTemplate.unbind(dn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId + " removed successfully";
    }

    @Override
    public String remove() {
        List<User> people = retrieveUsersFull();
        for (User user : people) {
            Name dn = buildDn(user.getUserId());

            try {
                ldapTemplate.unbind(dn);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return "All users removed successfully";
    }


    private Attributes buildAttributes(User p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");

        //ocattr.add("ads-passwordPolicy");



        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);

        //attrs.put("pwdExpireWarning", pwdExpireWarning);
        //attrs.put("pwdFailureCountInterval", pwdFaiilureCount);
        //attrs.put("pwdInHistory", pwdInHistory);
        //attrs.put("pwdCheckQuality", pwdCheckQuality);

        //attrs.put("ads-pwdId", "default");

        //attrs.put("ads-pwdAttribute", "userPassword");


        //attrs.put("pwdAttribute", "userPassword");
        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName().equals("") ? " " : p.getDisplayName());
        attrs.put("sn", p.getLastName().equals("") ? " " : p.getLastName());
        attrs.put("userPassword", p.getUserPassword() != null ? p.getUserPassword() : defaultPassword);
        attrs.put("displayName", p.getDisplayName());
        attrs.put("mobile", p.getMobile().equals("") || p.getMobile()==null ? " " : p.getMobile());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        if (p.getResetPassToken() != null)
            attrs.put("resetPassToken", p.getResetPassToken());
        if (p.getMemberOf() != null && p.getMemberOf().size() != 0) {
            Attribute attr = new BasicAttribute("ou");
            for (int i = 0; i < p.getMemberOf().size(); i++)
                attr.add(p.getMemberOf().get(i));
            attrs.put(attr);
        }
        if (p.getDescription() != null && !(p.getDescription().equals("")))
            attrs.put("description", p.getDescription());
        else
            attrs.put("description", " ");

        if (p.getPhotoName() != null)
            attrs.put("photoName", p.getPhotoName());
        else
            attrs.put("photoName", " ");



        attrs.put("qrToken", UUID.randomUUID().toString());
        if(p.isLocked())
        attrs.put("pwdAccountLockedTime", p.isEnabled());
        if (p.getEndTime()!=null)
        attrs.put("endTime", p.getEndTime());

        




        return attrs;
    }

    public String convertDateTime(String seTime){

        String year = seTime.substring(0,4);
        String month = seTime.substring(5,7);
        String day = seTime.substring(8,10);

        String hours = seTime.substring(11,13);
        String minutes = seTime.substring(14,16);
        String seconds = seTime.substring(17,19);

        String miliSeconds = seTime.substring(20,23);
        String tf1 = seTime.substring(24,25);
        String tf2 = seTime.substring(26,28);


        String date = convertDate(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day));

        String time = String.format("%02d",Integer.valueOf(hours))+
                String.format("%02d",Integer.valueOf(minutes))+
                String.format("%02d",Integer.valueOf(seconds))+"."+
                String.format("%03d",Integer.valueOf(miliSeconds))+"Z";

        return date+time;



    }

    String convertDate(int Y, int M, int D){

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Y, M,D);

        return dateConverter.getYear()+String.format("%02d",dateConverter.getMonth())+String.format("%02d",dateConverter.getDay());
    }


    @SneakyThrows
    private DirContextOperations buildAttributes(String uid, User p, Name dn) {

        User old = retrieveUser(uid);

        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != "" && p.getFirstName() != null)
            context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != "" && p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != "" && p.getDisplayName() != null)
            context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getUserPassword() != null && p.getUserPassword() != "")
            context.setAttributeValue("userPassword", p.getUserPassword());
        if (p.getMobile() != "" && p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getMail() != "" && p.getFirstName() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)
                context.setAttributeValue("cn", retrieveUser(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null)
                context.setAttributeValue("cn", p.getFirstName() + ' ' + retrieveUser(uid).getLastName());

            else context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }
        if (p.getMail() != null) context.setAttributeValue("photoName", p.getPhotoName());
        if (p.getEndTime()!=null)
            context.setAttributeValue("endTime", p.getEndTime());
        if (p.getCStatus()!=null){

            if (p.getCStatus().equals("enable"))
                enable(uid);
            else  if (p.getCStatus().equals("disable"))
                disable(uid);
            else  if (p.getCStatus().equals("unlock"))
                unlock(uid);

        }

        Attributes ls;

        if (p.getResetPassToken() != null) context.setAttributeValue("resetPassToken", p.getResetPassToken());


        if (p.getMemberOf()!=null) {
            if (p.getMemberOf().size() != 0) {
                for (int i = 0; i < p.getMemberOf().size(); i++) {
                    if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                    else context.addAttributeValue("ou", p.getMemberOf().get(i));
                }
            } else
                for (String id : old.getMemberOf()) {
                    context.removeAttributeValue("ou", id);
                }
        }

        if (p.getDescription() != "" && p.getDescription() != null)
            context.setAttributeValue("description", p.getDescription());
        if (p.getPhotoName() != "" && p.getPhotoName() != null)
            context.setAttributeValue("photoName", p.getPhotoName());
        else
            context.setAttributeValue("photoName", old.getPhotoName());

        //context.setAttributeValue("enabled", p.isEnabled());




        return context;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }




    @Override
    public HttpStatus changePassword(String uId, String currentPassword, String newPassword, String token) {

        //TODO:check current pass
        User user = retrieveUser(uId);

        if (true) {
            if (token!= null) {
                if (checkToken(uId,token) == HttpStatus.OK) {
                    user.setUserPassword(newPassword);

                    Name dn = buildDn(uId);
                    DirContextOperations context = buildAttributes(uId, user, dn);

                    try {
                        ldapTemplate.modifyAttributes(context);
                        return HttpStatus.OK;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return HttpStatus.EXPECTATION_FAILED;
                    }

                }
                else
                    return HttpStatus.FORBIDDEN;
            } else {
                user.setUserPassword(newPassword);
                Name dn = buildDn(uId);
                DirContextOperations context = buildAttributes(uId, user, dn);

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
    public HttpStatus showProfilePic(HttpServletResponse response, User user){
        File file = new File(uploadedFilesPath+user.getPhotoName());

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
                return HttpStatus.OK;
            } catch (Exception e) {
                return HttpStatus.BAD_REQUEST;

            }
        }
        return  HttpStatus.FORBIDDEN;
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
        if(update(user.getUserId(), user)==HttpStatus.OK) {
            oldPic.delete();
            return  HttpStatus.OK;

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
            if (!(user.getUserId().equals("admin")) && user.getDisplayName() != null) {
                relatedUsers.add(user);
            }
        }
        return relatedUsers;
    }

    @Override
    public User getName(String uid, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        if (checkToken(uid, token) == HttpStatus.OK)
            return ldapTemplate.search(query().attributes("givenName", "sn", "displayName").where("uid").is(uid)
                    ,
                    new UserAttributeMapper(false)).get(0);
        return null;
    }

    @Override
    public List<User> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*","+"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));

        List<User> people = ldapTemplate.search(BASE_DN, andFilter.toString() ,  searchControls,
                new UserAttributeMapper(true));
        List<User> relatedPeople = new LinkedList<>();
        for (User user : people)
            if (user.getDisplayName() != null)
                relatedPeople.add(user);

        return relatedPeople;
    }

    @Override
    public User retrieveUser(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = new User();
        if (!((ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(false))).toString() == "[]"))
            user = ldapTemplate.lookup(buildDn(userId), new String[] {"*", "+"}, new UserAttributeMapper(true));
        if (user.getUserId() == null) return null;
        else return user;
    }

    @Override
    public List<JSONObject> checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<JSONObject> jsonArray = new LinkedList<>();
        List<User> people = ldapTemplate.search(query().where("mail").is(email), new UserAttributeMapper(false));
        JSONObject jsonObject;
        for (User user : people) {
            jsonObject = new JSONObject();
            jsonObject.put("userId", user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public List<JSONObject> checkMobile(String mobile) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<User> people = ldapTemplate.search(query().where("mobile").is(mobile), new UserAttributeMapper(false));
        List<JSONObject> jsonArray = new LinkedList<>();
        JSONObject jsonObject;
        for (User user : people) {
            jsonObject = new JSONObject();
            jsonObject.put("userId", user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException {
        JSONObject jsonObject = new JSONObject();

        //________users data____________
        JSONObject userJson = new JSONObject();
        List<User> usersList = retrieveUsersFull();
        int nUsers = retrieveUsersMain().size();
        int nActive = 0;
        int nLocked = 0;
        int nDisabled = 0;

        for (User user : usersList) {
            if (user.getStatus().equals("active"))
                nActive++;
            else if (user.getStatus().equals("disabled"))
                nDisabled++;
            else if (user.getStatus().equals("locked"))
                nLocked++;
        }
        userJson.put("total", nUsers);
        userJson.put("active", nActive);
        userJson.put("disabled", nDisabled);
        userJson.put("locked", nLocked);

        //________services data____________
        JSONObject servicesJson = new JSONObject();
        ServiceRepoImpl serviceRepo = new ServiceRepoImpl();
        List<parsso.idman.Models.Service> services = serviceRepo.listServices();
        int nServices = services.size();
        int nEnabledServices = 0;

        for (parsso.idman.Models.Service service : services) {
            if (service.getAccessStrategy().isEnabled())
                nEnabledServices++;
        }

        int nDisabledServices = nServices - nEnabledServices;

        servicesJson.put("total", nServices);
        servicesJson.put("enabled", nEnabledServices);
        servicesJson.put("disabled", nDisabledServices);

        //__________________login data____________
        JSONObject loginJson = new JSONObject();
        EventRepo eventRepo = new EventRepoImpl();
        List<Event> events = eventRepo.analyze();
        int nSuccessful = 0;
        int nUnSucceful = 0;

        for (Event event : events) {
            if (event.getAction().equals("AUTHENTICATION_FAILED")) {
                nUnSucceful++;

            } else if (event.getAction().equals("AUTHENTICATION_SUCCESS"))
                nSuccessful++;
        }
        loginJson.put("total", nSuccessful+nUnSucceful);
        loginJson.put("unsuccessful", nUnSucceful);
        loginJson.put("successful", nSuccessful);

        //_________summary________________
        jsonObject.put("users", userJson);
        jsonObject.put("services", servicesJson);
        jsonObject.put("logins", loginJson);

        return jsonObject;


    }

    @Override
    public HttpStatus enable(String uid) {

        Name dn = buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);
        Boolean status = user.isEnabled();

        if(!status) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        }else {
            return HttpStatus.BAD_REQUEST;
        }

    }

    @Override
    public HttpStatus disable(String uid) {

        Name dn = buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);
        Boolean status = user.isEnabled();

        if(status) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        }else {
            return HttpStatus.BAD_REQUEST;
        }
    }



    public HttpStatus addEndTime(String uid , String date) {

        Name dn = buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);
        Boolean status = user.isEnabled();


        if(status) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime", date));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        }else {
            return HttpStatus.BAD_REQUEST;
        }
    }


    public HttpStatus expiryDate(String uid,String date) {

        Name dn = buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = retrieveUser(uid);
        Boolean status = user.isEnabled();

        if(status) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        }else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus unlock(String uid) {

        Name dn = buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[2];

        User user = retrieveUser(uid);
        Boolean locked = user.isLocked();

        if(locked) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));
            modificationItems[1] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdFailureTime"));


            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }
        }else {
            return HttpStatus.BAD_REQUEST;
        }

    }

    @Override
    public HttpStatus requestToken(User user) {
        if (insertMobileToken(user))
                return sendMessage(user.getMobile());

        else
            return HttpStatus.BAD_REQUEST;
    }

    @Override
    public HttpStatus sendEmail(String email) {
        if (checkMail(email) != null) {
            User user = retrieveUser(checkMail(email).get(0).getAsString("userId"));
            insertEmailToken(user);
            EmailSend emailSend = new EmailSend();

            String fullUrl = createUrl(BASE_URL, user.getUserId(), user.getResetPassToken().substring(0, 36));

            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
            return HttpStatus.OK;
        } else
            return HttpStatus.FORBIDDEN;
    }

    @Override
    public HttpStatus sendEmail(String email, String uid) {

        if (checkMail(email) != null & retrieveUser(uid).getUserId() != null) {
            List<JSONObject> ids = checkMail(email);
            List<User> people = new LinkedList<>();
            User user = retrieveUser(uid);
            for (JSONObject id : ids) people.add(retrieveUser(id.getAsString("userId")));

            Boolean emailSent = false;

            for (User p : people) {

                if (p.getUserId().equals(user.getUserId())) {

                    insertEmailToken(user);
                    EmailSend emailSend = new EmailSend();

                    String fullUrl = createUrl(BASE_URL, user.getUserId(), user.getResetPassToken().substring(0, 36));
                    emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
                    return HttpStatus.OK;

                }
            }

        } else
            return HttpStatus.FORBIDDEN;

        return HttpStatus.BAD_REQUEST;
    }


    private String createUrl(String BaseUrl, String userId, String token) {
        //TODO: Need to uncomment for war file
        return BaseUrl + /*"" +*/ EMAILCONTROLLER + userId + "/" + token;

    }

    @Override
    public HttpStatus checkToken(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        User user = retrieveUser(userId);

        String mainDbToken = user.getResetPassToken();
        String mainPartToken;

        if (token.length() > 30)
            mainPartToken = mainDbToken.substring(0, 36);
        else
            mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);


        if (token.equals(mainPartToken)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();

            if (mainPartToken.length() > 30) {

                String timeStamp = mainDbToken.substring(mainDbToken.indexOf(user.getUserId()) + user.getUserId().length());

                if ((cTimeStamp - Long.valueOf(timeStamp)) < (60000 * EMAIL_VALID_TIME))
                    return HttpStatus.OK;

                else
                    return HttpStatus.REQUEST_TIMEOUT;
            } else {
                String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
                if ((cTimeStamp - Long.valueOf(timeStamp)) < (60000 * SMS_VALID_TIME)) {
                    return HttpStatus.OK;
                } else
                    return HttpStatus.REQUEST_TIMEOUT;

            }

        } else
            return HttpStatus.FORBIDDEN;

    }


    private String passwordResetToken(String userId) {

        Date date = new Date();

        return UUID.randomUUID().toString().toUpperCase()
                + userId
                + date.getTime();
    }

    public String insertEmailToken(User user) {
        String token = passwordResetToken(user.getUserId());
        user.setResetPassToken(token);
        Name dn = buildDn(user.getUserId());
        Context context = buildAttributes(user.getUserId(), user, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Email Token for " + user.getUserId() + " is created";
    }

    public boolean insertMobileToken(User user) {

        Random rnd = new Random();
        int token = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1)) + rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS - 1) - 1)));
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long cTimeStamp = currentTimestamp.getTime();
        user.setResetPassToken(String.valueOf(token) + cTimeStamp);

        try {
            update(user.getUserId(), user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public HttpStatus updatePass(String userId, String pass, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(false)).get(0);

        setRole(userId, user);

        HttpStatus httpStatus = checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            user.setUserPassword(pass);
            Name dn = buildDn(user.getUserId());

            DirContextOperations context = buildAttributes(userId, user, dn);
            ldapTemplate.modifyAttributes(context);

            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }



    public JSONArray excelSheetAnalyze(Sheet sheet, int[] sequence, boolean hasHeader) {
        JSONArray jsonArray = new JSONArray();

        Iterator<Row> rowIterator = sheet.iterator();

        if (hasHeader == true) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            User user = new User();

            JSONObject temp;


            Cell cell = row.getCell(0);
            //Check the cell type and format accordingly

            if (cell == null) break;

            user.setUserId(row.getCell(sequence[0]).getStringCellValue());
            user.setFirstName(row.getCell(sequence[1]).getStringCellValue());
            user.setLastName(row.getCell(sequence[2]).getStringCellValue());
            user.setDisplayName(row.getCell(sequence[3]).getStringCellValue());
            DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
            user.setMobile(formatter.formatCellValue(row.getCell(sequence[4])));
            user.setMail(formatter.formatCellValue(row.getCell(sequence[5])));
            String tt1=formatter.formatCellValue(row.getCell(sequence[6]));
            List<String> lst = extractGroups(tt1);
            user.setMemberOf(lst);
            user.setDescription(row.getCell(sequence[7]).getStringCellValue());
            //user.isEnabled(row.getCell(sequence[8]).get);


            temp = createUserImport(user);


            if (temp != null)
                jsonArray.add(temp);


        }
        return jsonArray;
    }

    List<String> extractGroups(String strMain) {
        String[] arrSplit = (strMain.split(","));
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < arrSplit.length; i++)
            ls.add(arrSplit[i].trim());
        return ls;
    }


    public JSONObject compareUsers(User oldUser, User newUser) {

        List<String> conflicts = new LinkedList<>();

        if (oldUser.getUserId().equals(newUser.getUserId())) conflicts.add("userId");
        if (oldUser.getFirstName().equals(newUser.getFirstName())) conflicts.add("firsName");
        if (oldUser.getLastName().equals(newUser.getLastName())) conflicts.add("lastName");
        if (oldUser.getDisplayName().equals(newUser.getDisplayName())) conflicts.add("displayName");
        if (oldUser.getMail().equals(newUser.getMail())) conflicts.add("mail");
        if (oldUser.getMobile().equals(newUser.getMobile())) conflicts.add("mobile");
        if (oldUser.getDescription().equals(newUser.getDescription())) conflicts.add("description");
        if (oldUser.isEnabled() == (newUser.isEnabled())) conflicts.add("status");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("old", oldUser);
        jsonObject.put("new", newUser);
        jsonObject.put("conflicts", conflicts);


        return jsonObject;

    }

    public JSONArray csvSheetAnalyze(BufferedReader sheet, int[] sequence, boolean hasHeader) throws IOException {

        String row;
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        List<User> lsUserConflicts = new LinkedList();

        while ((row = sheet.readLine()) != null) {
            if (i == 0 && hasHeader) {
                i++;

                continue;
            }

            String[] data = row.split(",");
            // do something with the data

            User user = new User();

            user.setUserId(data[sequence[0]]);
            user.setFirstName(data[sequence[1]]);
            user.setLastName(data[sequence[2]]);
            user.setDisplayName(data[sequence[3]]);

            user.setUserPassword((data[sequence[7]]));
            user.setDescription((data[sequence[8]]));
            //user.setStatus(data[sequence[9]]);


            i++;

            jsonArray.add(create(user));

        }
        return jsonArray;

    }

    @Override
    public JSONArray importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException {

        JSONArray lsusers = new JSONArray();


        InputStream insfile = file.getInputStream();


        if (file.getOriginalFilename().endsWith(".xlsx")) {

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbookXLSX = null;
            workbookXLSX = new XSSFWorkbook(insfile);


            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbookXLSX.getSheetAt(0);

            lsusers = excelSheetAnalyze(sheet, sequence, hasHeader);

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS = null;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            lsusers = excelSheetAnalyze(xlssheet, sequence, hasHeader);

        } else if (file.getOriginalFilename().endsWith(".csv")) {


            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            lsusers = csvSheetAnalyze(csvReader, sequence, hasHeader);


            csvReader.close();

        } else if (file.getOriginalFilename().endsWith(".ldif")) {

            final LDIFReader ldifReader = new LDIFReader(insfile);

            lsusers = ldifAnalayze(ldifReader, sequence, hasHeader);
        }


        return lsusers;
    }

    private JSONArray ldifAnalayze(LDIFReader ldifReader, int[] sequence, boolean hasHeader) {
        Entry entry = null;
        while (true) {
            try {
                entry = ldifReader.readEntry();

                if (entry == null) {
                    break;
                }

                extractAttrEntry(entry);


            } catch (IOException | LDIFException ldifE) {
                //errorCount++;
                ldifE.printStackTrace();
                break;
            }
        }
        return null;
    }

    private List<User> extractAttrEntry(Entry entry) {

        List<User> lsUserConflicts = new LinkedList();

        User user = new User();

        user.setUserId(entry.getAttributeValue("uid"));
        user.setFirstName(entry.getAttributeValue("givenName"));
        user.setLastName(entry.getAttributeValue("sn"));
        user.setDisplayName(entry.getAttributeValue("displayName"));
        user.setMobile(entry.getAttributeValue("mobile"));
        user.setMail(entry.getAttributeValue("mail"));
        int nGroups = (null == entry.getAttributeValue("ou") ? 0 : entry.getAttributeValue("ou").length());
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < nGroups; i++) ls.add(entry.getAttributeValue("ou"));
        user.setMemberOf(null != entry.getAttributeValue("ou") ? ls : null);
        user.setResetPassToken(entry.getAttributeValue("resetPassToken"));
        user.setUserPassword(entry.getAttributeValue("userPassword"));
        user.setDescription(entry.getAttributeValue("description"));
        user.setPhotoName(entry.getAttributeValue("photoName"));
        //user.setStatus(entry.getAttributeValue("userStatus"));

        //lsUserConflicts.add(create(user));


        return lsUserConflicts;


    }

    @Override
    public HttpStatus sendMessage(String mobile) {
        if (checkMobile(mobile).size() > 0) {
            User user = retrieveUser(checkMobile(mobile).get(0).getAsString("userId"));
            if (insertMobileToken(user)) {
                try {
                    String receptor = mobile;
                    String message = user.getResetPassToken().substring(0,SMS_VALIDATION_DIGITS);
                    KavenegarApi api = new KavenegarApi(SMS_API_KEY);
                    api.verifyLookup(receptor, message, "", "", "mfa");



                } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("HttpException  : " + ex.getMessage());
                    return HttpStatus.BAD_REQUEST;
                } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("ApiException : " + ex.getMessage());
                    return HttpStatus.BAD_REQUEST;
                }
                return HttpStatus.OK;
            } else
                return HttpStatus.FORBIDDEN;

        } else
            return HttpStatus.FORBIDDEN;
    }

    @Override
    public HttpStatus sendMessage(String mobile, String uId) {


        if (checkMobile(mobile) != null & retrieveUser(uId).getUserId() != null) {
            List<JSONObject> ids = checkMobile(mobile);
            List<User> people = new LinkedList<>();
            User user = retrieveUser(uId);
            for (JSONObject id : ids) people.add(retrieveUser(id.getAsString("userId")));

            for (User p : people) {
                if (p.getUserId().equals(user.getUserId())) {

                    if (insertMobileToken(user)) {

                        try {
                            String receptor = mobile;
                            String message = user.getResetPassToken().substring(0,SMS_VALIDATION_DIGITS);
                            KavenegarApi api = new KavenegarApi(SMS_API_KEY);
                            api.verifyLookup(receptor, message, "", "", "mfa");
                            return HttpStatus.OK;
                        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                            System.out.print("HttpException  : " + ex.getMessage());
                            return HttpStatus.BAD_REQUEST;

                        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                            System.out.print("ApiException : " + ex.getMessage());
                            return HttpStatus.BAD_REQUEST;

                        }

                    }


                }
            }

        } else
            return HttpStatus.NOT_FOUND;

        return HttpStatus.FORBIDDEN;

    }

    private class SimpleUserAttributeMapper implements AttributesMapper<SimpleUser> {

        @Override
        public SimpleUser mapFromAttributes(Attributes attributes) throws NamingException {
            SimpleUser user = new SimpleUser();

            user.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
            int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
            List<String> ls = new LinkedList<>();
            for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());
            user.setMemberOf(ls);

            return user;
        }
    }

    private class UserAttributeMapper implements AttributesMapper<User> {
        private final boolean showToken;

        private UserAttributeMapper(boolean showToken) {
            this.showToken = showToken;
        }


        @Override
        public User mapFromAttributes(Attributes attributes) throws NamingException {
            User user = new User();

            user.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            user.setFirstName(null != attributes.get("givenName") ? attributes.get("givenName").get().toString() : null);
            user.setLastName(null != attributes.get("sn") ? attributes.get("sn").get().toString() : null);
            user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
            user.setMobile(null != attributes.get("mobile") ? attributes.get("mobile").get().toString() : null);
            user.setMail(null != attributes.get("mail") ? attributes.get("mail").get().toString() : null);
            int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
            List<String> ls = new LinkedList<>();
            for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());

            user.setResetPassToken(null != attributes.get("resetPassToken") ? attributes.get("resetPassToken").get().toString() : null);
            user.setMemberOf(null != attributes.get("ou") ? ls : null);
            user.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            user.setPhotoName(null != attributes.get("photoName") && "" != attributes.get("photoName").toString() ? attributes.get("photoName").get().toString() : null);
            user.setMobileToken(null != attributes.get("mobileToken") ? attributes.get("mobileToken").get().toString() : null);
            user.setQrToken(null != attributes.get("qrToken") ? attributes.get("qrToken").get().toString() : null);

            if (null!=attributes.get("pwdAccountLockedTime")){

                if (attributes.get("pwdAccountLockedTime").get().toString().equals("40400404040404.950Z")){
                    user.setEnabled(false);
                    user.setLocked(false);
                    user.setStatus("disabled");

                } else {
                    user.setEnabled(true);
                    user.setLocked(true);
                    user.setStatus("locked");

                }
            } else {
                user.setEnabled(true);
                user.setLocked(false);
                user.setStatus("active");

            }

            return user;
        }
    }

}
