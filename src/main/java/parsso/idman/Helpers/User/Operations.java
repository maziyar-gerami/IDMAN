package parsso.idman.Helpers.User;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

@Service
public class Operations {

    @Autowired
    BuildDnUser buildDnUser;
    @Autowired
    UserRepo userRepo;
    @Autowired
    LdapTemplate ldapTemplate;
    private final String model = "Users";
    @Value("${qr.devices.path}")
    private String qrDevicesPath;

    public HttpStatus enable(String doer, String uid) {

        Logger logger = LogManager.getLogger(doer);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = userRepo.retrieveUsers(uid);
        String status = user.getStatus();

        if (status.equalsIgnoreCase("disable")) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.warn(new ReportMessage(model, user.getUserId(), "", "enable", "success", "").toString());

                return HttpStatus.OK;

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, user.getUserId(), "", "enable", "failed", "writing to ldap").toString());
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus disable(String doerID, String uid) {
        Logger logger = LogManager.getLogger(doerID);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = userRepo.retrieveUsers(uid);

        if (user.isEnabled()) {


            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                logger.warn(new ReportMessage(model, user.getUserId(), "", "disable", "success", "").toString());
                return HttpStatus.OK;

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, user.getUserId(), "", "disable", "failed", "writing to ldap").toString());
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus unlock(String doerID, String uid) {

        Logger logger = LogManager.getLogger(doerID);


        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = userRepo.retrieveUsers(uid);
        String locked = user.getStatus();

        if (locked.equalsIgnoreCase("lock")) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);

                try {
                    modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdFailureTime"));
                    ldapTemplate.modifyAttributes(dn, modificationItems);
                } catch (Exception e) {
                }

                logger.warn(new ReportMessage(model, user.getUserId(), "", "unlock", "success", "").toString());
                return HttpStatus.OK;

            } catch (Exception e) {

                return HttpStatus.BAD_REQUEST;
            }

        } else {
            return HttpStatus.BAD_REQUEST;
        }

    }

    public String activeMobile(User user) {

        Logger logger = LogManager.getLogger("SYSTEM");

        String uuid = UUID.randomUUID().toString();

        String action = "Insert";

        {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(qrDevicesPath)) {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

                boolean existed = false;

                for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext(); ) {

                    String key = (String) iterator.next();
                    String value = (String) jsonObject.get(key);
                    if (value.equalsIgnoreCase(user.getUserId())) {
                        jsonObject.remove(key, value);
                        existed = true;
                        break;
                    }
                }

                jsonObject.put(uuid, user.getUserId());

                ObjectMapper mapper = new ObjectMapper();

                try {

                    // Writing to a file
                    mapper.writeValue(new File(qrDevicesPath), jsonObject);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!existed)
                    action = "Update";

                logger.warn(new ReportMessage(model, user.getUserId(), "DeviceID", action, "success", ""));

                return uuid;


            } catch (FileNotFoundException e) {
                logger.warn(new ReportMessage(model, user.getUserId(), "DeviceID", action, "fail", "file not found"));
            } catch (IOException e) {
                logger.warn(new ReportMessage(model, user.getUserId(), "DeviceID", action, "fail", "Saving problem"));

            } catch (org.json.simple.parser.ParseException e) {
                logger.warn(new ReportMessage(model, user.getUserId(), "DeviceID", action, "fail", "json parse"));

            }
        }

        return null;
    }

}
