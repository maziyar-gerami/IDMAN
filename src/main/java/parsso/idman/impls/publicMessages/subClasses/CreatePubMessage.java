package parsso.idman.impls.publicMessages.subClasses;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PublicMessage;

public class CreatePubMessage {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  public CreatePubMessage(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public HttpStatus postPubicMessage(String doer, PublicMessage message) {

    if (!mongoTemplate.collectionExists(Variables.col_publicMessage))
      mongoTemplate.createCollection(Variables.col_publicMessage);

    try {
      PublicMessage messageToSave = new PublicMessage(message.getTitle(), message.getBody(), message.isVisible(),
          doer);
      mongoTemplate.save(messageToSave, Variables.col_publicMessage);
      uniformLogger.info(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, messageToSave.getMessageId(), "",
          Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

      return HttpStatus.OK;

    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_PUBICMESSAGE, message.getMessageId(), "",
          Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to mongo"));
      return HttpStatus.FORBIDDEN;

    }

  }
}
