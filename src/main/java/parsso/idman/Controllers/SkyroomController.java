package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.SkyRoom;
import parsso.idman.Repos.SkyroomRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class SkyroomController {
    @Autowired
    SkyroomRepo runSkyroom;

    @Value("${skyroom.enable}")
    private String skyroomEnable;

    @GetMapping("/api/skyroom")
    ResponseEntity<SkyRoom> hello(HttpServletRequest request) throws IOException {
        if (skyroomEnable.equalsIgnoreCase("true"))
            return new ResponseEntity<>(runSkyroom.Run(request.getUserPrincipal().getName()), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
}
