package parsso.idman.impls.authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;

@Service
public class RetrieveAuthenticator implements AuthenticatorRepo.Retrieve {
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  @Autowired
  public RetrieveAuthenticator(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public Devices.DeviceList retrieve(String username, String deviceName, int page, int count) {
    Query query = new Query();
    int skip = (page - 1) * count;
    int pages;
    long size;

    if (!username.equals(""))
      query.addCriteria(Criteria.where("username").regex(".*" + username + ".*", "i"));

    if (!deviceName.equals(""))
      query.addCriteria(Criteria.where("name").regex(".*" + deviceName + ".*", "i"));

    size = mongoTemplate.count(query, Variables.col_devices);

    if (page != 0 && count != 0)
      query.skip(skip).limit(count);

    if (count != 0)
      pages = (int) Math.ceil(size / (float) count);
    else
      pages = 1;
    return new Devices.DeviceList(mongoTemplate.find(query, Devices.class, Variables.col_devices), size, pages);
  }

  public Boolean retrieveUsersDevice(String username) {
    return mongoTemplate.count(new Query(Criteria.where("username").is(username)),
        Variables.col_GoogleAuthDevice) > 0;
  }
}
