package parsso.idman.RepoImpls;


import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.PresentFilter;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.User.DashboardData;
import parsso.idman.Helpers.User.SimpleUserAttributeMapper;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.Helpers.Variables;
import parsso.idman.IdmanApplication;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.SystemRefresh;
import parsso.idman.Repos.UserRepo;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Service
public class SystemRefreshRepoImpl implements SystemRefresh {


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

    @Autowired
    DashboardData dashboardData;
    String model = "Refresh";
    String userExtraInfoCollection = Variables.col_usersExtraInfo;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Override
    public HttpStatus userRefresh(String doer) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        Logger logger = LoggerFactory.getLogger(doer);

        Logger loggerSecond = LoggerFactory.getLogger("System");


        //0. create collection, if not exist

        if (mongoTemplate.getCollection(userExtraInfoCollection) == null)
            mongoTemplate.createCollection(userExtraInfoCollection);

        loggerSecond.warn("******* User refresh started: Step 1 *******");

        UsersExtraInfo userExtraInfo;

        //1. create documents
        for (User user : userRepo.retrieveUsersFull()) {

            try {

                Query queryMongo = new Query(new Criteria("userId").regex(user.getUserId(), "i"));

                userExtraInfo = mongoTemplate.findOne(queryMongo, UsersExtraInfo.class, userExtraInfoCollection);

                if (userExtraInfo != null) {

                    if (userExtraInfo.getQrToken() == null || userExtraInfo.getQrToken().equals(""))
                        userExtraInfo.setQrToken(UUID.randomUUID().toString());


                    String photoName = ldapTemplate.search(
                            "ou=People," + BASE_DN, new EqualsFilter("uid", user.getUserId()).encode(), searchControls,
                            new AttributesMapper<String>() {
                                public String mapFromAttributes(Attributes attrs)
                                        throws NamingException {
                                    if (attrs.get("photoName") != null)
                                        return attrs.get("photoName").get().toString();

                                    return "";
                                }
                            }).get(0);

                    if (photoName != null)
                        userExtraInfo.setPhotoName(photoName);

                } else {

                    userExtraInfo.setUserId(user.getUserId());
                    userExtraInfo = new UsersExtraInfo();
                    userExtraInfo.setQrToken(UUID.randomUUID().toString());
                }

                if (userExtraInfo != null) {
                    if (userExtraInfo.getRole() == null)
                        userExtraInfo.setRole("USER");

                    else if (userExtraInfo.getUserId() != null && userExtraInfo.getUserId().equalsIgnoreCase("su"))
                        userExtraInfo.setRole("SUPERADMIN");

                    else if (userExtraInfo.getRole() != null)
                        userExtraInfo.setRole(userExtraInfo.getRole());

                    userExtraInfo.setUnDeletable(userExtraInfo.isUnDeletable());

                }
            } catch (Exception e) {
                userExtraInfo = new UsersExtraInfo(user.getUserId());
            }

            userExtraInfo.setDisplayName(user.getDisplayName());

            userExtraInfo.setMemberOf(user.getMemberOf());

            userExtraInfo.setStatus(user.getStatus());

            userExtraInfo.setPasswordChangedTime(user.getPasswordChangedTime());

            userExtraInfo.setCreationTimeStamp(user.getTimeStamp());

            try {

                mongoTemplate.save(userExtraInfo, userExtraInfoCollection);
                logger.warn(new ReportMessage("User", user.getUserId(), "", "refresh", "success", "Step 1: creating documents").toString());
            } catch (Exception e) {
                logger.warn(new ReportMessage("User", user.getUserId(), "", "refresh", "failed", "writing to mongo").toString());
            }


        }

        try {
            dashboardData.updateDashboardData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loggerSecond.warn("******* User refresh  Step 1 finished. To be continue... *******");


        loggerSecond.warn("******* User refresh started: Step 2 *******");


        //2. cleanUp mongo
        List<UsersExtraInfo> usersMongo = mongoTemplate.findAll(UsersExtraInfo.class, userExtraInfoCollection);
        if (usersMongo != null)
            for (UsersExtraInfo usersExtraInfo : usersMongo) {
                List<UsersExtraInfo> usersExtraInfoList = ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", usersExtraInfo.getUserId()).encode(), searchControls, simpleUserAttributeMapper);
                if (usersExtraInfoList.size() == 0) {
                    mongoTemplate.findAndRemove(new Query(new Criteria("userId").is(usersExtraInfo.getUserId())), UsersExtraInfo.class, userExtraInfoCollection);
                    logger.warn(new ReportMessage("MongoDB Document", usersExtraInfo.getUserId(), "", "remove ", "success", "Step 2: removing extra document").toString());
                }
            }

        loggerSecond.warn("******* User refresh  Step 2 finished *******");

        logger.warn(new ReportMessage(model, "", "Users", "refresh", "success", "").toString());


        return HttpStatus.OK;
    }

    @Override
    public HttpStatus captchaRefresh(String doer) {

        Logger logger = LoggerFactory.getLogger(IdmanApplication.class);

        logger.warn("Captcha refresh started");
        if (mongoTemplate.getCollection(Variables.col_captchas) != null) {
            mongoTemplate.getCollection(Variables.col_captchas).drop();
            logger.warn("IDMAN_captchas collection dropped");
        }

        mongoTemplate.createCollection(Variables.col_captchas);
        logger.warn(new ReportMessage(model, "", "Captcha", "refresh", "success", "").toString());

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus serivceRefresh(String doer) throws IOException, ParseException {
        Logger logger = LoggerFactory.getLogger(doer);

        if (mongoTemplate.getCollection(Variables.col_servicesExtraInfo) == null)
            mongoTemplate.createCollection(Variables.col_servicesExtraInfo);
        int i = 1;

        for (parsso.idman.Models.Services.Service service : serviceRepo.listServicesFull()) {
            Query query = new Query(Criteria.where("_id").is(service.getId()));
            MicroService serviceExtraInfo = mongoTemplate.findOne(query, MicroService.class, Variables.col_servicesExtraInfo);
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

            mongoTemplate.save(serviceExtraInfo, Variables.col_servicesExtraInfo);

            logger.warn(new ReportMessage(model, "", "Services", "refresh", "success", "").toString());
        }

        List<parsso.idman.Models.Services.Service> serviceList = serviceRepo.listServicesFull();
        List<Long> ids = new LinkedList<>();


        List<MicroService> microServices = mongoTemplate.findAll(MicroService.class, Variables.col_servicesExtraInfo);

        for (MicroService microService : microServices) ids.add(microService.get_id());

        for (parsso.idman.Models.Services.Service service : serviceList)
            ids.remove(service.getId());

        Query query;
        for (Long id : ids) {
            query = new Query(Criteria.where("_id").is(id));
            mongoTemplate.findAndRemove(query, MicroService.class, Variables.col_servicesExtraInfo);

        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus all(String doer) throws IOException, ParseException {
        Logger logger = LoggerFactory.getLogger(doer);
        try {
            mongoTemplate.getCollection("IDMAN_Tokens").drop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        captchaRefresh(doer);

        serivceRefresh(doer);

        userRefresh(doer);

        logger.warn(new ReportMessage(model, "", "System", "refresh", "success", "").toString());

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus refreshLockedUsers() {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        AndFilter andFilter = new AndFilter();

        Logger logger = LoggerFactory.getLogger("System");
        andFilter.and(new PresentFilter("pwdAccountLockedTime"));
        andFilter.and(new NotFilter(new EqualsFilter("pwdAccountLockedTime", "40400404040404.950Z")));

        List<User> users = ldapTemplate.search(BASE_DN, andFilter.encode(), userAttributeMapper);
        for (User user : users) {
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            UsersExtraInfo simpleUser = mongoTemplate.findOne(query, UsersExtraInfo.class, userExtraInfoCollection);
            if (!simpleUser.getStatus().equalsIgnoreCase("lock")) {
                simpleUser.setStatus("lock");

                logger.warn(new ReportMessage("User", user.getUserId(), "", "locked", "", "").toString());
                mongoTemplate.remove(query, UsersExtraInfo.class, userExtraInfoCollection);
                mongoTemplate.save(simpleUser, userExtraInfoCollection);
            }

        }

        List<UsersExtraInfo> simpleUsers = mongoTemplate.find(new Query(Criteria.where("status").is("lock")), UsersExtraInfo.class, userExtraInfoCollection);
        for (UsersExtraInfo simple : simpleUsers) {
            Query query = new Query(Criteria.where("userId").is(simple.getUserId()));

            if (ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", simple.getUserId()).encode(), searchControls, userAttributeMapper).size() == 0) {

                simple.setStatus("enable");

                mongoTemplate.remove(query, UsersExtraInfo.class, userExtraInfoCollection);
                mongoTemplate.save(simple, userExtraInfoCollection);

                logger.warn(new ReportMessage("User", simple.getUserId(), "", "unlock", "", "due to time pass").toString());


            }


        }


        return HttpStatus.OK;
    }
}
