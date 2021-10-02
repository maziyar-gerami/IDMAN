package parsso.idman.Repos.fileStorage;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface InitializeFile {
	void init();

}
