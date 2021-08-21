package parsso.idman.Helpers;


import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Services.ServicesSubModel.AccessStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class Comparison {
    public UsersGroups compare(AccessStrategy strategy1, AccessStrategy strategy2) {
        List<String> new_ous =null;
        List<String> new_uid = null;

        try {
            new_ous= (ArrayList<String>) ((ArrayList) strategy2.getRequiredAttributes().get("ou")).get(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            new_uid = (ArrayList<String>) ((ArrayList) strategy2.getRequiredAttributes().get("uid")).get(1);
        }catch (Exception e){}

        if(strategy1 == null){
            return new UsersGroups(new Inconsistency(new_uid,null),new Inconsistency(new_ous,null));
        }

        List<String> old_ous = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("ou")).get(1);

        Inconsistency groups = listCompare(old_ous, new_ous);
        List<String> old_uid = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("uid")).get(1);
        Inconsistency users = listCompare(old_uid, new_uid);
        return new UsersGroups(users, groups);
    }

    private Inconsistency listCompare(List<String> listOld, List<String> listNew) {
        List<String> add = new LinkedList<>();
        List<String> remove = new LinkedList<>();

        for (String s : listNew)
            if (listOld!=null && !listOld.contains(s))
                add.add(s);

        for (String s : listOld)
            if (listNew!=null && !listNew.contains(s))
                add.add(s);


        return new Inconsistency(add, remove);
    }

    private class Inconsistency {
        List add;
        List remove;
        Inconsistency(List add, List remove) {
            this.add = add;
            this.remove = remove;
        }
    }

    private class UsersGroups {
        Inconsistency users;
        Inconsistency groups;
        UsersGroups(Inconsistency users, Inconsistency groups) {
            this.users = users;
            this.groups = groups;
        }

    }
}
