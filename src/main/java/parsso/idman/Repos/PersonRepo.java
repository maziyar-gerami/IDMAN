package parsso.idman.Repos;

import parsso.idman.Models.Person;

import javax.naming.Name;
import java.util.List;

public interface PersonRepo {

    public List<Person> retrieve();
    public String create(int parentOU, Person p);
    public String update(String uid, Person p);
    public String remove(String userId);
    public String remove();
    public Person retrievePerson(String userId);
    public int retrieveOU(String userId);
    public List<Person> retrieveSubUsers(String OU);

}