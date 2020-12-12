package parsso.idman.Helpers.Service;


import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.ServiceType.MicroService;

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

    public HttpStatus increase(int position) {
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        for (int i = 0; i < microservices.size() - 1; i++) {
            if (microservices.get(i).getPosition() == position) {
                microservices.get(i + 1).setPosition(position - 1);
                microservices.get(i).setPosition(position + 1);

            } else
                return HttpStatus.FORBIDDEN;
        }
        mongoTemplate.save(microservices,collection);
        return HttpStatus.OK;
    }

    public HttpStatus decrease(int position) {
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, collection);
        for (int i = 1; i < microservices.size(); i++) {
            if (microservices.get(i).getPosition() == position) {
                microservices.get(i - 1).setPosition(position + 1);
                microservices.get(i).setPosition(position - 1);

            } else
                return HttpStatus.FORBIDDEN;
        }
        mongoTemplate.save(microservices,collection);
        return HttpStatus.OK;

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
