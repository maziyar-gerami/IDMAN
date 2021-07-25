package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.User.BuildDnUser;
import parsso.idman.Helpers.User.ExpirePassword;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.util.*;

@Service
public class GroupRepoImpl implements GroupRepo {


    private final String usersExtraInfoCollection = Variables.col_usersExtraInfo;
    private final String model = "Groups";
    @Autowired
    BuildDnUser buildDnUser;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ExpirePassword expirePassword;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ServiceRepo serviceRepo;

    @Override
    public HttpStatus remove(String doerID, JSONObject jsonObject) {

        Logger logger = LogManager.getLogger(doerID);

        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        DirContextOperations context;
        Iterator<String> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            Group group = retrieveOu(iterator.next());

            Name dn = buildDn(group.getId());
            try {
                ldapTemplate.unbind(dn);
                logger.warn(new ReportMessage(model, group.getId(), "Group", "remove", "success", "").toString());

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, group.getId(), "Group", "remove", "failed", "writing to ldap").toString());
            }


            for (UsersExtraInfo user : userRepo.retrieveGroupsUsers(group.getId())) {
                if (user != null && user.getMemberOf() != null)
                    for (String groupN : user.getMemberOf()) {
                        if (groupN.equalsIgnoreCase(group.getId())) {
                            context = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
                            context.removeAttributeValue("ou", group.getId());
                            try {
                                ldapTemplate.modifyAttributes(context);
                                UsersExtraInfo simpleUser = mongoTemplate.findOne
                                        (new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, usersExtraInfoCollection);
                                simpleUser.getMemberOf().remove(group.getId());

                                mongoTemplate.remove
                                        (new Query(Criteria.where("userId").is(user.getUserId())), usersExtraInfoCollection);

                                mongoTemplate.save
                                        (simpleUser, usersExtraInfoCollection);

                                logger.warn(new ReportMessage("User", user.getUserId(), "group " + groupN, "remove", "success", "").toString());


                            } catch (Exception e) {
                                logger.warn(new ReportMessage("User", user.getUserId(), "group " + groupN, "remove", "failed", "writing to LDAP").toString());

                            }
                        }

                    }
            }
        }

        return HttpStatus.OK;
    }


    @Override
    public Group retrieveOu(String uid) {

        List<Group> groups = retrieve();

        for (Group group : groups) {
            if (group.getId().equalsIgnoreCase(uid))
                return group;
        }
        return null;
    }

    @Override
    public List<Group> retrieveCurrentUserGroup(User user) {
        List<String> memberOf = user.getMemberOf();
        List<Group> groups = new ArrayList<Group>();
        try {
            for (int i = 0; i < memberOf.size(); ++i) {
                groups.add(retrieveOu(memberOf.get(i)));
            }
        } catch (NullPointerException e) {

        }
        return groups;
    }

    @Override
    public List<String> expireUsersGroupPassword(String doer, JSONObject jsonObject) {

        List<UsersExtraInfo> users = new LinkedList<>();
        for (String groupID : (List<String>) jsonObject.get("names"))
            users.addAll(mongoTemplate.find(new Query(Criteria.where("memberOf").is(groupID))
                    , UsersExtraInfo.class, Variables.col_usersExtraInfo));

        Set<UsersExtraInfo> set = new HashSet<>(users);
        users.clear();
        users.addAll(set);

        return expirePassword.expire(doer, users);
    }


    private Attributes buildAttributes(String uid, Group group) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("extensibleObject");
        ocattr.add("organizationalUnit");
        ocattr.add("top");
        ocattr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("name", group.getName());
        attrs.put("ou", group.getId());
        if (group.getDescription() != "")
            attrs.put("description", group.getDescription());
        else
            attrs.put("description", " ");

        return attrs;
    }

    public Name buildDn(String id) {
        return LdapNameBuilder.newInstance("ou=" + model + "," + BASE_DN).add("ou", id).build();
    }

    @Override
    public List<Group> retrieve() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "organizationalUnit"));

        return ldapTemplate.search("ou=Groups," + BASE_DN, filter.encode(),
                new OUAttributeMapper());
    }

    @Override
    public List<Group> retrieve(String ou) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        final AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "organizationalUnit"));

        List<Group> gt = ldapTemplate.search("ou=Groups," + BASE_DN, filter.encode(),
                new OUAttributeMapper());

        gt.removeIf(t -> t.getId().equals(model));

        if (gt.size() != 0)
            return null;


        return gt;

    }


    @Override
    public HttpStatus create(String doerID, Group ou) {

        Logger logger = LogManager.getLogger(doerID);

        List<Group> groups = retrieve();

        for (Group group : groups)
            if (group.getId().equals(ou.getId()))
                return HttpStatus.FOUND;

        Name dn = buildDn(ou.getId());
        try {
            ldapTemplate.bind(dn, null, buildAttributes(ou.getId(), ou));

            logger.warn(new ReportMessage(model, ou.getId(), "Group", "create", "success", "").toString());

            return HttpStatus.OK;

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(new ReportMessage(model, ou.getId(), "Group", "create", "failed", "writing to ldap").toString());

            return HttpStatus.BAD_REQUEST;
        }
    }


    @Override
    public HttpStatus update(String doerID, String id, Group ou) {

        Logger logger = LogManager.getLogger(doerID);


        Name dn = buildDn(id);

        List<Group> groups = retrieve();

        for (Group group : groups)
            if (!(id.equals(ou.getId())) && group.getId().equals(ou.getId()))
                return HttpStatus.FOUND;


        if (!(id.equals(ou.getId()))) {

            try {
                ldapTemplate.unbind(dn);
                create(doerID, ou);
                DirContextOperations contextUser;
                logger.warn(new ReportMessage(model, id, "Group", "update", "success", "").toString());


                for (UsersExtraInfo user : userRepo.retrieveGroupsUsers(id)) {
                    for (String group : user.getMemberOf()) {
                        if (group.equalsIgnoreCase(id)) {
                            contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
                            contextUser.removeAttributeValue("ou", id);
                            contextUser.addAttributeValue("ou", ou.getId());
                            ldapTemplate.modifyAttributes(contextUser);
                            UsersExtraInfo usersExtraInfo = mongoTemplate.findOne
                                    (new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, usersExtraInfoCollection);
                            usersExtraInfo.getMemberOf().remove(id);
                            usersExtraInfo.getMemberOf().add(ou.getId());

                            mongoTemplate.remove
                                    (new Query(Criteria.where("userId").is(user.getUserId())), usersExtraInfoCollection);

                            mongoTemplate.save
                                    (usersExtraInfo, usersExtraInfoCollection);
                        }
                    }
                }


                List<parsso.idman.Models.Services.Service> services = serviceRepo.listServicesWithGroups(id);
                if (services != null)
                    for (parsso.idman.Models.Services.Service service : services) {

                        //remove old id and add new id
                        ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).remove(id);
                        ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).add(ou.getId());

                        // delete old service
                        org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
                        jsonObject.put("names", ((((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))));
                        serviceRepo.deleteServices(doerID, jsonObject);

                        // create new service

                        serviceRepo.updateOuIdChange(doerID, service, service.getId(), service.getName(), id, ou.getId());

                    }

                logger.warn(new ReportMessage(model, id, "", "update", "success", "").toString());

                return HttpStatus.OK;

            } catch (IOException ioException) {

                logger.warn(new ReportMessage(model, id, "Group", "update", "failed", "ioException").toString());
                return HttpStatus.BAD_REQUEST;
            } catch (org.json.simple.parser.ParseException e) {
                logger.warn(new ReportMessage(model, id, "Group", "update", "failed", "parsing").toString());
                return HttpStatus.BAD_REQUEST;
            }

        } else {

            try {
                ldapTemplate.rebind(dn, null, buildAttributes(ou.getId(), ou));
                logger.warn(new ReportMessage(model, doerID, "Group", "update", "success", "").toString());

                return HttpStatus.OK;

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, doerID, ou.getId(), "update", "failed", "writing to ldap").toString());

                return HttpStatus.BAD_REQUEST;
            }
        }


    }


    private class OUAttributeMapper implements AttributesMapper<Group> {

        @Override
        public Group mapFromAttributes(Attributes attributes) throws NamingException {
            Group group = new Group();

            group.setId(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
            group.setName(null != attributes.get("name") ? attributes.get("name").get().toString() : null);
            group.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return group;
        }
    }
}
