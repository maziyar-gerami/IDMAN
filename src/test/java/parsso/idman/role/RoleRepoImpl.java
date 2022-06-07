package parsso.idman.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import net.minidev.json.JSONObject;
import parsso.idman.repos.RolesRepo;
import parsso.idman.repos.users.oprations.sub.UsersCreateRepo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RoleRepoImpl {
  public RoleRepoImpl() {
  }

  @Autowired
  RolesRepo rolesRepo;
  @Autowired
  UsersRetrieveRepo userRepo;
  @Autowired
  UsersCreateRepo userRepCreate;

  @Test
  public void retrieve() {
    assertNotNull(rolesRepo.retrieve(), "retrieving all roles, failed");
  }

  @Test
  public void update() {

    List<String> users = new LinkedList<>();
    // TODO:Change it
    users.add("su");

    ArrayList<String> names = new ArrayList<>();
    JSONObject jsonObject = new JSONObject();
    names.addAll(users);
    jsonObject.put("names", names);

    assertEquals(HttpStatus.OK, rolesRepo.updateRole("tester", "USER", jsonObject),
        "User role Update failed: step 1");
    for (String user : names) {
      assertEquals("USER", userRepo.retrieveUserMain(user).getRole(), "Role not changed: Step 1");

    }
    assertEquals(HttpStatus.OK, rolesRepo.updateRole("tester", "SUPERUSER", jsonObject),
        "User role Update failed: Step 2");
    for (String user : names) {
      assertEquals("SUPERUSER", userRepo.retrieveUserMain(user).getRole(), "Role not changed: Step 2");
    }
    names.add("jsdvns");
    jsonObject.put("names", names);
    assertEquals(HttpStatus.PARTIAL_CONTENT, rolesRepo.updateRole("tester", "SUPERUSER", jsonObject),
        "User role Update failed for partial content: Step 3");
    names = new ArrayList<>();
    names.add("jsdvns");
    names.add("jsd1vns");
    jsonObject.put("names", names);
    assertEquals(HttpStatus.BAD_REQUEST, rolesRepo.updateRole("tester", "SUPERUSER", jsonObject),
        "User role Update failed for bad reques: Step 4");
  }

}
