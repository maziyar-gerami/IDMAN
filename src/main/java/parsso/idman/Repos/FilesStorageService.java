package parsso.idman.Repos;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
	void init();

	void saveMetadata(MultipartFile file, String name);

	void saveProfilePhoto(MultipartFile file, String name);

	Resource load(String filename);

	Stream<Path> loadAll();
}
