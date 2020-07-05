package parsso.idman.Controllers;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController{

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;
    
    @Value("${administrator.ou.name}")
    private String adminOu;

    @GetMapping("/")
    public String Root(){
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String Dashboard(){
        return "dashboard";
    }

    @GetMapping("/groups")
    public String Groups(HttpServletRequest request){
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                    return "groups";
            }else{
                    return "403";
            }
        } catch (Exception e) {
            return "403";
        }
    }

    @GetMapping("/users")
    public String Users(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                    return "users";
            }else{
                    return "403";
            }
        } catch (Exception e) {
            return "403";
        }
    }

    @GetMapping("/settings")
    public String Settings(){
        return "settings";
    }

    @GetMapping("/error")
    public String Error(){
        return "error";
    }

    @GetMapping("/privacy")
    public String Privacy(){
        return "privacy";
    }
}