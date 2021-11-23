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
    MongoTemplate mongoTemplate;

    @Autowired
    AuthenticatorRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Devices.DeviceList retrieve(String username, String deviceName, int page, int count) {
        Query query = new Query();
        int skip =  (page - 1) *count;

        if(!username.equals(""))
            query.addCriteria(Criteria.where("username").regex(".*"+username+".*", "i"));

        if (!deviceName.equals(""))
            query.addCriteria(Criteria.where("name").regex(".*"+deviceName+".*", "i"));

        if (page!=0 && count != 0){
            query.skip(skip).limit(count);
        }
        List<Devices> devicesList = mongoTemplate.find(query,Devices.class, Variables.col_devices);
        long size = mongoTemplate.count(new Query(),Variables.col_devices);
        int pages;
        if (count!=0)
            pages = (int) Math.ceil(size / (float) count);
        else
            pages =1;
        return new Devices.DeviceList(devicesList,size, pages);
    }

    @Override
    public HttpStatus deleteByDeviceName(String name) {
        try {
            mongoTemplate.remove(new Query(Criteria.where("name").is(name)));
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteByUsername(String username) {
        try {
            mongoTemplate.remove(new Query(Criteria.where("username").is(username)));
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }
}
