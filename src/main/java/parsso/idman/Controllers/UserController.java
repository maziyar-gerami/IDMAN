package parsso.idman.Controllers;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    FilesStorageService storageService;
    @Autowired
    private UserRepo userRepo;
    @Value("${administrator.ou.name}")
    private String adminOu;

    @Value("${upload.pic.path}")
    private String uploadedFilesPath;

    @Value("${folder.seprator}")
    private String folderSeprator;

    /**
     * Retrieve logged-in user
     *
     * @return user object of current user which logged in
     */
    @GetMapping("/api/user")
    public ResponseEntity<User> retrieveUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.retrieveUser(principal.getName()), HttpStatus.OK);
    }

    /**
     * Update logged-in user
     *
     * @return http error code
     */
    @PutMapping("/api/user")
    public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody User user) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.update(principal.getName(), user));
    }

    /**
     * Specify that logged-in user is admin or not
     *
     * @return whether logged-in user is admin or not
     */
    @GetMapping("/api/user/isAdmin")
    public boolean isAdmin(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();
            return memberOf.contains(adminOu);

        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Get logged-in user photo
     *
     * @return the photo of logged in user
     */
    @GetMapping("/api/user/photo")
    public void getImage(HttpServletResponse response, HttpServletRequest request) throws IOException {

        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser("ali");


        File file = new File(uploadedFilesPath+folderSeprator+user.getPhotoPath());



        if (file.exists()) {
            try {
                String contentType = "image/png";
                response.setContentType(contentType);
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                // copy from in to out
                IOUtils.copy(in, out);
                out.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Post photo for logged-in user
     *
     * @return the response entity
     */
    @PostMapping("/api/user/photo")
    public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();


        String message;
        try {

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());



String s =timeStamp + file.getOriginalFilename();

            storageService.save(file, s);




            User user = userRepo.retrieveUser("ali");


            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            user.setPhotoPath(s);
            userRepo.update(user.getUserId(), user);

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";

        }
        System.out.println(message);
        return new RedirectView("/dashboard");
    }

}
