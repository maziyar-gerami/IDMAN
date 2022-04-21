package parsso.idman.impls.refresh.subclass;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;

public class CaptchaRefresh {

  final UniformLogger uniformLogger;
  final MongoTemplate mongoTemplate;

  public CaptchaRefresh(UniformLogger uniformLogger, MongoTemplate mongoTemplate) {
    this.uniformLogger = uniformLogger;
    this.mongoTemplate = mongoTemplate;
  }

  public HttpStatus refresh(String doer) {

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_CAPTCHA, "", "", Variables.ACTION_REFRESH,
        Variables.RESULT_STARTED, ""));
    mongoTemplate.getCollection(Variables.col_captchas);
    mongoTemplate.getCollection(Variables.col_captchas).drop();

    mongoTemplate.createCollection(Variables.col_captchas);
    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_CAPTCHA, "", "",
        Variables.ACTION_REFRESH, Variables.RESULT_SUCCESS, ""));

    return HttpStatus.OK;
  }

}
