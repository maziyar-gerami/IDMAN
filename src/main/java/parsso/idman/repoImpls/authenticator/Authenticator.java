package parsso.idman.repoImpls.authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Devices;
import parsso.idman.repoImpls.authenticator.subClasses.Delete;
import parsso.idman.repoImpls.authenticator.subClasses.Retrieve;
import parsso.idman.repos.AuthenticatorRepo;

@Service
public class Authenticator implements AuthenticatorRepo {
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;

    @Autowired
    Authenticator(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }

    @Override
    public Devices.DeviceList retrieve(String username, String deviceName, int page, int count) {
        return new Retrieve(mongoTemplate,uniformLogger).retrieve(username,deviceName,page,count);
    }

    @Override
    public HttpStatus deleteByDeviceName(String name, String doer) {
        return new Delete(mongoTemplate,uniformLogger).deleteByDeviceName(name,doer);
    }

    @Override
    public HttpStatus deleteByUsername(String username, String doer) {

        return new Delete(mongoTemplate,uniformLogger).deleteByUsername(username,doer);
    }

    @Override
    public HttpStatus deleteByUsernameAndDeviceName(String username, String deviceName, String doer) {
        return new Delete(mongoTemplate,uniformLogger).deleteByUsernameAndDeviceName(username,deviceName, doer);
    }

    @Override
    public Boolean retrieveUsersDevice(String username) {
        return mongoTemplate.count(new Query(Criteria.where("username").is(username)), Variables.col_GoogleAuthDevice) > 0;
    }
}
