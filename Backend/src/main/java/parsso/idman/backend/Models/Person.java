package parsso.idman.backend.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class Person {
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String telephoneNumber;
    private String mail;
    private String nid;
    private String memberOf;
    private String userPassword;
    private String description;
}
