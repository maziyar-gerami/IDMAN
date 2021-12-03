package parsso.idman.models.other;

import org.bson.types.ObjectId;

public class Token {
    ObjectId _id;
    String username;
    String token;

    public Token(String userName, String token) {
        this.username = userName;
        this.token = token;
    }
}
