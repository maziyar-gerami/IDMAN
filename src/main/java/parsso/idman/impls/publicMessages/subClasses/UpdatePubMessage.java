package parsso.idman.impls.publicMessages.subClasses;

import java.util.Objects;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PublicMessage;

public class UpdatePubMessage {

  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  public UpdatePubMessage(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public HttpStatus editPubicMessage(String doer, PublicMessage message) {

    PublicMessage oldMessage;
    try {
      oldMessage = new RetrievePubMessage(mongoTemplate).showAllPubicMessages(message.getMessageId()).get(0);
    } catch (IndexOutOfBoundsException e) {
      return HttpStatus.NOT_FOUND;
    }

    message.setUpdater(doer);
    message.setUpdateDate(System.currentTimeMillis());

    message.setCreator(oldMessage.getCreator());
    message.setCreateDate(oldMessage.getCreateDate());

    PublicMessage publicMessage = mongoTemplate.findOne(new Query(Criteria.where("messageId").is(message.getMessageId())),
        PublicMessage.class, Variables.col_publicMessage);
    message.set_id(Objects.requireNonNull(publicMessage).get_id());
    try {
      mongoTemplate.save(message, Variables.col_publicMessage);
      uniformLogger.info(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, message.getMessageId(), "",
          "update", Variables.RESULT_SUCCESS, ""));

      return HttpStatus.OK;
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, message.getMessageId(), "",
          Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Writing to mongo"));
      return HttpStatus.FORBIDDEN;
    }
  }
}
