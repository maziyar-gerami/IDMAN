package parsso.idman.Repos;

import com.sun.mail.iap.ByteArray;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Person;

import javax.naming.Name;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface PersonRepo {

    public String changePassword(String uId, String currentPassword, String newPassword);
    public List<Person> retrieveUsersMain();
    public Person getName (String uid);
    public List<Person> retrieveUsersFull();
    public String create(Person p);
    public String update(String uid, Person p);
    public String remove(String userId);
    public String remove();
    public Person retrievePerson(String userId);
    public List<Person> checkMail(String token);
    public String sendEmail(String email);
    public String sendEmail(String email, String uid);
    public Person checkToken(String userId, String token);
    public String updatePass(String userId, String pass, String token);
    public List<Person> importFileUsers(MultipartFile file , int[] sequence, boolean hasHeader) throws IOException;
    public BufferedImage retrievePic(String name) throws IOException;
}