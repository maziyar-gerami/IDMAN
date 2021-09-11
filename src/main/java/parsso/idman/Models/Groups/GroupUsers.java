package parsso.idman.Models.Groups;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GroupUsers {
	List<String> add;
	List<String> remove;
}
