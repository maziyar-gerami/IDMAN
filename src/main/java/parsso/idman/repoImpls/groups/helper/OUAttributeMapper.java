package parsso.idman.repoImpls.groups.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.AttributesMapper;
import parsso.idman.helpers.Variables;
import parsso.idman.models.groups.Group;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class OUAttributeMapper implements AttributesMapper<Group> {
    final MongoTemplate mongoTemplate;

    public OUAttributeMapper(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Group mapFromAttributes(Attributes attributes) throws NamingException {
        Group group = new Group();

        group.setId(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
        group.setName(null != attributes.get("name") ? attributes.get("name").get().toString() : null);
        group.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
        group.setUsersCount(mongoTemplate.count(new Query(Criteria.where("memberOf").is(attributes.get("ou").get().toString())), Variables.col_usersExtraInfo));
        return group;
    }
}
