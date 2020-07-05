package parsso.idman.RepoImpls;

import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.utils.Email.EmailSend;
import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepoImpl implements PersonRepo {

    public static final String BASE_DN = "";

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${email.controller}")
    private String EMAILCONTROLLER;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public String create(Person p) {
        System.out.println(p);

        Person person = retrievePerson(p.getUserId());
        DirContextOperations context;

        if (person.getUserId() == null) {
            Name dn = buildDn(p.getUserId());

            ldapTemplate.bind(dn, null, buildAttributes(p));
            return p.getUserId() + " created successfully";
        } else {
            update(p.getUserId(), p);
            return p.getUserId() + " was found and existing record updated";
        }
    }

    @Override
    public String update(String uid, Person p) {
        Name dn = buildDn(uid);
        DirContextOperations context = buildAttributes(uid, p, dn);
        ldapTemplate.modifyAttributes(context);
        return uid + " updated successfully";
    }


    @Override
    public String remove(String userId) {
        Name dn = buildDn(String.valueOf(userId));
        ldapTemplate.unbind(dn);
        return userId + " removed successfully";
    }

    @Override
    public String remove() {
        List<Person> people = retrieveUsersMain();
        for (Person person : people) {
            Name dn = buildDn(person.getUserId());
            ldapTemplate.unbind(dn);
        }

        return "All users removed successfully";
    }

    private Attributes buildAttributes(Person p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName());
        attrs.put("sn", p.getLastName());
        attrs.put("displayName", p.getDisplayName());
        attrs.put("mobile", p.getMobile());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        System.out.println("**************");
        if (p.getToken() != null) {
            attrs.put("l", p.getToken());
            System.out.println(p.getToken());

        }
        if (p.getMemberOf() != null) {
            Attribute attr = new BasicAttribute("ou");
            for (int i = 0; i < p.getMemberOf().size(); i++)
                attr.add(p.getMemberOf().get(i));
            attrs.put(attr);
        }

        attrs.put("description", p.getDescription());
        attrs.put("photo", p.getPhoto());
        return attrs;
    }


    private DirContextOperations buildAttributes(String uid, Person p, Name dn) {

        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != null) context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != null) context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getMail() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)
                context.setAttributeValue("cn", retrievePerson(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null)
                context.setAttributeValue("cn", p.getFirstName() + ' ' + retrievePerson(uid).getLastName());

            else
                context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }


        if (p.getToken() != null) context.setAttributeValue("l", p.getToken());

        if (p.getMemberOf() != null) {

            for (int i = 0; i < p.getMemberOf().size(); i++) {
                if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                else context.addAttributeValue("ou", p.getMemberOf().get(i));
            }
        }

        if (p.getDescription() != null) context.setAttributeValue("description", p.getDescription());
        if (p.getPhoto() != null) context.setAttributeValue("photo", p.getPhoto());
        return context;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }

    public Name buildGroupDn(String groupID) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", groupID).build();
    }

    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "people").build();
    }


    @Override
    public String changePassword(String uId, String currentPassword, String newPassword) {

        //TODO:check current pass
        Person person = retrievePerson(uId);

        if (true) {
            person.setUserPassword(newPassword);
            return "Password Changed";
        }
        else
            return "Passwords don't match";

    }

    @Override
    public List<Person> retrieveUsersMain() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Person> people = ldapTemplate.search(query().attributes("uid", "displayName", "ou").where("objectClass").is("person"),
                new PersonAttributeMapper());
        return people;
    }

    @Override
    public Person getName(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = ldapTemplate.search(query().attributes("givenName", "sn", "displayName").where("uid").is(uid)
        ,
                new PersonAttributeMapper()).get(0);
        return person;
    }

    @Override
    public List<Person> retrieveUsersFull() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Person> people = ldapTemplate.search(query().where("objectClass").is("person"),
                new PersonAttributeMapper());
        return people;
    }

    @Override
    public Person retrievePerson(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();
        if (!((ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper())).toString() == "[]"))
            person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);
        //System.out.println(byte(person.getPhoto().toString()));
        return person;
    }

    @Override
    public List<Person> checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();
        List<Person> people = ldapTemplate.search(query().where("mail").is(email), new PersonAttributeMapper());
        return people;
    }

    @Override
    public String sendEmail(String email) {
        if (checkMail(email) != null) {
            Person person = checkMail(email).get(0);
            String token = insertToken(person);
            EmailSend emailSend = new EmailSend();


            String fullUrl = createUrl(BASE_URL, person.getUserId(), person.getToken());

            emailSend.sendMail(email, person.getUserId(), person.getDisplayName(), "\n" + fullUrl);
            return "Email sent";
        } else
            return "Email Not found";
    }

    @Override
    public String sendEmail(String email, String uid) {

        if (checkMail(email) != null & retrievePerson(uid).getUserId() != null) {
            List<Person> people = checkMail(email);
            Person person = retrievePerson(uid);
            Boolean emailSent = false;

            System.out.println(person.getUserId());

            for (Person p : people) {

                System.out.println(p.getUserId());

                if (p.getUserId().equals(person.getUserId())) {

                    String token = insertToken(person);
                    EmailSend emailSend = new EmailSend();

                    String fullUrl = createUrl(BASE_URL, person.getUserId(), person.getToken());
                    emailSend.sendMail(email, person.getUserId(), person.getDisplayName(), "\n" + fullUrl);
                    return "Email sent";

                }
            }

        } else
            return "Email Not found or userId not found";

        return "Not a such user";
    }


    private String createUrl(String BaseUrl, String userId, String token) {
        //TODO: Need to uncomment for war file
        return BaseUrl + ":" + /*"" +*/ EMAILCONTROLLER + userId + "/" + token.toString();

    }

    @Override
    public Person checkToken(String userId, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();

        person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);

        String tok = person.getToken().toString();

        if (token.equals(tok)) {
            return person;
        } else
            return null;

    }


    private String passwordResetToken(String userId) {

        Date date = new Date();

        return UUID.randomUUID().toString().toUpperCase()
                + userId
                + date.getTime();
    }

    public String insertToken(Person person) {
        String token = passwordResetToken(person.getUserId());
        person.setToken(token);
        Name dn = buildDn(person.getUserId());
        Context context = buildAttributes(person.getUserId(), person, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Token for " + person.getUserId() + " is created";
    }

    public String insertProfilePic(Person person) {
        String token = passwordResetToken(person.getUserId());
        person.setToken(token);
        Name dn = buildDn(person.getUserId());
        Context context = buildAttributes(person.getUserId(), person, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Token for " + person.getUserId() + " is created";
    }


    private class PersonAttributeMapper implements AttributesMapper<Person> {

        @Override
        public Person mapFromAttributes(Attributes attributes) throws NamingException {
            Person person = new Person();

            person.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            person.setFirstName(null != attributes.get("givenName") ? attributes.get("givenName").get().toString() : null);
            person.setLastName(null != attributes.get("sn") ? attributes.get("sn").get().toString() : null);
            person.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
            person.setMobile(null != attributes.get("mobile") ? attributes.get("mobile").get().toString() : null);
            person.setMail(null != attributes.get("mail") ? attributes.get("mail").get().toString() : null);
            int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
            List<String> ls = new LinkedList<>();
            for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());
            person.setToken(null != attributes.get("l") ? attributes.get("l").get().toString() : null);
            person.setMemberOf(null != attributes.get("ou") ? ls : null);
            person.setUserPassword(null != attributes.get("userPassword") ? attributes.get("userPassword").get().toString() : null);
            person.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            person.setPhoto(null != attributes.get("photo") ? attributes.get("photo").get().toString().getBytes() : null);
            return person;
        }
    }

    public String updatePass(String userId, String pass, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);

        if (token.equals(person.getToken())) {
            person.setUserPassword(pass);
            Name dn = buildDn(person.getUserId());

            DirContextOperations context = buildAttributes(userId, person, dn);
            ldapTemplate.modifyAttributes(context);

            return userId + " passwords was updated";
        } else {
            return "userId or token was incorrect";
        }
    }

    public void excelSheetAnalyze(Sheet sheet, int[] sequence, boolean hasHeader){

        Iterator<Row> rowIterator = sheet.iterator();

        if (hasHeader == true) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();

            Person person = new Person();


            Cell cell = row.getCell(0);
            //Check the cell type and format accordingly
            Name dn = buildDn(cell.getStringCellValue());

            person.setUserId(row.getCell(sequence[0]).getStringCellValue());
            person.setFirstName(row.getCell(sequence[1]).getStringCellValue());
            person.setLastName(row.getCell(sequence[2]).getStringCellValue());
            person.setDisplayName(row.getCell(sequence[3]).getStringCellValue());
            person.setMobile(row.getCell(sequence[4]).getStringCellValue());
            person.setMail(row.getCell(sequence[5]).getStringCellValue());
            person.setMemberOf(Collections.singletonList(row.getCell(sequence[6]).getStringCellValue()));
            person.setUserPassword(row.getCell(sequence[7]).getStringCellValue());
            person.setDescription(row.getCell(sequence[8]).getStringCellValue());
            person.setPhoto(row.getCell(sequence[9]).getStringCellValue().getBytes());

            System.out.println(person);

            create(person);

            System.out.println("");
        }
    }


    public void csvSheetAnalyze(BufferedReader sheet, int[] sequence, boolean hasHeader) throws IOException {

        String row;
        int i=0;

        while ((row = sheet.readLine()) != null) {
            if (i==0  && hasHeader) {
                i++;

                continue;
            }
            System.out.println(row);
            String[] data = row.split(",");
            // do something with the data

            Person person = new Person();

            person.setUserId(data[sequence[0]]);
            person.setFirstName(data[sequence[1]]);
            person.setLastName(data[sequence[2]]);
            person.setDisplayName(data[sequence[3]]);
            person.setMobile(data[sequence[4]]);
            person.setMail(data[sequence[5]]);
            person.setMemberOf(Collections.singletonList(data[sequence[6]]));
            person.setUserPassword((data[sequence[7]]));
            person.setDescription((data[sequence[8]]));
            person.setPhoto((data[sequence[9]]).getBytes());

            System.out.println(person);

            create(person);

            i++;

            System.out.println("");
        }
    }

    @Override
    public List<Person> importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) {

        try {
            InputStream insfile = file.getInputStream();
            String format = file.getContentType();
            System.out.println(format);
            switch (format){

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    System.out.println("XLSX");
                    //Create Workbook instance holding reference to .xlsx file
                    XSSFWorkbook workbookXLSX = new XSSFWorkbook(insfile);

                    //Get first/desired sheet from the workbook
                    XSSFSheet sheet = workbookXLSX.getSheetAt(0);

                    excelSheetAnalyze(sheet , sequence, hasHeader);

                    break;

                case "application/vnd.ms-excel":
                    HSSFWorkbook workbookXLS = new HSSFWorkbook(insfile);
                    System.out.println("XLS");

                    HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

                    excelSheetAnalyze(xlssheet ,sequence, hasHeader);

                case "text/csv":

                    BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

                    csvSheetAnalyze(csvReader,sequence,hasHeader);

                    csvReader.close();


                    break;


            }




        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public BufferedImage retrievePic(String name) throws IOException {
        Person person = retrievePerson(name);
        byte[] imageBytes = person.getPhoto();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bis);
    }
}
