package parsso.idman.Repos.fileStorage;


import org.springframework.web.multipart.MultipartFile;

public interface SaveFiles {

	void saveMetadata(MultipartFile file, String name);

	void saveProfilePhoto(MultipartFile file, String name);

	void saveIcon(MultipartFile file, String fileName);
}
