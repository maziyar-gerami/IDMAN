package parsso.idman.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final String PREFIX = "ROLE_";

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
    @JsonIgnore
    private String resetPassToken;
    private String photoName;
    private String status;
    @JsonIgnore
    private String mobileToken;
    @JsonIgnore
    private String qrToken;

    @Repository
    public interface UserRepository extends CrudRepository<User, String> {}

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        if (this.memberOf==null)

            list.add(new SimpleGrantedAuthority(PREFIX + "USER"));


        else {

            if (this.memberOf.contains("1598656906150")) {
                list.add(new SimpleGrantedAuthority(PREFIX + "ADMIN"));

                list.add(new SimpleGrantedAuthority(PREFIX + "USER"));


            } else
                list.add(new SimpleGrantedAuthority(PREFIX + "USER"));

        }


        return list;
    }
}

