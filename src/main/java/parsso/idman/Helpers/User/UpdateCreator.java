package parsso.idman.Helpers.User;


import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class UpdateCreator {
    Update update = new Update();

    public Update extraInfo(String photoName, boolean undDeletable) {
        update.set("photoName", photoName);
        update.set("unDeletable", undDeletable);
        return update;
    }

    public Update simpleUser(String status, List<String> memberOf) {

        update.set("status", status);
        update.set("memberOf", memberOf);

        return update;

    }
}
