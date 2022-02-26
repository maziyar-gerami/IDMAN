package parsso.idman.repoImpls.services.create.sublcasses;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.SamlService;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.repoImpls.services.types.CAS;
import parsso.idman.repoImpls.services.types.OAuth;
import parsso.idman.repoImpls.services.types.SAML;
import parsso.idman.repos.ServiceRepo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Analyze {
    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;
    ServiceRepo.Retrieve serviceRetrieve;

    public Analyze(MongoTemplate mongoTemplate,ServiceRepo.Retrieve serviceRetrieve, UniformLogger uniformLogger) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }

    public Service analyze(String file) throws IOException, ParseException {

        FileReader reader = new FileReader(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue() + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        reader.close();

        Service service = null;
        ExtraInfo extraInfo;

        if (isCasService((JSONObject) obj))
            service = new CAS(mongoTemplate,uniformLogger).analyze(file);
        else if (isOAuthService((JSONObject) obj))
            service = new OAuth(mongoTemplate,uniformLogger).analyze(file);
        else if (isSamlService((JSONObject) obj))
            service = new SAML(mongoTemplate,uniformLogger).analyze(file);


        Query query = new Query(Criteria.where("_id").is(Objects.requireNonNull(service).getId()));
        try {
            extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, Variables.col_servicesExtraInfo);
        } catch (Exception e) {
            extraInfo = new ExtraInfo();
            e.printStackTrace();
            uniformLogger.warn("System", new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
                    Variables.ACTION_PARSE, Variables.RESULT_FAILED, "Unable to get extra service for service"));

        }

        service.setExtraInfo(extraInfo);

        return service;
    }

    private boolean isSamlService(JSONObject jo) {
        return jo.get("@class").toString().equals("org.apereo.cas.support.saml.services.SamlRegisteredService");
    }

    private boolean isOAuthService(JSONObject jo) {
        return jo.get("@class").toString().equals("org.apereo.cas.support.oauth.services.OAuthRegisteredService");
    }

    boolean isCasService(JSONObject jo) {

        return jo.get("@class").toString().equals("org.apereo.cas.services.RegexRegisteredService");

    }
}
