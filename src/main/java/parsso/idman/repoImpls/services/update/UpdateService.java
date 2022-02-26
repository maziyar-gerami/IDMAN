package parsso.idman.repoImpls.services.update;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.models.services.Service;
import parsso.idman.repoImpls.services.create.sublcasses.Metadata;
import parsso.idman.repoImpls.services.create.sublcasses.ServiceIcon;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.ServiceRepo;

@org.springframework.stereotype.Service
public class UpdateService implements ServiceRepo.Update {
    FilesStorageService filesStorageService;
    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    public UpdateService(FilesStorageService filesStorageService) {
        this.filesStorageService = filesStorageService;
    }

    @Override
    public String uploadMetadata(MultipartFile file) {
        return new Metadata(filesStorageService,BASE_URL).upload(file);
    }

    @Override
    public void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) {

    }


    @Override
    public HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) {
        return new UpdateService(filesStorageService).updateService(doerID,id,jsonObject,system);
    }

    @Override
    public String uploadIcon(MultipartFile file) {
        return new ServiceIcon(filesStorageService,BASE_URL).upload(file);
    }
}
