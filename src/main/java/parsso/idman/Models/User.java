package parsso.idman.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class User implements UserDetails {

    public User(){
        locked = false;
        enabled = true;
    }

    @Value("${administrator.ou.id}")
    private String admidId;


    private static final String PREFIX = "ROLE_";
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String mobile;
    @JsonIgnore
    private boolean locked;
    @JsonIgnore
    private boolean enabled;
    private String mail;
    private String description;

    private List<String> memberOf;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
    @JsonIgnore
    private String resetPassToken;
    private String photoName;
    @JsonIgnore
    private String mobileToken;
    private Role role;
    @JsonIgnore
    private String qrToken;
    @JsonProperty
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endTime;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cStatus;




    public String getStatus(){
        if (this.isEnabled() && !this.isLocked())
            return "active";
        if (this.isLocked())
            return "locked";
        if (!this.isEnabled())
            return "disabled";

        return null;
    }




    @Override
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

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }


}
