package parsso.idman.Helpers;


import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Services.ServicesSubModel.AccessStrategy;
import parsso.idman.Models.Users.Inconsistency;
import parsso.idman.Models.Users.UsersGroups;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class Comparison {
	public UsersGroups compare(AccessStrategy strategy1, AccessStrategy strategy2) {
		List<String> new_ous = null;
		List<String> new_uid = null;

		try {
			new_ous = (ArrayList<String>) ((ArrayList) strategy2.getRequiredAttributes().get("ou")).get(1);
		} catch (Exception e) {
		}
		try {
			new_uid = (ArrayList<String>) ((ArrayList) strategy2.getRequiredAttributes().get("uid")).get(1);
		} catch (Exception e) {
		}

		List<String> old_ous = null;
		List<String> old_uid = null;
		Inconsistency groups;
		Inconsistency users;

		try {

			if (strategy1.getRequiredAttributes() != null && strategy1.getRequiredAttributes().get("ou") != null)
				old_ous = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("ou")).get(1);

			groups = listCompare(old_ous, new_ous);
		} catch (Exception e) {
			groups = listCompare(null, new_ous);
		}

		try {

			if (strategy1.getRequiredAttributes() != null && strategy1.getRequiredAttributes().get("uid") != null)
				old_uid = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("uid")).get(1);
			users = listCompare(old_uid, new_uid);

		} catch (NullPointerException e) {
			users = listCompare(null, new_uid);
		}

		return new UsersGroups(users, groups);
	}

	private Inconsistency listCompare(List<String> listOld, List<String> listNew) {
		List<String> add = new LinkedList<>();
		List<String> remove = new LinkedList<>();

		if (listNew != null)
			for (String s : listNew) {
				if (listOld == null)
					add.add(s);
				else if (!listOld.contains(s))
					add.add(s);

			}

		if (listOld != null)
			for (String s : listOld) {
				if (listNew == null)
					remove.add(s);
				else if (!listNew.contains(s))
					remove.add(s);

			}

		return new Inconsistency(add, remove);
	}
}
