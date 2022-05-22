package parsso.idman.impls.logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.LicensedAndUnlicensed;
import parsso.idman.impls.groups.RetrieveGroup;
import parsso.idman.models.license.License;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Service
@SuppressWarnings({ "unchecked" })
public class RetrieveTranscripts implements LogsRepo.TranscriptRepo {

  final ServiceRepo.Retrieve serviceRepo;
  final MongoTemplate mongoTemplate;

  @Autowired
  public RetrieveTranscripts(ServiceRepo.Retrieve serviceRepo,
      MongoTemplate mongoTemplate,
      RetrieveGroup retrieveGroup) {
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;

  }

  @Override
  public License servicesOfUser(String userId) {
    List<MicroService> licensed = new LinkedList<>();
    List<MicroService> unLicensed = new LinkedList<>();

    List<Service> allServices = serviceRepo.listServicesFull();

    UsersExtraInfo user = mongoTemplate.findOne(new Query(Criteria.where("_id").is(userId)), UsersExtraInfo.class,
        Variables.col_usersExtraInfo);

    List<String> memberOf;
    try {
      memberOf = Objects.requireNonNull(user).getMemberOf();

    } catch (NullPointerException e) {
      memberOf = null;
    }

    for (Service service : allServices) {

      LicensedAndUnlicensed extract = new LicensedAndUnlicensed();

      licensed = extract.licensedServicesForGroups(user, licensed, service);

      licensed = extract.licensedServicesForUserID(userId, licensed, service);

      List<List<MicroService>> temp = extract.unLicensedServicesForUserID(userId, licensed, unLicensed, service);

      licensed = temp.get(0);
      unLicensed = temp.get(1);

      temp = extract.licensedServiceWithGroupID(licensed, unLicensed, service, memberOf);

      licensed = temp.get(0);
      unLicensed = temp.get(1);

    }

    return new License(licensed, unLicensed);
  }

}
