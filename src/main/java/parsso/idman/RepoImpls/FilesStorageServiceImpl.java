package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Repos.FilesStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    Path photoPathRoot;
    Path servicesPathRoot;
    @Value("${profile.photo.path}")
    private String photoPath;
    @Value("${services.folder.path}")
    private String servicesPath;
    @Value("${metadata.file.path}")
    private String metadataPath;

    @Override
    public void init() {
        photoPathRoot = Paths.get(photoPath);
        servicesPathRoot = Paths.get(servicesPath);


        try {
            if (Files.notExists(photoPathRoot))
                Files.createDirectory(photoPathRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload photo!");
        }

        try {
            if (Files.notExists(servicesPathRoot))
                Files.createDirectory(servicesPathRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for services!");
        }

    }

    @Override
    public void saveMetadata(MultipartFile file, String name) throws IOException {

        try {
            Path pathServices = Paths.get(metadataPath);
            Files.copy(file.getInputStream(), pathServices.resolve(name));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void saveProfilePhoto(MultipartFile file, String name) throws IOException {

        try {

            Files.copy(file.getInputStream(), this.photoPathRoot.resolve(name));

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