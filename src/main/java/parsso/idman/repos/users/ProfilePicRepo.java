package parsso.idman.repos.users;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import parsso.idman.models.users.User;

public interface ProfilePicRepo {
  String retrieve(HttpServletResponse response, User user);

  byte[] retrieve(User user);

  boolean upload(MultipartFile file, String name);

  boolean delete(User user);

}
