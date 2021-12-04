package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class Token {
    ObjectId _id;
    String username;
    String token;

    public Token(String userName, String token) {
        this.username = userName;
        this.token = token;
    }
}
