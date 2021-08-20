package parsso.idman.Helpers;


import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Services.ServicesSubModel.AccessStrategy;

import java.util.List;

@Service
public class Comparison {

    public difference compare(AccessStrategy strategy1, AccessStrategy strategy2){
        List<String> old_ous = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("ou")).get(1);
        List<String> old_uid = (List<String>) ((JSONArray) strategy1.getRequiredAttributes().get("uid")).get(1);
        List<String> new_ou = (List<String>) ((JSONArray) strategy2.getRequiredAttributes().get("ou")).get(1);
        List<String> new_uid = (List<String>) ((JSONArray) strategy2.getRequiredAttributes().get("uid")).get(1);
        return  null;
    }

    private List<String> listCompare(List listOld, List listNew){
        return null;
    }



    private class difference{
        difference(Object add, Object remove){
            this.add = add;
            this.remove = remove;
        }
        Object add;
        Object remove;
    }
}
