package parsso.idman.repoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;

import java.util.List;

@Service
public class AuthenticatorRepoImpl implements AuthenticatorRepo {
    final MongoTemplate mongoTemplate;

    @Autowired
    AuthenticatorRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Devices.DeviceList retrieve(String username, String deviceName, int page, int count) {
        Query query = new Query();
        int skip = (page - 1) * count;

        if (!username.equals(""))
            query.addCriteria(Criteria.where("username").regex(".*" + username + ".*", "i"));

        if (!deviceName.equals(""))
            query.addCriteria(Criteria.where("name").regex(".*" + deviceName + ".*", "i"));

        if (page != 0 && count != 0) {
            query.skip(skip).limit(count);
        }
        List<Devices> devicesList = mongoTemplate.find(query, Devices.class, Variables.col_devices);
        long size = mongoTemplate.count(new Query(), Variables.col_devices);
        int pages;
        if (count != 0)
            pages = (int) Math.ceil(size / (float) count);
        else
            pages = 1;
        return new Devices.DeviceList(devicesList, size, pages);
    }

    @Override
    public HttpStatus deleteByDeviceName(String name) {
        Devices device = mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), Devices.class, Variables.col_devices);
        if (device == null)
            return HttpStatus.FORBIDDEN;

        try {
            mongoTemplate.remove(new Query(Criteria.where("name").is(name)), Devices.class, Variables.col_devices);
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteByUsername(String username) {

        Devices device = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), Devices.class, Variables.col_devices);
        if (device == null)
            return HttpStatus.FORBIDDEN;

        try {
            mongoTemplate.findAndRemove(new Query(Criteria.where("username").is(username)), Devices.class, Variables.col_devices);
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteByUsernameAndDeviceName(String username, String deviceName) {
        Query query = new Query(Criteria.where("username").is(username).and("name").is(deviceName));

        Devices device = mongoTemplate.findOne(query, Devices.class, Variables.col_devices);
        if (device == null)
            return HttpStatus.FORBIDDEN;

        try {
            mongoTemplate.findAndRemove(query, Devices.class, Variables.col_devices);
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }
}
