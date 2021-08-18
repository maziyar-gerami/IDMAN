package parsso.idman.Helpers.User;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
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
    private final String model = "Users";
    @Autowired
    BuildDnUser buildDnUser;
    @Autowired
    UserRepo userRepo;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    UniformLogger uniformLogger;
    @Value("${qr.devices.path}")
    private String qrDevicesPath;

    public HttpStatus enable(String doer, String uid) throws IOException, ParseException {

        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = userRepo.retrieveUsers(uid);
        String status = user.getStatus();

        if (status.equalsIgnoreCase("disable")) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                uniformLogger.record(doer, Variables.LEVEL_INFO, new ReportMessage(model, user.getUserId(), "", "Enable", "Success", ""));

                return HttpStatus.OK;

            } catch (Exception e) {
                uniformLogger.record(doer, Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "", "Enable", "Failed", "Writing to ldap"));
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus disable(String doerID, String uid) throws IOException, ParseException {

        Name dn = buildDnUser.buildDn(uid);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        User user = userRepo.retrieveUsers(uid);

        if (user.isEnabled()) {

            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "40400404040404.950Z"));

            try {
                ldapTemplate.modifyAttributes(dn, modificationItems);
                uniformLogger.record(doerID, Variables.LEVEL_INFO, new ReportMessage(model, user.getUserId(), "", "Disable", "Success", ""));
                return HttpStatus.OK;

            } catch (Exception e) {
                uniformLogger.record(doerID, Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "", "Disable", "Failed", "Writing to ldap"));
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus unlock(String doerID, String uid) throws IOException, ParseException {

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
                    uniformLogger.record(doerID, Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "", "Unlock", "Failed", "Problem with LDAP modifyAttribute"));
                }

                uniformLogger.record(doerID, Variables.LEVEL_INFO, new ReportMessage(model, user.getUserId(), "", "Unlock", "Success", ""));
                return HttpStatus.OK;

            } catch (Exception e) {
                uniformLogger.record(doerID, Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "", "Unlock", "Failed", "Problem with LDAP modifyAttribute"));
                return HttpStatus.BAD_REQUEST;
            }

        } else {
            return HttpStatus.BAD_REQUEST;
        }

    }

    public String activeMobile(User user) {

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
                    uniformLogger.record(user.getUserId(), Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "DeviceID", action, "Failed", "Saving File problem"));

                }

                if (!existed)
                    action = "Update";
                uniformLogger.record(user.getUserId(), Variables.LEVEL_INFO, new ReportMessage(model, user.getUserId(), "DeviceID", action, "Success", ""));
                return uuid;


            } catch (FileNotFoundException e) {
                uniformLogger.record(user.getUserId(), Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "DeviceID", action, "Failed", "File not found"));
            } catch (IOException e) {
                uniformLogger.record(user.getUserId(), Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "DeviceID", action, "Failed", "Saving problem"));

            } catch (org.json.simple.parser.ParseException e) {
                uniformLogger.record(user.getUserId(), Variables.LEVEL_WARN, new ReportMessage(model, user.getUserId(), "DeviceID", action, "Failed", "Json ّfile parse problem"));

            }
        }
        return null;
    }

}
