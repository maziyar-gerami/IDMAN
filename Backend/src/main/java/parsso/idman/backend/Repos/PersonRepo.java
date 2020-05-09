package parsso.idman.backend.Repos;

import parsso.idman.backend.Models.Person;

import java.util.List;

public interface PersonRepo {

    public List<Person> retrieve();
    public String create(Person p);
    public String update(Person p);
    public String remove(String userId);
    public Person retrievePerson(String userId);

}