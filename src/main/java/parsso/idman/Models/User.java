package parsso.idman.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class User implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String mobile;
    private String mail;
    private List<String> memberOf;
    @JsonIgnore
    private String userPassword;
    private String description;
    private String resetPassToken;
    private String photoName;
    private String status;
    @JsonIgnore
    private String mobileToken;
    @JsonIgnore
    private String qrToken;

    @Repository
    public interface UserRepository extends CrudRepository<User, String> {}
}

