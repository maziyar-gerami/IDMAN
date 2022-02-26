package parsso.idman.repoImpls.users.usersOprations.retrieve.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;

import java.util.Objects;

public class SkyroomAccess {
    final MongoTemplate mongoTemplate;

    public SkyroomAccess(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Boolean get(User user) {
        boolean isEnable = Boolean.parseBoolean((new Settings(mongoTemplate).retrieve(Variables.SKYROOM_ENABLE)).getValue());

        boolean accessRole;
        try {
            if (user.getUsersExtraInfo() == null) {
                //noinspection ConstantConditions
                Objects.requireNonNull(user.getUsersExtraInfo());
            }
            accessRole = true;
        } catch (Exception e) {
            accessRole = false;
        }

        return isEnable & accessRole;
    }

}
