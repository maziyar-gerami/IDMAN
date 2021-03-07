package parsso.idman.RepoImpls;


import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.User.SimpleUserAttributeMapper;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.IdmanApplication;
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.UsersExtraInfo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.SystemRefresh;
import parsso.idman.Repos.UserRepo;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


@Service
public class SystemRefreshRepoImpl implements SystemRefresh {

    final Logger logger = LoggerFactory.getLogger(IdmanApplication.class);

    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserAttributeMapper userAttributeMapper;

    @Autowired
    SimpleUserAttributeMapper simpleUserAttributeMapper;

    @Override
    public HttpStatus userRefresh() throws IOException {
        //0. crete collection, if not exist


        if (mongoTemplate.getCollection("IDMAN_UsersExtraInfo") == null)
            mongoTemplate.createCollection("IDMAN_UsersExtraInfo");

        //1. create documents
        for (SimpleUser user : userRepo.retrieveUsersMain()) {
            Query queryMongo = new Query(new Criteria("userId").is(user.getUserId()));
            if (mongoTemplate.findOne(queryMongo, UsersExtraInfo.class, "IDMAN_UsersExtraInfo") != null) {

                UsersExtraInfo userExtraInfo = mongoTemplate.findOne(queryMongo, UsersExtraInfo.class, "IDMAN_UsersExtraInfo");
                if (userExtraInfo!=null && userExtraInfo.getQrToken().equals(""))
                    userExtraInfo.setQrToken(UUID.randomUUID().toString());

                String photoName = ldapTemplate.search(
                        query().where("uid").is(user.getUserId()),
                        new AttributesMapper<String>() {
                            public String mapFromAttributes(Attributes attrs)
                                    throws NamingException {
                                if(attrs.get("photoName")!=null)
                                    return attrs.get("photoName").get().toString();

                                return "";
                            }
                        }).get(0);

                userExtraInfo.setPhotoName(photoName);

                if (userExtraInfo!=null)
                    mongoTemplate.save(userExtraInfo, "IDMAN_UsersExtraInfo");

            } else {
                UsersExtraInfo userExtraInfo = new UsersExtraInfo();

                userExtraInfo.setUserId(user.getUserId());
                userExtraInfo.setQrToken(UUID.randomUUID().toString());

                mongoTemplate.save(userExtraInfo, "IDMAN_UsersExtraInfo");
            }

        }

        //2. cleanUp mongo
        List<SimpleUser> usersMongo = mongoTemplate.findAll(SimpleUser.class, "IDMAN_UsersExtraInfo");
        for (SimpleUser simpleUser : usersMongo) {
            if (ldapTemplate.search(query().where("uid").is(simpleUser.getUserId()), simpleUserAttributeMapper).size()==0)
                mongoTemplate.findAndRemove(new Query(new Criteria("userId").is(simpleUser.getUserId())), UsersExtraInfo.class, "IDMAN_UsersExtraInfo");
        }

        return  HttpStatus.OK;
    }

    @Override
    public HttpStatus captchaRefresh() {

        logger.info("Captcha refresh started");
        if (mongoTemplate.getCollection("IDMAN_Captchas") != null) {
            mongoTemplate.getCollection("IDMAN_Captchas").drop();
            logger.info("IDMAN_captchas collection dropped");
        }

        mongoTemplate.createCollection("IDMAN_Captchas");
        logger.info("IDMAN_captchas collection created");

        logger.info("IDMAN_captchas collection refreshed");

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus serivceRefresh() throws IOException, ParseException {
        if (mongoTemplate.getCollection("IDMAN_ServicesExtraInfo") == null)
            mongoTemplate.createCollection("IDMAN_ServicesExtraInfo");
        int i = 1;

        for (parsso.idman.Models.Service service : serviceRepo.listServicesFull()) {
            Query query = new Query(Criteria.where("_id").is(service.getId()));
            MicroService serviceExtraInfo = mongoTemplate.findOne(query, MicroService.class, "IDMAN_ServicesExtraInfo");
            MicroService newServiceExtraInfo = new MicroService();

            if (serviceExtraInfo == null) {
                newServiceExtraInfo.setUrl(service.getServiceId());
                newServiceExtraInfo.set_id(service.getId());
                serviceExtraInfo = newServiceExtraInfo;
            } else
                serviceExtraInfo.set_id(service.getId());
            if (serviceExtraInfo.getUrl() == null)
                serviceExtraInfo.setUrl(service.getServiceId());

            serviceExtraInfo.setPosition(i++);

            mongoTemplate.save(serviceExtraInfo, "IDMAN_ServicesExtraInfo");
        }

        List<parsso.idman.Models.Service> serviceList = serviceRepo.listServicesFull();
        List<Long> ids = new LinkedList<>();


        List<MicroService> microServices = mongoTemplate.findAll(MicroService.class, "IDMAN_ServicesExtraInfo");

        for (MicroService microService : microServices) ids.add(microService.get_id());

        for (parsso.idman.Models.Service service : serviceList)
            ids.remove(service.getId());

        Query query;
        for (Long id:ids) {
            query = new Query(Criteria.where("_id").is(id));
            mongoTemplate.findAndRemove(query,MicroService.class,"IDMAN_ServicesExtraInfo");

        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus all() throws IOException, ParseException {
        try {
            mongoTemplate.getCollection("IDMAN_Tokens").drop();
        } catch (Exception e) {
            e.printStackTrace();
        }


        captchaRefresh();

        logger.info("service refresh started");
        serivceRefresh();

        logger.info("service refresh finished");

        logger.info("user refresh started");

        userRefresh();

        logger.info("user refresh finished");

        
        logger.info("System refresh finished");

        return HttpStatus.OK;
    }
}
