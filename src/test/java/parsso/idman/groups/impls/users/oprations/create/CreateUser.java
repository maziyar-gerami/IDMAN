package parsso.idman.groups.impls.users.oprations.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.user.ImportUsers;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersCreateRepo;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CreateUser {

  @Autowired
  UsersCreateRepo usersCreateRepo;

  @Test
  public void createTest() {

    // Create null user
    User user = new User();
    assertNull(usersCreateRepo.create("tester", user), "creating null user problem");

    // Create repetitive user
    user.set_id("su");
    JSONObject jo = usersCreateRepo.create("tester", user);
    boolean l = true;
    try {
      jo.get("conflicts");
    } catch (NullPointerException e) {
      l = false;
    }
    assertTrue(l, "creating repetitive user problem");
    // Create user with invalid attributes
    user.set_id("tester_test_003");
    JSONObject js = usersCreateRepo.create("tester", user);
    boolean t = true;
    try {
      js.get("invalid attributes");
    } catch (NullPointerException e) {
      t = false;
    }
    assertTrue(t, "insert user without esential parameters");
    // create user successfully
    user.setStatus("enable");
    user.setDisplayName("displayName");
    user.setMail("mail");
    user.setMobile("08923429");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setUserPassword("xsd!2dsvgfslA");
    user.setEmployeeNumber("employeeNumber");
    user.setDescription("description");

    JSONObject s = usersCreateRepo.create("tester", user);

    assertEquals(new JSONObject(), s, "User creation failed");
  }

  @Test
  public void importTest() throws IOException {

    int[] sequence = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

    File file = new File("src/test/resources/users_ok.txt");
    FileInputStream input = new FileInputStream(file);
    MultipartFile multipartFile = new MockMultipartFile("file",
        file.getName(), "text/plain", IOUtils.toByteArray(input));

    new ImportUsers(usersCreateRepo).importFileUsers("tester", multipartFile, sequence, true);
  }

}
