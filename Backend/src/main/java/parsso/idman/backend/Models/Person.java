package parsso.idman.backend.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Person implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String telephoneNumber;
    private String mail;
    private String nid;
    private int memberOf;
    private String userPassword;
    private String description;

    @Repository
    public interface PersonRepository extends CrudRepository<Person, String> {}
}
