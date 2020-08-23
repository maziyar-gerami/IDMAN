package parsso.idman.RepoImpls;

import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.SimpleUser;
import parsso.idman.utils.Email.EmailSend;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.utils.SMS.sdk.KavenegarApi;
import parsso.idman.utils.SMS.sdk.excepctions.ApiException;
import parsso.idman.utils.SMS.sdk.excepctions.HttpException;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserRepoImpl implements UserRepo {

    public static final String BASE_DN = "dc=example,dc=com";

    @Value("${sms.api.key}")
    private String  SMSAPI;

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

    private final int apiMiliseconds = apiHours*60*60000;

    @Autowired
    private LdapTemplate ldapTemplate;

    Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);

    @Override
    public JSONObject create(User p) {

        User user = retrieveUser(p.getUserId());
        DirContextOperations context;

        try {
            if (user == null) {
                Name dn = buildDn(p.getUserId());
                ldapTemplate.bind(dn, null, buildAttributes(p));
                //logger.info("User "+user.getUserId() + " created");
                return new JSONObject();
            } else {

                return compareUsers(user,p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpStatus update(String uid, User p) {
        Name dn = buildDn(uid);
        DirContextOperations context = buildAttributes(uid, p, dn);

        try {
            ldapTemplate.modifyAttributes(context);
            return HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            return HttpStatus.EXPECTATION_FAILED;
        }
    }


    @Override
    public String remove(String userId) {
        Name dn = buildDn(String.valueOf(userId));
        try {
            ldapTemplate.unbind(dn);

        } catch (Exception e){
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

            } catch (Exception e){
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
        //ocattr.add("pwdPolicy");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        //attrs.put("pwdExpireWarning","600");
        //attrs.put("pwdFailureCountInterval","5");
        //attrs.put("pwdInHistory","5");
        //attrs.put("pwdCheckQuality","2");



        //attrs.put("pwdAttribute", "userPassword");
        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName());
        attrs.put("sn", p.getLastName());
        attrs.put("userPassword", p.getUserPassword());
        attrs.put("displayName", p.getDisplayName());
        attrs.put("mobile", p.getMobile());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        if (p.getToken() != null)
            attrs.put("l", p.getToken());
        if (p.getMemberOf() != null) {
            Attribute attr = new BasicAttribute("ou");
            for (int i = 0; i < p.getMemberOf().size(); i++)
                attr.add(p.getMemberOf().get(i));
            attrs.put(attr);
        }
        attrs.put("description", p.getDescription());
        attrs.put("initials" , p.getPhotoPath());
        if (p.getStatus()==null)
            attrs.put("o", "Activated");
        else
            attrs.put("o", p.getStatus());
        attrs.put("st",UUID.randomUUID().toString());
        return attrs;
    }



    private DirContextOperations buildAttributes(String uid, User p, Name dn) {

        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != null) context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != null) context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getUserPassword() != null) context.setAttributeValue("userPassword", p.getUserPassword());
        if (p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getMail() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)  context.setAttributeValue("cn", retrieveUser(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null) context.setAttributeValue("cn", p.getFirstName() + ' ' + retrieveUser(uid).getLastName());

            else  context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }
        if (p.getMail() != null) context.setAttributeValue("initials", p.getPhotoPath());

        if (p.getToken() != null) context.setAttributeValue("l", p.getToken());

        if (p.getMemberOf() != null) {

            for (int i = 0; i < p.getMemberOf().size(); i++) {
                if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                else context.addAttributeValue("ou", p.getMemberOf().get(i));
            }
        }

        if (p.getDescription() != null) context.setAttributeValue("description", p.getDescription());
        if (p.getPhotoPath() != null) context.setAttributeValue("initials", p.getPhotoPath());
        if (p.getStatus() != null) context.setAttributeValue("o", p.getStatus());



        return context;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }


    @Override
    public HttpStatus changePassword(String uId, String currentPassword, String newPassword) {

        //TODO:check current pass
        User user = retrieveUser(uId);

        if (true) {
            user.setUserPassword(newPassword);
            return HttpStatus.OK;
        }
        else
            return HttpStatus.FORBIDDEN;

    }



    @Override
    public List<SimpleUser> retrieveUsersMain() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<SimpleUser> people = ldapTemplate.search(query().attributes("uid", "displayName", "ou").where("objectClass").is("person"),
                new SimpleUserAttributeMapper());
        return people;
    }

    @Override
    public User getName(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = ldapTemplate.search(query().attributes("givenName", "sn", "displayName").where("uid").is(uid)
                ,
                new UserAttributeMapper(false)).get(0);
        return user;
    }

    @Override
    public List<User> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<User> people = ldapTemplate.search(query().where("objectClass").is("person"),
                new UserAttributeMapper(false));
        return people;
    }

    @Override
    public User retrieveUser(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = new User();
        if (!((ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(false))).toString() == "[]"))
            user = ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(true)).get(0);
        if (user.getUserId()==null) return null;
        else  return user;
    }

    @Override
    public List<JSONObject> checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<JSONObject> jsonArray = new LinkedList<>();
        List<User> people = ldapTemplate.search(query().where("mail").is(email), new UserAttributeMapper(false));
        JSONObject jsonObject;
        for (User user:people ){
            jsonObject = new JSONObject();
            jsonObject.put("userId",user.getUserId());
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
        for (User user:people ) {
            jsonObject = new JSONObject();
            jsonObject.put("userId",user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public String sendEmail(String email) {
        if (checkMail(email) != null) {
            User user = retrieveUser(checkMail(email).get(0).getAsString("userId"));
            insertEmailToken(user);
            EmailSend emailSend = new EmailSend();

            String fullUrl = createUrl(BASE_URL, user.getUserId(), user.getToken().substring(0,36));

            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
            return "Email sent";
        } else
            return "Email Not found";
    }

    @Override
    public String sendEmail(String email, String uid) {

        if (checkMail(email) != null & retrieveUser(uid).getUserId() != null) {
            List<JSONObject> ids = checkMail(email);
            List<User> people = new LinkedList<>();
            User user = retrieveUser(uid);
            for (JSONObject id:ids ) people.add(retrieveUser(id.getAsString("userId")));

            Boolean emailSent = false;

            for (User p : people) {

                if (p.getUserId().equals(user.getUserId())) {

                    insertEmailToken(user);
                    EmailSend emailSend = new EmailSend();

                    String fullUrl = createUrl(BASE_URL, user.getUserId(), user.getToken());
                    emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
                    return "Email sent";

                }
            }

        } else
            return "Email Not found or userId not found";

        return "Not a such user";
    }


    private String createUrl(String BaseUrl, String userId, String token) {
        //TODO: Need to uncomment for war file
        return BaseUrl + /*"" +*/ EMAILCONTROLLER + userId + "/" + token.toString();

    }

    @Override
    public HttpStatus checkToken(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        User user = ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(true)).get(0);

        String mainDbToken = user.getToken();
        String mainPartToken;

        if (token.length()>30)
            mainPartToken = mainDbToken.substring(0, 36);
        else
            mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);


        if (token.equals(mainPartToken)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();

            if (mainPartToken.length()>30){

                String timeStamp = mainDbToken.substring(mainDbToken.indexOf(user.getUserId())+user.getUserId().length());

                if ((cTimeStamp - Long.valueOf(timeStamp))<(60000*EMAIL_VALID_TIME))
                    return HttpStatus.OK;

                else
                    return HttpStatus.REQUEST_TIMEOUT;
            }
            else {
                String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
                if ((cTimeStamp - Long.valueOf(timeStamp))<(60000*SMS_VALID_TIME)) {
                    return HttpStatus.OK;
                }
                else
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
        user.setToken(token);
        Name dn = buildDn(user.getUserId());
        Context context = buildAttributes(user.getUserId(), user, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Email Token for " + user.getUserId() + " is created";
    }

    public String insertMobileToken(User user) {


        Random rnd = new Random();
        int token = (int) (Math.pow(10,(SMS_VALIDATION_DIGITS-1))+rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS-1)-1)));
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long cTimeStamp = currentTimestamp.getTime();
        user.setToken(String.valueOf(token)+cTimeStamp);
        Name dn = buildDn(user.getUserId());
        Context context = buildAttributes(user.getUserId(), user, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Mobile Token for " + user.getUserId() + " is created";


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
        private boolean showToken;

        private UserAttributeMapper(boolean showToken){
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
            if(showToken)
                user.setToken(null != attributes.get("l") ? attributes.get("l").get().toString() : null);
            user.setMemberOf(null != attributes.get("ou") ? ls : null);
            user.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            user.setPhotoPath(null != attributes.get("initials") ? attributes.get("initials").get().toString() : null);
            user.setStatus(null != attributes.get("o") ? attributes.get("o").get().toString() : "Activated");
            return user;
        }
    }


    public String updatePass(String userId, String pass, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        User user = ldapTemplate.search(query().where("uid").is(userId), new UserAttributeMapper(false)).get(0);

        if (token.equals(user.getToken())) {
            user.setUserPassword(pass);
            Name dn = buildDn(user.getUserId());

            DirContextOperations context = buildAttributes(userId, user, dn);
            ldapTemplate.modifyAttributes(context);

            return userId + " passwords was updated";
        } else {
            return "userId or token was incorrect";
        }
    }

    public JSONArray excelSheetAnalyze(Sheet sheet, int[] sequence, boolean hasHeader){
        JSONArray jsonArray = new JSONArray();

        Iterator<Row> rowIterator = sheet.iterator();

        if (hasHeader == true) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            User user = new User();

            JSONObject temp;


            Cell cell = row.getCell(0);
            //Check the cell type and format accordingly

            if (cell==null) break;

            user.setUserId(row.getCell(sequence[0]).getStringCellValue());
            user.setFirstName(row.getCell(sequence[1]).getStringCellValue());
            user.setLastName(row.getCell(sequence[2]).getStringCellValue());
            user.setDisplayName(row.getCell(sequence[3]).getStringCellValue());
            user.setMobile(row.getCell(sequence[4]).getStringCellValue());
            user.setMail(row.getCell(sequence[5]).getStringCellValue());
            user.setMemberOf(Collections.singletonList(row.getCell(sequence[6]).getStringCellValue()));
            user.setUserPassword(row.getCell(sequence[7]).getStringCellValue());
            user.setDescription(row.getCell(sequence[8]).getStringCellValue());
            user.setPhotoPath(row.getCell(sequence[9]).getStringCellValue());


            temp = create(user);


            if (!temp.equals(null))
                jsonArray.add(temp);


        }
        return jsonArray;
    }

    public JSONObject compareUsers(User oldUser , User newUser){

        List<String> conflicts = new LinkedList<>();

        if (oldUser.getUserId().equals(newUser.getUserId())) conflicts.add("userId");
        if (oldUser.getFirstName().equals(newUser.getFirstName())) conflicts.add("firsName");
        if (oldUser.getLastName().equals(newUser.getLastName())) conflicts.add("lastName");
        if (oldUser.getDisplayName().equals(newUser.getDisplayName())) conflicts.add("displayName");
        if (oldUser.getMail().equals(newUser.getMail())) conflicts.add("mail");
        if (oldUser.getMobile().equals(newUser.getMobile())) conflicts.add("mobile");
        if (oldUser.getDescription().equals(newUser.getDescription())) conflicts.add("description");
        if (oldUser.getStatus().equals(newUser.getStatus())) conflicts.add("status");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("old", oldUser);
        jsonObject.put("new", newUser);
        jsonObject.put("conflicts", conflicts);


        return jsonObject;

    }

    public JSONArray csvSheetAnalyze(BufferedReader sheet, int[] sequence, boolean hasHeader) throws IOException {

        String row;
        JSONArray jsonArray = new JSONArray();
        int i=0;
        List<User> lsUserConflicts = new LinkedList();

        while ((row = sheet.readLine()) != null) {
            if (i==0  && hasHeader) {
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
            user.setMobile(data[sequence[4]]);
            user.setMail(data[sequence[5]]);
            user.setMemberOf(Collections.singletonList(data[sequence[6]]));
            user.setUserPassword((data[sequence[7]]));
            user.setDescription((data[sequence[8]]));
            user.setStatus(data[sequence[9]]);


            i++;

            jsonArray.add(create(user));

        }
        return jsonArray;

    }

    @Override
    public JSONArray importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) {

        JSONArray lsusers = new JSONArray();

        try {
            InputStream insfile = file.getInputStream();
            String format = file.getContentType();

            switch (format){

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":

                    //Create Workbook instance holding reference to .xlsx file
                    XSSFWorkbook workbookXLSX = new XSSFWorkbook(insfile);

                    //Get first/desired sheet from the workbook
                    XSSFSheet sheet = workbookXLSX.getSheetAt(0);

                    lsusers = excelSheetAnalyze(sheet , sequence, hasHeader);

                    break;

                case "application/vnd.ms-excel":
                    HSSFWorkbook workbookXLS = new HSSFWorkbook(insfile);

                    HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

                    lsusers = excelSheetAnalyze(xlssheet ,sequence, hasHeader);

                case "text/csv":

                    BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

                    lsusers = csvSheetAnalyze(csvReader,sequence,hasHeader);

                    csvReader.close();

                    break;

                case "application/octet-stream":

                    final LDIFReader ldifReader = new LDIFReader(insfile);

                    lsusers = ldifAnalayze(ldifReader, sequence,hasHeader);
            }


        } catch (Exception e) {
            e.printStackTrace();
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

    private List<User> extractAttrEntry(Entry entry){

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
            user.setToken(entry.getAttributeValue("l"));
            user.setMemberOf(null != entry.getAttributeValue("ou") ? ls : null);
            user.setUserPassword(entry.getAttributeValue("userPassword"));
            user.setDescription(entry.getAttributeValue("description"));
            user.setPhotoPath(entry.getAttributeValue("initials"));
            user.setStatus(entry.getAttributeValue("o"));

            //lsUserConflicts.add(create(user));



        return lsUserConflicts;


    }



    @Override
    public String sendMessage(String mobile) {
        String s = checkMobile(mobile).get(0).getAsString("userId");
        User user = retrieveUser(checkMobile(mobile).get(0).getAsString("userId"));
        insertMobileToken(user);
        try {
            String receptor = mobile;
            String message = "کد اعتبارسنجی شما در پارسو Idman:\n"+user.getToken().substring(0,SMS_VALIDATION_DIGITS);
            KavenegarApi api = new KavenegarApi(SMSAPI);
            api.send(SENDER, receptor, message);
        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
            System.out.print("HttpException  : " + ex.getMessage());
        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
            System.out.print("ApiException : " + ex.getMessage());
        }
        return "SMS Sent!";
    }


    @Override
    public String sendMessage(String mobile, String uId) {


        if (checkMobile(mobile) != null & retrieveUser(uId).getUserId() != null) {
            List<JSONObject> ids = checkMobile(mobile);
            List<User> people = new LinkedList<>();
            User user = retrieveUser(uId);
            for (JSONObject id : ids) people.add(retrieveUser(id.getAsString("userId")));

            for (User p : people) {
                if (p.getUserId().equals(user.getUserId())) {

                    insertMobileToken(user);

                    try {
                        String receptor = mobile;
                        String message = "کد اعتبارسنجی شما در پارسو Idman:\n"+user.getToken().substring(0,SMS_VALIDATION_DIGITS);
                        KavenegarApi api = new KavenegarApi(SMSAPI);
                        api.send(SENDER, receptor, message);
                    } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                        System.out.print("HttpException  : " + ex.getMessage());
                    } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                        System.out.print("ApiException : " + ex.getMessage());
                    }


                    return "SMS sent";

                }
            }

        } else
            return "Email Not found or userId not found";

        return "Not a such user";

    }


}
