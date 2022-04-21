package parsso.idman.impls.publicMessages.subClasses;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.PublicMessage;

public class RetrievePubMessage {
  MongoTemplate mongoTemplate;

  public RetrievePubMessage(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<PublicMessage> showAllPubicMessages(String id) {
    if (id.equals(""))
      return mongoTemplate.find(new Query(new Criteria()), PublicMessage.class, Variables.col_publicMessage);

    return mongoTemplate.find(new Query(Criteria.where("_id").is(id)), PublicMessage.class,
        Variables.col_publicMessage);
  }

  public List<PublicMessage> showVisiblePubicMessages() {
    return mongoTemplate.find(new Query(Criteria.where("visible").is(true)), PublicMessage.class,
        Variables.col_publicMessage);
  }

}
