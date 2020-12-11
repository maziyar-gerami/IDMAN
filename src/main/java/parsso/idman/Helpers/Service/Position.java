package parsso.idman.Helpers.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.ServiceType.MicroService;

import java.util.List;

@Service
public class Position {
    @Autowired
    MongoTemplate mongoTemplate;

    String collection = "IDMAN_ServicesExtraInfo";

    public int lastPosition(){
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class,collection);
        int maxPosition=0;
        for (MicroService microservice:microservices) {
            if(microservice.getPosition()>maxPosition)
                maxPosition = microservice.getPosition();

        }
        return maxPosition;
    }

    public HttpStatus increase(int position){
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class,collection);
        for (int i=0; i< microservices.size(); i++) {
            if (microservices.get(i).getPosition()==position){
                if (i!=microservices.size()){
                microservices.get(i+1).setPosition(position-1);
                microservices.get(i).setPosition(position+1);
                    return HttpStatus.OK;

                }else
                    return HttpStatus.FORBIDDEN;
            }else
                return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.FORBIDDEN;
    }
    public HttpStatus decrease(int position){
        List<MicroService> microservices = mongoTemplate.findAll(MicroService.class,collection);
        for (int i=0; i< microservices.size(); i++) {
            if (microservices.get(i).getPosition()==position){
                if (i!=microservices.size()){
                    microservices.get(i-1).setPosition(position+1);
                    microservices.get(i).setPosition(position-1);
                    return org.springframework.http.HttpStatus.OK;

                }else
                    return HttpStatus.FORBIDDEN;
            }else
                return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.FORBIDDEN;

    }
}
