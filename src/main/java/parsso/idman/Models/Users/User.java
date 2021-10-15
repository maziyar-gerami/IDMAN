package parsso.idman.Models.Users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Models.License.License;
import parsso.idman.Models.other.SkyRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class User implements UserDetails, Comparable {
	private static final String PREFIX = "ROLE_";
	@JsonIgnore
	ObjectId _id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	SkyRoom skyRoom;
	Boolean skyroomAccess;
	private String userId;
	private String firstName;
	private String lastName;
	private String displayName;
	private String mobile;
	@JsonIgnore
	private long timeStamp;
	@JsonIgnore
	private long passwordChangedTime;
	@JsonIgnore
	private boolean locked;
	@JsonIgnore
	private boolean enabled;
	private String mail;
	private String description;
	private List<String> memberOf;
	@JsonIgnore
	private String exportMemberOf;
	private String userPassword;
	@JsonIgnore
	private String photo;
	private String role;
	private String employeeNumber;
	@JsonProperty
	private String status;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String endTime;
	@JsonIgnore
	private String exportEndTime;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String cStatus;
	@JsonIgnore
	private UsersExtraInfo usersExtraInfo;
	private boolean unDeletable;
	private boolean profileInaccessibility;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private License services;

	public User() {
		locked = false;
		enabled = true;
	}

	public String getExportEndTime() {
		return TimeHelper.getExportEndTime(getEndTime());
	}

	public String getExportMemberOf() {
		if (getMemberOf() != null && getMemberOf().size() != 0) {
			StringBuilder groups = new StringBuilder();
			for (String group : getMemberOf()) {
				if (!groups.toString().equals(""))
					groups.append(", ");
				groups.append(group);
			}
			return groups.toString();
		}
		return "";
	}

	public String getStatus() {
		if (this.status != null)
			return this.status;

		if (this.isEnabled() && !this.isLocked())
			return "enable";
		if (this.isLocked())
			return "lock";
		if (!this.isEnabled())
			return "disable";

		return null;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();

		if (this.getUserId().equalsIgnoreCase("su"))
			list.add(new SimpleGrantedAuthority(PREFIX + "SUPERUSER"));

		else
			list.add(new SimpleGrantedAuthority(PREFIX + this.getUsersExtraInfo().getRole()));

		return list;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				'}';
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return null;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {

		final User other = (User) obj;

		return this.getUserId().equals(other.getUserId());
	}

	@Override
	public int compareTo(Object second) {
		return Long.compare(((User) second).timeStamp, this.timeStamp);
	}
}



