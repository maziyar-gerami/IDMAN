package parsso.idman.impls.refresh.subclass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.serviceType.SimpleService;

public class ServiceRefresh {
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  public ServiceRefresh(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public HttpStatus refresh(String doer) {

    mongoTemplate.getCollection(Variables.col_servicesExtraInfo);
    int i = 1;

    for (parsso.idman.models.services.Service service : new RetrieveService(mongoTemplate).listServicesFull()) {
      Query query = new Query(Criteria.where("_id").is(service.getId()));
      SimpleService serviceExtraInfo = mongoTemplate.findOne(query, SimpleService.class,
          Variables.col_servicesExtraInfo);
          SimpleService newServiceExtraInfo = new SimpleService();

      String tempUrl;

      if (serviceExtraInfo != null && serviceExtraInfo.getUrl() != null) {
        tempUrl = serviceExtraInfo.getUrl();
      } else
        tempUrl = service.getServiceId();

      try {
        URL url = new URL(tempUrl);
        newServiceExtraInfo.setUrl(new URL(tempUrl).getProtocol() + "://" + url.getAuthority());
      } catch (MalformedURLException e) {
        newServiceExtraInfo.setUrl("www.example.com");
      }
      newServiceExtraInfo.set_id(service.getId());
      serviceExtraInfo = newServiceExtraInfo;

      serviceExtraInfo.set_id(service.getId());

      serviceExtraInfo.setPosition(i++);

      serviceExtraInfo.setDescription(service.getDescription());

      serviceExtraInfo.setName(service.getName());

      serviceExtraInfo.setServiceId(service.getServiceId());

      mongoTemplate.save(serviceExtraInfo, Variables.col_servicesExtraInfo);

      uniformLogger.info(doer, new ReportMessage(Variables.MODEL_SERVICE, "", "", Variables.ACTION_REFRESH,
          Variables.RESULT_SUCCESS, ""));
    }

    List<parsso.idman.models.services.Service> serviceList = new RetrieveService(mongoTemplate).listServicesFull();
    List<Long> ids = new LinkedList<>();

    List<SimpleService> microServices = mongoTemplate.findAll(SimpleService.class, Variables.col_servicesExtraInfo);

    for (SimpleService microService : microServices)
      ids.add(microService.get_id());

    for (parsso.idman.models.services.Service service : serviceList)
      ids.remove(service.getId());

    Query query;
    for (Long id : ids) {
      query = new Query(Criteria.where("_id").is(id));
      mongoTemplate.findAndRemove(query, SimpleService.class, Variables.col_servicesExtraInfo);

    }

    return HttpStatus.OK;
  }

}
