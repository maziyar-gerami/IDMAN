package parsso.idman.impls.refresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.refresh.subclass.CaptchaRefresh;
import parsso.idman.impls.refresh.subclass.LockedUsers;
import parsso.idman.impls.refresh.subclass.ServiceRefresh;
import parsso.idman.impls.refresh.subclass.UserRefresh;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.repos.SystemRefresh;

@Service
public class SystemRefreshRepoImpl implements SystemRefresh {

  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;
  UserRefresh userRefresh;
  LockedUsers lockedUsers;

  public SystemRefreshRepoImpl(LockedUsers lockedUsers) {
    this.lockedUsers = lockedUsers;
  }

  @Autowired
  public SystemRefreshRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger, UserRefresh userRefresh,
      LockedUsers lockedUsers) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.userRefresh = userRefresh;
    this.lockedUsers = lockedUsers;
  }

  @Override
  public HttpStatus userRefresh(String doer) {

    return userRefresh.refresh(doer);
  }

  @Override
  public HttpStatus serviceRefresh(String doer) {

    return new ServiceRefresh(mongoTemplate, uniformLogger).refresh(doer);
  }

  @Override
  public void refreshLockedUsers() {
    lockedUsers.refresh();
  }

  @Override
  public HttpStatus captchaRefresh(String doer) {
    return new CaptchaRefresh(uniformLogger, mongoTemplate).refresh(doer);
  }

  @Override
  public HttpStatus all(String doer) {
    try {
      mongoTemplate.getCollection("IDMAN_Tokens").drop();
    } catch (Exception e) {
      e.printStackTrace();
    }

    captchaRefresh(doer);

    serviceRefresh(doer);

    userRefresh(doer);

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, Variables.DOER_SYSTEM, "",
        Variables.ACTION_REFRESH, Variables.RESULT_SUCCESS, ""));

    return HttpStatus.OK;
  }

}
