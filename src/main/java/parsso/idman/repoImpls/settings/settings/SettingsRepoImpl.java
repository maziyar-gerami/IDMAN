package parsso.idman.repoImpls.settings.settings;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.repoImpls.settings.settings.subclasses.BackupSettings;
import parsso.idman.repoImpls.settings.settings.subclasses.ResetSettings;
import parsso.idman.repoImpls.settings.settings.subclasses.RestoreSettings;
import parsso.idman.repoImpls.settings.settings.subclasses.RetrieveSettings;
import parsso.idman.repoImpls.settings.settings.subclasses.UpdateSettings;
import parsso.idman.repoImpls.settings.settings.subclasses.BackupSettings.Backup;
import parsso.idman.repos.SettingsRepo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class SettingsRepoImpl implements SettingsRepo {
    PasswordSettings passwordSettings;
    MongoTemplate mongoTemplate;
    LdapTemplate ldapTemplate;
    UniformLogger uniformLogger;


    @Autowired
    public SettingsRepoImpl(PasswordSettings passwordSettings, MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, UniformLogger uniformLogger) {
        this.passwordSettings = passwordSettings;
        this.mongoTemplate = mongoTemplate;
        this.ldapTemplate = ldapTemplate;
        this.uniformLogger = uniformLogger;
    }

    @PostConstruct
    public void setPreference(){
        //new PreferenceSettings(mongoTemplate).run();
    }


    @Override
    public List<Setting> retrieve() {

    
        return new RetrieveSettings(mongoTemplate,passwordSettings).retrieve();
    }

    @Override
    public List<Setting> retrieveALL() {

       return new RetrieveSettings(mongoTemplate,passwordSettings).retrieveALL();
    }

    @Override
    public HttpStatus update(String doer, List<Property> properties) {
        
        return new UpdateSettings(passwordSettings, mongoTemplate, uniformLogger).update(doer, properties);
    }

    @Override
    public HttpStatus backup(String doerID) {
        return new BackupSettings(mongoTemplate, uniformLogger, passwordSettings).backup(doerID);
    }

    @Override
    public Object retrieveProperties(long id) {
        if(id == 0)
            return mongoTemplate.findAll(ID.class,Variables.col_propertiesBackup);
        return mongoTemplate.find(new Query(Criteria.where("_id").is(id)),Setting.class,Variables.col_propertiesBackup);
    }

    @Override
    public HttpStatus reset(String doerId) throws IOException {
        return new ResetSettings(mongoTemplate, uniformLogger, passwordSettings).reset(doerId);

    }

    @Override
    public HttpStatus restore(String doerId, String id) {
       return new RestoreSettings(mongoTemplate, uniformLogger, passwordSettings).restore(doerId, id);
    }

    @Override
    public List<Backup> listBackups(String lang) {
        return  new BackupSettings(mongoTemplate, uniformLogger, passwordSettings).listBackups(lang);
    }


    @Setter
    @Getter
    private class ID{
        private long _id;
    }

    

    @Setter
    @Getter
    public class BackupData{
        long _id;
        List<Setting> data;
    }
}
