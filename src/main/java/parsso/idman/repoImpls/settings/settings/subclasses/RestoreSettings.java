package parsso.idman.repoImpls.settings.settings.subclasses;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.repoImpls.settings.settings.SettingsRepoImpl.BackupData;

public class RestoreSettings {
    
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;
    final PasswordSettings passwordSettings;


    public RestoreSettings(MongoTemplate mongoTemplate, UniformLogger uniformLogger, PasswordSettings passwordSettings) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.passwordSettings = passwordSettings;
    }



    
    public HttpStatus restore(String doerId, String id) {
        BackupData backupData = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Long.valueOf(id))),BackupData.class,Variables.col_propertiesBackup);
        List<Setting> settings = backupData.getData();
        HttpStatus httpStatus =  new UpdateSettings(passwordSettings, mongoTemplate, uniformLogger).update(doerId,new Property().settingsToProperty(settings,"fa"));
        if (httpStatus.value() == 200)
            uniformLogger.info(doerId,new ReportMessage(Variables.MODEL_SETTINGS,id,Variables.ACTION_RESTORE,Variables.RESULT_SUCCESS));
        else
            uniformLogger.warn(doerId,new ReportMessage(Variables.MODEL_SETTINGS,id,Variables.ACTION_RESTORE,Variables.RESULT_FAILED));

        return httpStatus;
    }
    
}
