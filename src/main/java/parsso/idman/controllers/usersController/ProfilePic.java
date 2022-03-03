package parsso.idman.controllers.usersController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.models.response.Response;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfilePic extends UsersOps {
    private UserRepo.ProfilePic profilePic;

    @Autowired
    public ProfilePic(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.ProfilePic profilePic,UserRepo.UsersOp.Retrieve usersOpRetrieve) {
        super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
        this.profilePic = profilePic;
    }

    @GetMapping("/api/user/photo")
    public ResponseEntity<String> getImage(HttpServletResponse response, HttpServletRequest request) {
        parsso.idman.models.users.User user = usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName());
        return new ResponseEntity<>(profilePic.retrieve(response, user), HttpStatus.OK);
    }


    @DeleteMapping("/api/user/photo")
    public ResponseEntity<Response> deleteImage(HttpServletRequest request, @RequestParam(name = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        parsso.idman.models.users.User user = usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName());
        if (profilePic.delete(user))
            return new ResponseEntity<>(new Response(true,Variables.MODEL_USER,HttpStatus.OK.value(), lang), HttpStatus.OK);
        return new ResponseEntity<>(new Response(false, Variables.MODEL_USER,  HttpStatus.BAD_REQUEST.value(), lang), HttpStatus.OK);
    }

    @PostMapping("/api/user/photo")
    public ResponseEntity<Response> uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam(name = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        if (profilePic.upload(file, request.getUserPrincipal().getName()))
            return new ResponseEntity<>(new Response(true,Variables.MODEL_USER, HttpStatus.OK.value(), lang), HttpStatus.OK);
        return new ResponseEntity<>(new Response(true, Variables.MODEL_USER, HttpStatus.BAD_REQUEST.value(), lang), HttpStatus.OK);
    }


}
