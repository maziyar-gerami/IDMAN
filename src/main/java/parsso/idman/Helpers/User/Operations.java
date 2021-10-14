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
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	public HttpStatus enable(String doer, String uid) {

		Name dn = buildDnUser.buildDn(uid);

		ModificationItem[] modificationItems;
		modificationItems = new ModificationItem[1];

		User user = userRepo.retrieveUsers(uid);
		String status = user.getStatus();

		if (status.equalsIgnoreCase("disable")) {
			modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime"));

			try {
				ldapTemplate.modifyAttributes(dn, modificationItems);
				uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.getUserId(), "", Variables.STATUS_CHANGE,
						Variables.RESULT_SUCCESS, Variables.STATUS_ENABLE, ""));

				return HttpStatus.OK;

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.getUserId(), "", Variables.STATUS_CHANGE,
						Variables.RESULT_FAILED, Variables.STATUS_ENABLE, "Writing to ldap"));
				return HttpStatus.BAD_REQUEST;
			}
		} else {
			return HttpStatus.BAD_REQUEST;
		}
	}

	public HttpStatus disable(String doerID, String uid) {

		Name dn = buildDnUser.buildDn(uid);

		ModificationItem[] modificationItems;
		modificationItems = new ModificationItem[1];

		User user = userRepo.retrieveUsers(uid);

		if (user.isEnabled()) {

			modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdAccountLockedTime", "00010101000000Z"));

			try {
				ldapTemplate.modifyAttributes(dn, modificationItems);
				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), "", Variables.RESULT_SUCCESS, Variables.ACTION_DISBLAE, ""));
				return HttpStatus.OK;

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), Variables.ATTR_STATUS,
						Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_DISBLAE, "Writing to DB"));
				return HttpStatus.BAD_REQUEST;
			}
		} else {
			return HttpStatus.BAD_REQUEST;
		}
	}

	public HttpStatus unlock(String doerID, String uid) {

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
					e.printStackTrace();
					uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), Variables.ATTR_STATUS,
							Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_UNLOCK, "Problem with LDAP modifyAttribute"));
				}

				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), Variables.ATTR_STATUS,
						Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, Variables.ACTION_UNLOCK, ""));
				return HttpStatus.OK;

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(), Variables.ATTR_STATUS,
						Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_UNLOCK, "Problem with LDAP modifyAttribute"));
				return HttpStatus.BAD_REQUEST;
			}

		} else {
			return HttpStatus.BAD_REQUEST;
		}

	}

	public String activeMobile(User user) {

		String uuid = UUID.randomUUID().toString();

		{
			//JSON parser object to parse read file
			JSONParser jsonParser = new JSONParser();

			try (FileReader reader = new FileReader(qrDevicesPath)) {
				//Read JSON file
				Object obj = jsonParser.parse(reader);
				org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

				boolean existed = false;

				for (Object o : jsonObject.keySet()) {

					String key = (String) o;
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
					uniformLogger.warn(user.getUserId(), new ReportMessage(Variables.MODEL_USER, user.getUserId(),
							Variables.ATTR_DEVICEID, Variables.ACTION_INSERT, Variables.RESULT_FAILED, "Saving File problem"));

				}

				if (!existed)
					uniformLogger.info(user.getUserId(), new ReportMessage(Variables.MODEL_USER, user.getUserId(),
							Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
				return uuid;


			} catch (FileNotFoundException e) {
				e.printStackTrace();
				uniformLogger.warn(user.getUserId(), new ReportMessage(Variables.MODEL_USER, user.getUserId(),
						Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "File not found"));
			} catch (IOException e) {
				e.printStackTrace();
				uniformLogger.warn(user.getUserId(), new ReportMessage(Variables.MODEL_USER, user.getUserId(),
						Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Saving problem"));

			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				uniformLogger.warn(user.getUserId(), new ReportMessage(Variables.MODEL_USER, user.getUserId(),
						Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Json ّfile parse problem"));

			}
		}
		return null;
	}

}
