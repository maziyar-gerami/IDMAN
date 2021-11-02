package parsso.idman.helpers.group;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parsso.idman.models.groups.Group;
import parsso.idman.repos.GroupRepo;

import java.util.LinkedList;
import java.util.List;

@Service
public class GroupsChecks {
    @Autowired
    GroupRepo groupRepo;

    public boolean checkGroup(List<String> groups) {
        if (groups.size() == 1 && groups.get(0).equals(""))
            return true;

        List<Group> realGroups = groupRepo.retrieve();
        List<String> realStrings = new LinkedList<>();
        for (Group group : realGroups) {
            realStrings.add(group.getId());
        }

        for (String group : groups) {
            if (!realStrings.contains(group))
                return false;

        }
        return true;
    }

    public List<String> invalidGroups(List<String> groups) {
        List<Group> realGroups = groupRepo.retrieve();
        List<String> realStrings = new LinkedList<>();
        List<String> invalids = new LinkedList<>();
        for (Group group : realGroups) {
            realStrings.add(group.getId());
        }

        for (String group : groups) {
            if (!realStrings.contains(group))
                invalids.add(group);

        }
        return invalids;
    }
}
