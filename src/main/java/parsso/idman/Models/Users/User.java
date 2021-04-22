package parsso.idman.Models.Users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.client.MongoClients;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.odm.annotations.*;
import org.springframework.ldap.support.LdapNameBuilder;
import parsso.idman.Helpers.User.BuildDnUser;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.Models.Services.ServiceType.MicroService;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


@Setter
@Getter
@Entry(objectClasses = { "inetOrgPerson", "organizationalPerson", "person", "top" })
public final class User implements Comparable {

    private static final String PREFIX = "ROLE_";

    @JsonIgnore
    ObjectId _id;

    @Id
    @JsonIgnore
    private LdapName dn;

    //@Attribute(name = "cn")
    //@DnAttribute(value="cn", index=2)
    //private String commonName;

    @Attribute(name = "uid")
    private String userId;

    @Attribute(name = "givenName")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "displayName")
    private String displayName;

    @Attribute(name = "mobile")
    private String mobile;

    @JsonIgnore
    private long timeStamp;

    @JsonIgnore
    private long passwordChangedTime;

    @JsonIgnore
    private boolean locked;

    @JsonIgnore
    private boolean enabled;

    @Attribute (name = "mail")
    private String mail;

    @Attribute (name = "description")
    private String description;

    @Attribute(name="ou")
    private List<String> memberOf;



    @Attribute(name="userPassword")
    private String userPassword;

    @JsonIgnore
    private String photo;
    private String role;
    @Attribute(name = "employeeNumber")
    private String employeeNumber;

    @JsonProperty
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Attribute(name = "pwdEndTime")
    private String endTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cStatus;

    @JsonIgnore
    private UsersExtraInfo usersExtraInfo;

    private boolean unDeletable;

    public UsersExtraInfo getUsersExtraInfo(){

        Query query = new Query(Criteria.where("userId").is(this.userId));
        String collection1 = "IDMAN_UsersExtraInfo";

        String mongoURI = "mongodb://" + "parssouser:APA00918" + "@" + "parsso2.razi.ac.ir:27017" + "/" + "parssodb";
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(mongoURI), "parssodb");

        return   mongoTemplate.findOne(query, UsersExtraInfo.class, collection1);

    }


    public String getStatus() {
        return this.getUsersExtraInfo().getStatus();
    }

    public String getPhoto() {
        return this.getUsersExtraInfo().getPhotoName();
    }

    public String getRole() {
        return this.getUsersExtraInfo().getRole();
    }

    public boolean isUnDeletable() {
        return this.getUsersExtraInfo().isUnDeletable();
    }

    @Override
    public int compareTo(Object second) {
        if (this.timeStamp > ((User) second).timeStamp)
            return -1;
        else if (this.timeStamp < ((User) second).timeStamp)
            return 1;
        else
            return 0;
    }
}
