package parsso.idman.repoImpls.publicMessages.subClasses;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PublicMessage;

public class DeletePubMessage {
    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;


    public DeletePubMessage(MongoTemplate mongoTemplate, UniformLogger uniformLogger){
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }
    

    public HttpStatus deletePubicMessage(String doer, JSONObject jsonObject) {

        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();

        if (!iterator.hasNext()) {
            try {

                mongoTemplate.remove(new Query(), Variables.col_publicMessage);
                uniformLogger.info(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, "All", "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

            } catch (Exception e) {

                e.printStackTrace();
                uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, "All", "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, ""));

            }
        }

        while (iterator.hasNext()) {
            String next = iterator.next();

            try {
                mongoTemplate.remove(new Query(Criteria.where("_id").is(next)), Variables.col_publicMessage);
                uniformLogger.info(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, next, "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, next, "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, "Writing to mongo"));
            }

        }

        return HttpStatus.OK;
    }
    
}
