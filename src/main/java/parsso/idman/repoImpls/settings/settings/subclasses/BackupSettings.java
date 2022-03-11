package parsso.idman.repoImpls.settings.settings.subclasses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Setting;
import parsso.idman.models.other.Time;

public class BackupSettings {
    final MongoTemplate mongoTemplate;
        final UniformLogger uniformLogger;
        final PasswordSettings passwordSettings;

    public BackupSettings(MongoTemplate mongoTemplate, UniformLogger uniformLogger,PasswordSettings passwordSettings) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.passwordSettings = passwordSettings;
    }

    public HttpStatus backup(String doerID) {
        
        
        List<Setting> allSettings = new RetrieveSettings(mongoTemplate, passwordSettings).retrieveALL();
        JSONObject jsonObject = new JSONObject();
        long _id = new Date().getTime();
        jsonObject.put("_id",_id);
        jsonObject.put("nameFA",new Time().timeToShow(_id,"FA"));
        jsonObject.put("nameEN",new Time().timeToShow(_id,"EN"));
        jsonObject.put("data",allSettings);
        if(!mongoTemplate.collectionExists(Variables.col_propertiesBackup))
            mongoTemplate.createCollection(Variables.col_propertiesBackup);
        try {
            mongoTemplate.save(jsonObject,Variables.col_propertiesBackup);
            uniformLogger.info(doerID,new ReportMessage(Variables.MODEL_SETTINGS,String.valueOf(_id),Variables.ACTION_BACKUP,Variables.RESULT_SUCCESS));
            return HttpStatus.OK;
        }catch (Exception e){
            uniformLogger.info(doerID,new ReportMessage(Variables.MODEL_SETTINGS,String.valueOf(_id),Variables.ACTION_BACKUP,Variables.RESULT_FAILED));
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }
    public List<Backup> listBackups(String lang) {

        List<Backup> backups = mongoTemplate.findAll(Backup.class,Variables.col_propertiesBackup);
        for (Backup backup:backups ) {
            if (lang.equalsIgnoreCase("EN"))
                backup.setName(backup.getNameEN());
            else
                backup.setName(backup.getNameFA());
        }
        backups.removeIf(backup -> backup.get_id()==0);
    return  backups;
    }

    @Setter
    @Getter
    public class Backup{
        long _id;
        String name;
        @JsonIgnore
        String nameFA;
        @JsonIgnore
        String nameEN;
    }
    
}
