package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import parsso.idman.Models.Person;
import parsso.idman.RepoImpls.PersonRepoImpl;
import parsso.idman.Repos.PersonRepo;

@Controller
public class LoginController {

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/resetPass")
    public String resetPass() {
        return "resetPass";
    }


    @GetMapping ("/api/users/validateToken/{uId}/{token}")
    public String resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token)
    {
        Person person = personRepo.checkToken(uId,token);

        if (person != null)
            return "resetPassword";
        else
            return "resetPassWrongTAI";


    }



}

