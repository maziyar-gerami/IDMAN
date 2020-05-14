package parsso.idman.backend.Repos;

import parsso.idman.backend.Models.Person;

<<<<<<< HEAD
import javax.naming.Name;
=======
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
import java.util.List;

public interface PersonRepo {

    public List<Person> retrieve();
<<<<<<< HEAD
    public String create(int parentOU, Person p);
    public String update(String uid, Person p);
    public String remove(int userId);
    public String remove();
    public Person retrievePerson(String userId);
    public int retrieveOU(String userId);
    public List<Person> retrieveSubUsers(String OU);
=======
    public String create(Person p);
    public String update(Person p);
    public String remove(String userId);
    public Person retrievePerson(String userId);
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44

}