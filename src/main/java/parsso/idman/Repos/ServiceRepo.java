package parsso.idman.Repos;

import parsso.idman.Models.Group;

import java.util.List;

public interface ServiceRepo {

    List<Group> retrieve();
    String create(Group ou);
    String update(String name, Group ou);
    String remove(String ou);
    String remove();
    Group retrieveOu(String name);
    Group retrieveOu();

}
