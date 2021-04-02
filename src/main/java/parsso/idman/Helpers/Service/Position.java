package parsso.idman.Helpers.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;

import java.util.Collections;
import java.util.List;

@Service
public class Position {
    @Autowired
    MongoTemplate mongoTemplate;

    String collection = "IDMAN_ServicesExtraInfo";

    public int lastPosition() {
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        int maxPosition = 0;
        for (MicroService microservice : microservices) {
            if (microservice.getPosition() > maxPosition)
                maxPosition = microservice.getPosition();

        }
        return maxPosition;
    }

    public HttpStatus increase(String id) {
        Query query = new Query(Criteria.where("_id").is(Long.valueOf(id)));
        MicroService ms = mongoTemplate.findOne(query, MicroService.class, collection);
        int position = ms.getPosition();
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        if(position!=microservices.size()) {
            MicroService ms1 = searchByPosition(microservices,position+1);
            ms1.setPosition(position);
            mongoTemplate.save(ms1,collection);
            ms.setPosition(position+1);
            mongoTemplate.save(ms,collection);

        }else
            return HttpStatus.FORBIDDEN;
        return HttpStatus.OK;
    }

    public HttpStatus decrease(String id) {
        Query query = new Query(Criteria.where("_id").is(Long.valueOf(id)));
        MicroService ms = mongoTemplate.findOne(query, MicroService.class, collection);
        int position = ms.getPosition();
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        if(position!=1) {
            MicroService ms1 = searchByPosition(microservices,position);
            MicroService ms2 = searchByPosition(microservices,position-1);
            ms1.setPosition(position-1);
            ms2.setPosition(position);
            mongoTemplate.save(ms1,collection);
            mongoTemplate.save(ms2,collection);
        }else
            return HttpStatus.FORBIDDEN;
        return HttpStatus.OK;
    }

    public MicroService searchByPosition(List<MicroService> microServices, int position){
        for (MicroService microService:microServices) {
            if(microService.getPosition()==position)
                return microService;
        }
        return  null;
    }

    public  HttpStatus delete(int position){
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        Collections.sort(microservices);
        Collections.reverse(microservices);

        for (int i=position; i< microservices.size(); i++) {
            microservices.get(i).setPosition(i);
            mongoTemplate.save(microservices.get(i),collection);

        }

        return  HttpStatus.OK;

    }
}
