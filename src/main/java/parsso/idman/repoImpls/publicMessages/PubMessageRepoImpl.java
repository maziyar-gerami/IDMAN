package parsso.idman.repoImpls.publicMessages;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.models.other.PublicMessage;
import parsso.idman.repoImpls.publicMessages.subClasses.CreatePubMessage;
import parsso.idman.repoImpls.publicMessages.subClasses.DeletePubMessage;
import parsso.idman.repoImpls.publicMessages.subClasses.RetrievePubMessage;
import parsso.idman.repoImpls.publicMessages.subClasses.UpdatePubMessage;
import parsso.idman.repos.PubMessageRepo;
import java.util.List;
@Service
public class PubMessageRepoImpl implements PubMessageRepo {
    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;
    @Autowired
    public PubMessageRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger){
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }

    @Override
    public List<PublicMessage> showVisiblePubicMessages() {
        return new RetrievePubMessage(mongoTemplate).showVisiblePubicMessages();
    }

    @Override
    public List<PublicMessage> showAllPubicMessages(String id) {
       return new RetrievePubMessage(mongoTemplate).showAllPubicMessages(id);
    }

    @Override
    public HttpStatus postPubicMessage(String doer, PublicMessage message) {

        return new CreatePubMessage(mongoTemplate, uniformLogger).postPubicMessage(doer, message);
    }

    @Override
    public HttpStatus editPubicMessage(String doer, PublicMessage message) {

        return new UpdatePubMessage(mongoTemplate,uniformLogger).editPubicMessage(doer,message);
    }

    @Override
    public HttpStatus deletePubicMessage(String doer, JSONObject jsonObject) {

        return new DeletePubMessage(mongoTemplate, uniformLogger).deletePubicMessage(doer, jsonObject);
    }
}
