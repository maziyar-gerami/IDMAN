package parsso.idman.groups.impls;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import net.minidev.json.JSONObject;
import parsso.idman.models.groups.Group;
import parsso.idman.repos.GroupRepo;

@RunWith(SpringRunner.class) 
@SpringBootTest(webEnvironment =  WebEnvironment.NONE)
public class GroupRepoImp {

  @Autowired
  private GroupRepo groupRepo;

  @Test
  public void run(){
    create();
    retrieve();
    update();
    delete();
  }
  
  public void create(){
    
    //Bad Request
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group(null,"test","test")));
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group("test_001",null,"test")));
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group("test_001","test",null)));
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group("","test","test")));
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group("test_001","","test")));
    assertEquals("Create group failed. Not valid data", HttpStatus.BAD_REQUEST, groupRepo.create("Tester", new Group("test_001","test","")));

    //Created
    assertEquals("Create group failed.", HttpStatus.CREATED, groupRepo.create("Tester", new Group("test_001","test","test")));
    assertEquals("Create group failed.", HttpStatus.CREATED, groupRepo.create("Tester", new Group("test_002","test","test")));
    assertEquals("Create group failed.", HttpStatus.CREATED, groupRepo.create("Tester", new Group("test_003","test","test")));
    assertEquals("Create group failed.", HttpStatus.CREATED, groupRepo.create("Tester", new Group("test_004","test","test")));
    assertEquals("Create group failed.", HttpStatus.CREATED, groupRepo.create("Tester", new Group("test_005","test","test")));

    //Found
    assertEquals("Create group failed. This group already exist", HttpStatus.FOUND, groupRepo.create("Tester", new Group("test_001","test","test")));
        
  }

  public void retrieve(){
    assertTrue((groupRepo.retrieve()).size()>0, "Retrieve failed");
    assertEquals("Group Retrieve Failed",  new Group("test_001","test",""),  groupRepo.retrieve(true,"test_001"));
    assertEquals("Group Retrieve Failed",  null,  groupRepo.retrieve(true,"xx2345gf"));
    assertEquals("Group Retrieve Failed",  null,  groupRepo.retrieve(true,""));
    assertEquals("Bad request",  null,  groupRepo.retrieve(true,""));
    assertEquals("Bad request",  null,  groupRepo.retrieve(true,null));
    assertEquals("Bad request",  null,  groupRepo.retrieve(false,""));
    assertEquals("Bad request",  null,  groupRepo.retrieve(false,null));
    assertTrue( groupRepo.retrieve().size()>4 ,"Bad request");
    assertTrue( groupRepo.retrieve().stream().anyMatch(ou -> ou.getId().equals("test_001")) ,"Bad request");
    assertTrue( groupRepo.retrieve().stream().anyMatch(ou -> ou.getId().equals("test_002")) ,"Bad request");
    assertTrue( groupRepo.retrieve().stream().anyMatch(ou -> ou.getId().equals("test_003")) ,"Bad request");
    assertTrue( groupRepo.retrieve().stream().anyMatch(ou -> ou.getId().equals("test_004")) ,"Bad request");
    assertNotNull( groupRepo.retrieve(true, "test_001"), "reatrieve created group with simple attributes failed");
    assertNotNull( groupRepo.retrieve(false, "test_001"), "reatrieve created group with full attributes failed");
  }

  
  public void update(){
    Group ou = new Group("jfdf", "name", "description");

    assertEquals("Update group faild, group not found", HttpStatus.NOT_FOUND, groupRepo.update("tester", "jdfsh", ou));
    ou.setId(null);
    assertEquals("Update group faild, data corrupt", HttpStatus.BAD_REQUEST, groupRepo.update("tester", "jdfsh", ou));
    ou.setName(null);
    assertEquals("Update group faild, data corrupt", HttpStatus.BAD_REQUEST, groupRepo.update("tester", "jdfsh", ou));
    ou.setDescription(null);
    assertEquals("Update group faild, data corrupt", HttpStatus.BAD_REQUEST, groupRepo.update("tester", "jdfsh", ou));

    ou = new Group("test_0011", "name1", "description1");
    assertEquals("Update group failed", HttpStatus.OK, groupRepo.update("tester", "test_001", ou));
  }

  public void delete(){
    JSONObject json1 = new JSONObject();
    ArrayList<String> names = new ArrayList<String>();
    names.add("test_001");
    json1.put("names", names);

    assertEquals("cannot delete", HttpStatus.NO_CONTENT, groupRepo.remove("tester", json1));

    JSONObject json2 = new JSONObject();
    names = new ArrayList<String>();
    names.add("test_002");
    names.add("test_003");
    json2.put("names", names);

    assertEquals("cannot delete", HttpStatus.NO_CONTENT, groupRepo.remove("tester", json2));

    names.add("test_004");
    names.add("test_00x");
    names.add("test_005");

    assertEquals("cannot delete", HttpStatus.NO_CONTENT, groupRepo.remove("tester", json2));

    assertNull("test_001 not deleted", groupRepo.retrieve(true, "test_001"));
    assertNull("test_002 not deleted", groupRepo.retrieve(true, "test_002"));
    assertNull("test_003 not deleted", groupRepo.retrieve(true, "test_003"));
    assertNull("test_004 not deleted", groupRepo.retrieve(true, "test_004"));
    assertNull("test_005 not deleted", groupRepo.retrieve(true, "test_005"));
  }
  
}
