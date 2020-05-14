package parsso.idman.backend.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
<<<<<<< HEAD
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
=======
import org.springframework.http.HttpStatus;
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44

@Getter
@Setter
@ToString
<<<<<<< HEAD
public class Person implements Serializable {
=======
public class Person {
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String telephoneNumber;
    private String mail;
    private String nid;
<<<<<<< HEAD
    private int memberOf;
    private String userPassword;
    private String description;

    @Repository
    public interface PersonRepository extends CrudRepository<Person, String> {}
=======
    private String memberOf;
    private String userPassword;
    private String description;
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
}
