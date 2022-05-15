package parsso.idman.helpers.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import parsso.idman.helpers.Variables;
import parsso.idman.models.services.serviceType.MicroService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({ "SameReturnValue", "unchecked" })
public class Position {

  private final MongoTemplate mongoTemplate;

  public Position(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public int lastPosition() {
    Query query = new Query().with(Sort.by(Sort.Direction.DESC, "position")).limit(1);
    return mongoTemplate.findOne(query, MicroService.class, Variables.col_servicesExtraInfo).getPosition();
  }

  public HttpStatus increase(String id) {
    Query query = new Query(Criteria.where("_id").is(Long.valueOf(id)));
    MicroService ms = mongoTemplate.findOne(query, MicroService.class, Variables.col_servicesExtraInfo);
    int position = Objects.requireNonNull(ms).getPosition();
    List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, Variables.col_servicesExtraInfo);
    if (position != microservices.size()) {
      MicroService ms1 = searchByPosition(microservices, position + 1);
      ms1.setPosition(position);
      mongoTemplate.save(ms1, Variables.col_servicesExtraInfo);
      ms.setPosition(position + 1);
      mongoTemplate.save(ms, Variables.col_servicesExtraInfo);

    } else
      return HttpStatus.FORBIDDEN;
    return HttpStatus.OK;
  }

  public HttpStatus decrease(String id) {
    Query query = new Query(Criteria.where("_id").is(Long.valueOf(id)));
    MicroService ms = mongoTemplate.findOne(query, MicroService.class, Variables.col_servicesExtraInfo);
    int position = Objects.requireNonNull(ms).getPosition();
    List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, Variables.col_servicesExtraInfo);
    if (position != 1) {
      MicroService ms1 = searchByPosition(microservices, position);
      MicroService ms2 = searchByPosition(microservices, position - 1);
      ms1.setPosition(position - 1);
      ms2.setPosition(position);
      mongoTemplate.save(ms1, Variables.col_servicesExtraInfo);
      mongoTemplate.save(ms2, Variables.col_servicesExtraInfo);
    } else
      return HttpStatus.FORBIDDEN;
    return HttpStatus.OK;
  }

  public MicroService searchByPosition(List<MicroService> microServices, int position) {
    for (MicroService microService : microServices) {
      if (microService.getPosition() == position)
        return microService;
    }
    return null;
  }

  public void delete(int position) {
    List<MicroService> microservices = mongoTemplate.findAll(MicroService.class, Variables.col_servicesExtraInfo);
    Collections.sort(microservices);
    Collections.reverse(microservices);

    for (int i = position; i < microservices.size(); i++) {
      microservices.get(i).setPosition(i);
      mongoTemplate.save(microservices.get(i), Variables.col_servicesExtraInfo);

    }

  }
}
