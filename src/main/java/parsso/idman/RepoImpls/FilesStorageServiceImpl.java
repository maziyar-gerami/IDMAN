package parsso.idman.RepoImpls;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Repos.FilesStorageService;
import java.nio.file.Files;



@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Value("${profile.photo.path}")
    private String photoPath;
    Path photoPathRoot;

    @Value("${services.folder.path}")
    private String servicesPath;
    Path servicesPathRoot;


    @Override
    public void init() {
        photoPathRoot = Paths.get(photoPath);
        servicesPathRoot = Paths.get(servicesPath);


        try {
            if(Files.notExists(photoPathRoot))
            Files.createDirectory(photoPathRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload photo!");
        }

        try {
            if(Files.notExists(servicesPathRoot))
                Files.createDirectory(servicesPathRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for services!");
        }

    }

    @Override
    public void save(MultipartFile file, String name) {
        try {
            //InputStream inputStream = file.getInputStream();

            //Files.copy(inputStream, this.photoPathRoot.resolve(name));
            Files.copy(file.getInputStream(),this.photoPathRoot.resolve(name));


        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = photoPathRoot.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(photoPathRoot.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.photoPathRoot, 1).filter(path -> !path.equals(this.photoPathRoot)).map(this.photoPathRoot::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}