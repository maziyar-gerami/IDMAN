package parsso.idman.impls.authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;

@Service
public class DeleteAuthenticator implements AuthenticatorRepo.Delete {

  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  @Autowired
  public DeleteAuthenticator(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  @Override
  public HttpStatus deleteByDeviceName(String name, String doer) {
    Devices device = mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), Devices.class,
        Variables.col_devices);
    if (device == null)
      return HttpStatus.FORBIDDEN;

    try {
      mongoTemplate.remove(new Query(Criteria.where("name").is(name)), Devices.class, Variables.col_devices);
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, name, Variables.ACTION_DELETE,
              Variables.RESULT_SUCCESS));

    } catch (Exception e) {
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, name, Variables.ACTION_DELETE,
              Variables.RESULT_FAILED));
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.NO_CONTENT;
  }

  @Override
  public HttpStatus deleteByUsername(String username, String doer) {

    Devices device = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), Devices.class,
        Variables.col_devices);
    if (device == null)
      return HttpStatus.FORBIDDEN;

    try {
      mongoTemplate.remove(new Query(Criteria.where("username").is(username)), Devices.class,
          Variables.col_devices);
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, username, Variables.ACTION_DELETE,
              Variables.RESULT_SUCCESS));

    } catch (Exception e) {
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, username, Variables.ACTION_DELETE,
              Variables.RESULT_FAILED));
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.NO_CONTENT;
  }

  @Override
  public HttpStatus deleteByUsernameAndDeviceName(String username, String deviceName, String doer) {
    Query query = new Query(Criteria.where("username").is(username).and("name").is(deviceName));

    Devices device = mongoTemplate.findOne(query, Devices.class, Variables.col_devices);
    if (device == null)
      return HttpStatus.FORBIDDEN;

    try {
      mongoTemplate.findAndRemove(query, Devices.class, Variables.col_devices);
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, username, Variables.ACTION_DELETE,
              Variables.RESULT_SUCCESS));

    } catch (Exception e) {
      uniformLogger.info(doer,
          new ReportMessage(Variables.MODEL_AUTHENTICATOR, username, Variables.ACTION_DELETE,
              Variables.RESULT_FAILED));
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.NO_CONTENT;
  }

}
