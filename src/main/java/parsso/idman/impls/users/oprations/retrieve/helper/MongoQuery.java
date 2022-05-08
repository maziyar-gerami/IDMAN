package parsso.idman.impls.users.oprations.retrieve.helper;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoQuery {

  public static Query builder(String groupFilter, String searchUid, String searchDisplayName, String mobile,
      String userStatus) {
    Query query = new Query();
    if (!searchUid.equals("")) {
      query.addCriteria(Criteria.where("_id").regex(searchUid));
    }
    if (!searchDisplayName.equals("")) {
      query.addCriteria(Criteria.where("displayName").regex(searchDisplayName));
    }
    if (!userStatus.equals("")) {
      query.addCriteria(Criteria.where("status").is(userStatus));
    }
    if (!mobile.equals("")) {
      query.addCriteria(Criteria.where("mobile").regex(mobile));
    }
    if (!groupFilter.equals("")) {
      query.addCriteria(Criteria.where("memberOf").all(groupFilter));
    }
    return query;

  }

}
