package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Person implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String mobile;
    private String mail;
    private List<String> memberOf;
    private String userPassword;
    private String description;
    private String token;
    private byte[] photo;

    @Repository
    public interface PersonRepository extends CrudRepository<Person, String> {}
}
