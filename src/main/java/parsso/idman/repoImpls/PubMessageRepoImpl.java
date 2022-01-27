package parsso.idman.repoImpls;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PublicMessage;
import parsso.idman.repos.PubMessageRepo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
@Service
public class PubMessageRepoImpl implements PubMessageRepo {
    final String model = "PublicMessage";
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UniformLogger uniformLogger;

    @Override
    public List<PublicMessage> showVisiblePubicMessages() {

        return mongoTemplate.find(new Query(Criteria.where("visible").is(true)), PublicMessage.class, Variables.col_publicMessage);
    }

    @Override
    public List<PublicMessage> showAllPubicMessages(String id) {
        if (id.equals(""))
            return mongoTemplate.find(new Query(new Criteria()), PublicMessage.class, Variables.col_publicMessage);

        return mongoTemplate.find(new Query(Criteria.where("messageId").is(id)), PublicMessage.class, Variables.col_publicMessage);
    }

    @Override
    public HttpStatus postPubicMessage(String doer, PublicMessage message) {

        if (!mongoTemplate.collectionExists(Variables.col_publicMessage))
            mongoTemplate.createCollection(Variables.col_publicMessage);

        try {
            PublicMessage messageToSave = new PublicMessage(message.getTitle(), message.getBody(), message.isVisible(), doer);
            mongoTemplate.save(messageToSave, Variables.col_publicMessage);
            uniformLogger.info(doer, new ReportMessage(model, messageToSave.getMessageId(), "", Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

            return HttpStatus.OK;

        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doer, new ReportMessage(model, message.getMessageId(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to mongo"));
            return HttpStatus.FORBIDDEN;

        }

    }

    @Override
    public HttpStatus editPubicMessage(String doer, PublicMessage message) {

        PublicMessage oldMessage;
        try {
            oldMessage = showAllPubicMessages(message.getMessageId()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return HttpStatus.BAD_REQUEST;
        }

        message.setUpdater(doer);
        message.setUpdateDate(System.currentTimeMillis());

        message.setCreator(oldMessage.getCreator());
        message.setCreateDate(oldMessage.getCreateDate());

        PublicMessage publicMessage = mongoTemplate.findOne(new Query(Criteria.where("messageId").is(message.getMessageId())), PublicMessage.class, Variables.col_publicMessage);
        message.set_id(Objects.requireNonNull(publicMessage).get_id());
        try {
            mongoTemplate.save(message, Variables.col_publicMessage);
            uniformLogger.info(doer, new ReportMessage(model, message.getMessageId(), "", "update", Variables.RESULT_SUCCESS, ""));

            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doer, new ReportMessage(model, message.getMessageId(), "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Writing to mongo"));
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus deletePubicMessage(String doer, JSONObject jsonObject) {

        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();

        if (!iterator.hasNext()) {
            try {

                mongoTemplate.remove(new Query(), Variables.col_publicMessage);
                uniformLogger.info(doer, new ReportMessage(model, "All", "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

            } catch (Exception e) {

                e.printStackTrace();
                uniformLogger.warn(doer, new ReportMessage(model, "All", "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, ""));

            }
        }

        while (iterator.hasNext()) {
            String next = iterator.next();

            try {
                mongoTemplate.remove(new Query(Criteria.where("messageId").is(next)), Variables.col_publicMessage);
                uniformLogger.info(doer, new ReportMessage(model, next, "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doer, new ReportMessage(model, next, "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, "Writing to mongo"));
            }

        }

        return HttpStatus.OK;
    }
}
