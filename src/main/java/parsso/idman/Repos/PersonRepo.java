package parsso.idman.Repos;

import parsso.idman.Models.Person;

import javax.naming.Name;
import java.util.List;

public interface PersonRepo {

    public List<Person> retrieve();
    public String create(Person p);
    public String update(Person p);
    public String remove(String userId);
    public String remove();
    public Person retrievePerson(String userId);
    public List<Person> checkMail(String token);
    public String sendEmail(String email);
    public String sendEmail(String email, String uid);
    public Person checkToken(String userId, String token);
    public String updatePass(String userId, String pass, String token);
}