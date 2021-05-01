package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PubMessageRepoImpl implements PubMessageRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    final String model = "PublicMessage";

    private final String collection = "IDMAN_PublicMessage";



    @Override
    public List<PublicMessage> showPubicMessages(String id) {
        if (id.equals(""))
            return mongoTemplate.find(new Query(), PublicMessage.class, collection);

        return mongoTemplate.find(new Query(Criteria.where("ID").is(id)), PublicMessage.class, collection);
    }


    @Override
    public HttpStatus postPubicMessage(String doer, PublicMessage message) {
        Logger logger = LogManager.getLogger(doer);


        if (!mongoTemplate.collectionExists(collection))
            mongoTemplate.createCollection(collection);

        try {
            PublicMessage messageToSave = new PublicMessage(message.getTitle(),message.getBody(),message.isVisible(),doer);
            mongoTemplate.save(messageToSave,collection);
            logger.warn(new ReportMessage(model, messageToSave.getID(),"", "create", "success", ""));

            return  HttpStatus.OK;

        }catch (Exception e){
            logger.warn(new ReportMessage(model, message.getID(),"","create", "failed", "Writing to mongo"));
            return HttpStatus.FORBIDDEN;

        }

    }

    @Override
    public HttpStatus editPubicMessage(String doer, PublicMessage message) {
        Logger logger = LogManager.getLogger(doer);

        message.setUpdater(doer);
        message.setUpdateDate(System.currentTimeMillis());

        PublicMessage publicMessage = mongoTemplate.findOne(new Query(Criteria.where("ID").is(message.getID())), PublicMessage.class,collection);
        message.set_id(publicMessage.get_id());
        try {
            mongoTemplate.save(message,collection);
            logger.warn(new ReportMessage(model, message.getID(),"", "update", "success", ""));

            return HttpStatus.OK;
        }catch (Exception e){
            logger.warn(new ReportMessage(model, message.getID(),"","create", "failed", "Writing to mongo"));
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus deletePubicMessage(String doer, JSONObject jsonObject) {
        Logger logger = LogManager.getLogger(doer);

        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();

        if (!iterator.hasNext()) {
            try {

                mongoTemplate.remove(new Query(), collection);
                logger.warn(new ReportMessage(model, "All", "", "delete", "success", ""));

            }catch (Exception e){

                logger.warn(new ReportMessage(model, "All", "", "delete", "failed", ""));

            }
        }

        while (iterator.hasNext()) {
            String next = iterator.next();

            try {
                mongoTemplate.remove(new Query(Criteria.where("ID").is(next)),collection);
                logger.warn(new ReportMessage(model, next, "", "delete", "success", ""));

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, next, "", "delete", "failed", "Writing to mongo"));
            }

        }

        return HttpStatus.OK;
    }
}
