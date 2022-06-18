package parsso.idman.repos;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  void init() throws IOException;

  void saveMetadata(MultipartFile file, String name);

  void saveProfilePhoto(MultipartFile file, String name);

  Resource load(String filename);

  void saveIcon(MultipartFile file, String fileName);
}
