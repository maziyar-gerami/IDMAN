package parsso.idman.RepoImpls;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

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
            return mongoTemplate.find(new Query(),PublicMessage.class, collection);

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
            logger.warn(new ReportMessage(model, doer,messageToSave.getID(),"create", "success", "by '" +doer+"'"));

            return  HttpStatus.OK;

        }catch (Exception e){
            logger.warn(new ReportMessage(model, doer,"","create", "failed", "Writing to mongo"));
            return HttpStatus.FORBIDDEN;

        }

    }

    @Override
    public HttpStatus editPubicMessage(String doer, PublicMessage message) {
        return null;
    }

    @Override
    public HttpStatus deletePubicMessage(String doer, String id) {
        return null;
    }
}
