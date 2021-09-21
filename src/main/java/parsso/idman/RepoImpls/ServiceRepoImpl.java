package parsso.idman.RepoImpls;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Service.*;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceGist;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Services.ServicesSubModel.ExtraInfo;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Other.GenerateUUID;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UserRepo userRepo;
	@Autowired
	CasServiceHelper casServiceHelper;
	@Autowired
	SamlServiceHelper samlServiceHelper;
	@Autowired
	OAuthServiceHelper oAuthServiceHelper;
	@Autowired
	FilesStorageService storageService;
	@Autowired
	LdapTemplate ldapTemplate;
	@Autowired
	Position position;
	@Autowired
	UniformLogger uniformLogger;
	String collection = Variables.col_servicesExtraInfo;
	@Value("${services.folder.path}")
	private String path;
	@Value("${base.url}")
	private String baseUrl;

	@Override
	public List<MicroService> listUserServices(User user) throws IOException {

		List<Service> services = listServicesFull();

		List<Service> relatedList = new LinkedList();

		for (Service service : services) {

			if (service.getAccessStrategy() != null)
				if (service.getAccessStrategy().getRequiredAttributes() != null)
					if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null) {

						Object member = service.getAccessStrategy().getRequiredAttributes().get("ou");
						if (member != null) {
							JSONArray s = (JSONArray) member;

							if (user.getMemberOf() != null && s != null)
								for (int i = 0; i < user.getMemberOf().size(); i++)
									for (int j = 0; j < ((JSONArray) s.get(1)).size(); j++) {
										if (user.getMemberOf().get(i).equals(((JSONArray) s.get(1)).get(j)) && !relatedList.contains(service)) {
											relatedList.add(service);
											break;

										}
									}
						}


					}


			try {
				if (((List<String>)(((JSONArray)(service.getAccessStrategy().getRequiredAttributes().get("uid"))).get(1))).contains(user.getUserId()))
					relatedList.add(service);
			}catch (NullPointerException e){

			}



		}
		List<MicroService> microServices = new LinkedList<>();
		MicroService microService = null;
		relatedList.stream().distinct();

		for (Service service : relatedList) {
			Query query = new Query(Criteria.where("_id").is(service.getId()));
			try {
				microService = mongoTemplate.findOne(query, MicroService.class, collection);
			} catch (Exception e) {
				e.printStackTrace();
				microService = new MicroService(service.getId(), service.getServiceId());
				uniformLogger.error(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getId(), "",
						Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "unable read extra info from mongo"));

			} finally {
				MicroService fMicro = new MicroService(service, microService);

				try {
					fMicro.setNotification(new Notifs().getNotifications(user.getUserId(),
							service.getExtraInfo().getNotificationApiURL(), service.getExtraInfo().getNotificationApiKey()));
				}catch (Exception e){e.printStackTrace();}

				microServices.add(fMicro);

			}
		}

		Collections.sort(microServices);
		return microServices;

	}

	@Override
	public List<Service> listServicesFull() throws IOException {
		File folder = new File(path); // ./services/
		String[] files = folder.list();
		List<Service> services = new LinkedList<>();
		Service service = null;
		for (String file : files) {
			if (file.endsWith(".json"))
				try {
					service = analyze(file);
					services.add(service);
					Collections.sort(services);
				} catch (Exception e) {
					e.printStackTrace();
					uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
							"", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to read service"));
					continue;
				}
		}
		return services;
	}

	@Override
	public List<Service> listServicesWithGroups(String ou) throws IOException {
		List<Service> allServices = listServicesFull();

		List<Service> relatedList = new LinkedList<>();

		for (Service service : allServices) {
			JSONArray o = (JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou");

			if (o == null || o.size() == 0)
				return null;
			List<String> attributes = (List<String>) o.get(1);

			if (attributes.contains(ou)) {
				relatedList.add(service);
				continue;
			}
		}
		return relatedList;
	}

	@Override
	public List<MicroService> listServicesMain() {

		File folder = new File(path); // ./services/
		String[] files = folder.list();
		List<MicroService> services = new LinkedList<>();
		Service service = null;
		MicroService microService = null;
		for (String file : files) {
			if (file.endsWith(".json"))
				try {
					service = analyze(file);
				} catch (Exception e) {
					e.printStackTrace();
					uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getId(), "",
							Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to parse service"));

					continue;
				}
			Query query = new Query(Criteria.where("_id").is(Long.valueOf(Trim.extractIdFromFile(file))));
			try {
				microService = mongoTemplate.findOne(query, MicroService.class, collection);
			} catch (Exception e) {
				microService = new MicroService(service.getId(), service.getServiceId());
			} finally {
				services.add(new MicroService(service, microService));
			}
		}
		Collections.sort(services);
		return services;
	}

	@Override
	public Service retrieveService(long serviceId) throws IOException {

		ExtraInfo extraInfo;

		for (Service service : listServicesFull())
			if (service.getId() == serviceId) {

				Query query = new Query(Criteria.where("_id").is(service.getId()));
				try {
					extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
				} catch (Exception e) {
					extraInfo = new ExtraInfo();
					e.printStackTrace();
					uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
							"", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to get extraInfo of service"));

				}

				service.setExtraInfo(extraInfo);
				return service;
			}
		return null;
	}

	@Override
	public LinkedList<String> deleteServices(String doerID, JSONObject jsonObject) {

		File folder = new File(path);
		List allFiles = Arrays.asList(folder.list());
		List selectedFiles = new LinkedList();

		if (jsonObject.size() == 0)
			selectedFiles = allFiles;

		else {
			ArrayList<String> files = (ArrayList<String>) jsonObject.get("names");
			for (String file : files)
				for (Object aFile : allFiles)
					if (aFile.toString().contains(file))
						selectedFiles.add(aFile);

		}

		LinkedList<String> notDeleted = null;

		for (Object file : selectedFiles) {
			File serv = new File(path + file.toString());
			if (!(serv.delete()))
				notDeleted.add((String) file);

		}

		for (Object file : selectedFiles) {
			long id = Trim.extractIdFromFile(file.toString());
			Query query = new Query(Criteria.where("_id").is(id));
			MicroService microService = mongoTemplate.findOne(query, MicroService.class, collection);
			position.delete(microService.getPosition());
			try {
				mongoTemplate.remove(query, MicroService.class, collection);
				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
						Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

			} catch (Exception e) {
				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
						Variables.ACTION_DELETE, Variables.RESULT_FAILED, "Writing to mongoDB"));

			}

		}

		return notDeleted;
	}

	@Override
	public HttpStatus updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException {

		//Update ou
		userRepo.updateUsersWithSpecificOU(doerID, oldOu, newOu);

		//Update text
		String fileName = String.valueOf(sid);
		String s1 = fileName.replaceAll("\\s+", "");
		String filePath = name + "-" + sid + ".json";

		ObjectMapper mapper = new ObjectMapper();
		//Converting the Object to JSONString
		String jsonString = mapper.writeValueAsString(service);

		try {
			FileWriter file = new FileWriter(path + filePath, false);
			file.write(jsonString);
			file.close();
			uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, service, ""));
		} catch (Exception e) {
			e.printStackTrace();
			uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, service, ""));

		}

		return HttpStatus.OK;

	}

	@Override
	public HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException {

		ExtraInfo extraInfo = new ExtraInfo();
		long id = 0;
		JSONObject jsonExtraInfo = new JSONObject();

		extraInfo.setUrl(jsonExtraInfo != null && jsonExtraInfo.get("url") != null ?
				jsonExtraInfo.get("url").toString() : jsonObject.get("serviceId").toString());

		if (baseUrl.contains("localhost"))
			extraInfo.setUUID(GenerateUUID.getUUID());

		if (system.equalsIgnoreCase("cas"))
			id = casServiceHelper.create(doerID, jsonObject);

		else if (system.equalsIgnoreCase("saml"))
			id = samlServiceHelper.create(doerID, jsonObject);

		else if (system.equalsIgnoreCase("OAuth"))
			id = oAuthServiceHelper.create(doerID, jsonObject);

		if (id > 0)
			if (extraInfo == null)
				extraInfo = new ExtraInfo();

		String jsonString = new Gson().toJson(jsonObject.get("extraInfo"), Map.class);

		JSONParser parser = new JSONParser();
		JSONObject jsonObjectExtraInfo = (JSONObject) parser.parse(jsonString);

		extraInfo = setExtraInfo(id, extraInfo, jsonObjectExtraInfo, position.lastPosition() + 1);

		try {
			mongoTemplate.save(extraInfo, collection);

			return HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to mongoDB"));
			return HttpStatus.FORBIDDEN;
		}

	}

	private ExtraInfo setExtraInfo(long id, ExtraInfo extraInfo, JSONObject jsonObject, int i) {
		extraInfo.setId(id);
		extraInfo.setPosition(i);
		try {
			extraInfo.setNotificationApiURL(jsonObject.get("notificationApiURL").toString());
		} catch (Exception e) {
		}

		try {
			extraInfo.setNotificationApiKey(jsonObject.get("notificationApiKey").toString());
		} catch (Exception e) {
		}

		return extraInfo;
	}

	@Override
	public String uploadMetadata(MultipartFile file) {
		Date date = new Date();
		String fileName = date.getTime() + "_" + file.getOriginalFilename();

		try {
			storageService.saveMetadata(file, fileName);
			return baseUrl + "/api/public/metadata/" + fileName;
			//return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) throws IOException, ParseException {

		JSONObject JsonExtraInfo = null;

		ExtraInfo extraInfo = new ExtraInfo();

		Query query = new Query(Criteria.where("_id").is(id));

		ExtraInfo oldExtraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);

		if (jsonObject.get("extraInfo") != null) {

			if (jsonObject.get("extraInfo").getClass().toString().equals("class org.json.simple.JSONObject"))
				JsonExtraInfo = (JSONObject) jsonObject.get("extraInfo");

			else if (jsonObject.get("extraInfo").getClass().toString().equals("class java.util.LinkedHashMap"))
				JsonExtraInfo = new JSONObject((Map) jsonObject.get("extraInfo"));

			extraInfo.setUrl(JsonExtraInfo != null && JsonExtraInfo.get("url") != null ?
					JsonExtraInfo.get("url").toString() : jsonObject.get("serviceId").toString());

			extraInfo.setUrl(JsonExtraInfo.get("url") != null ? JsonExtraInfo.get("url").toString() : oldExtraInfo.getUrl());

			try {
				extraInfo.setNotificationApiURL(JsonExtraInfo.get("notificationApiURL") != null ? JsonExtraInfo.get("notificationApiURL").toString() : oldExtraInfo.getNotificationApiURL());

			} catch (Exception e) {
			}
			try {
				extraInfo.setNotificationApiKey(JsonExtraInfo.get("notificationApiKey") != null ? JsonExtraInfo.get("notificationApiKey").toString() : oldExtraInfo.getNotificationApiKey());
			} catch (Exception e) {
			}

			try {
				extraInfo.setPosition(oldExtraInfo.getPosition());
			} catch (Exception e) {
			}

			extraInfo.setId(id);
		}

		if (system.equalsIgnoreCase("cas")) {
			try {
				mongoTemplate.save(extraInfo, collection);

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
						Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
				return HttpStatus.FORBIDDEN;
			}
			return casServiceHelper.update(doerID, id, jsonObject);

		} else if (system.equalsIgnoreCase("saml")){
			try {
				mongoTemplate.save(extraInfo, collection);

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
						Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
				return HttpStatus.FORBIDDEN;
			}

			return samlServiceHelper.update(doerID, id, jsonObject);
		}
		else if (system.equalsIgnoreCase("OAuth"))
			try {
				mongoTemplate.save(extraInfo, collection);

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
						Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
				return HttpStatus.FORBIDDEN;
			}

		return oAuthServiceHelper.update(doerID, id, jsonObject);

	}

	@Override
	public HttpStatus increasePosition(String id) {
		return position.increase(id);
	}

	@Override
	public HttpStatus decreasePosition(String id) {
		return position.decrease(id);
	}

	@Override
	public ServiceGist gistService(String apikey) {
		return new ServiceGist();

	}

	private Service analyze(String file) throws IOException, ParseException {

		FileReader reader = new FileReader(path + file);
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(reader);
		reader.close();

		Service service;
		ExtraInfo extraInfo;

		if (isCasService((JSONObject) obj))
			service = casServiceHelper.analyze(file);
		else
			service = samlServiceHelper.analyze(file);

		Query query = new Query(Criteria.where("_id").is(service.getId()));
		try {
			extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
		} catch (Exception e) {
			extraInfo = new ExtraInfo();
			e.printStackTrace();
			uniformLogger.warn("System", new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
					Variables.ACTION_PARSE, Variables.RESULT_FAILED, "Unable to get extra service for service"));

		}

		service.setExtraInfo(extraInfo);

		return service;
	}

	boolean isCasService(JSONObject jo) {

		return !jo.get("@class").toString().contains("saml");

	}

}
