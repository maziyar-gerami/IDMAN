package parsso.idman.Repos;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init();

    void saveMetadata(MultipartFile file, String name) throws IOException;

    void saveProfilePhoto(MultipartFile file, String name) throws IOException;

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();
}
