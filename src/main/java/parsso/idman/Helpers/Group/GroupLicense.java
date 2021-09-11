package parsso.idman.Helpers.Group;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class GroupLicense {
	@Autowired
	GroupRepo groupRepo;
	@Autowired
	ServiceRepo serviceRepo;

	public List<Group> licensedGroups(long serviceId) throws IOException, ParseException {
		List<Group> groups = new LinkedList<>();

		parsso.idman.Models.Services.Service service = serviceRepo.retrieveService(serviceId);
		JSONArray jsonArray = (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1);
		for (Object name : jsonArray)
			try {
				groups.add(groupRepo.retrieveOu(true, name.toString()));
			} catch (NullPointerException e) {
			}
		return groups;
	}

	public Groups groups(long serviceId) throws IOException, ParseException {

		return new Groups(licensedGroups(serviceId), null);
	}

	@Setter
	@Getter
	private class Groups {
		@JsonInclude(JsonInclude.Include.NON_NULL)

		List licensed;
		@JsonInclude(JsonInclude.Include.NON_NULL)

		List unLicensed;

		Groups(List licensed, List unLicensed) {
			this.licensed = licensed;
			this.unLicensed = unLicensed;
		}

	}

}
