package parsso.idman.repoImpls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.models.other.Time;
import parsso.idman.repoImpls.settings.helper.PreferenceSettings;
import parsso.idman.repos.SettingsRepo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

        List<Setting> settings = retrieveALL();
        PWD pwd = passwordSettings.retrieve();
        Setting sms_sdk = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.SMS_SDK)), Setting.class, Variables.col_properties);

        assert sms_sdk != null;
        if (sms_sdk.getValue().equalsIgnoreCase("magfa"))
            settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.KAVENEGAR_API_KEY));

        else if (sms_sdk.getValue().toString().equalsIgnoreCase("kavenegar")) {
            settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.SMS_MAGFA_USERNAME));
            settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.SMS_MAGFA_PASSWORD));
        }

        Setting passwordLimit = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.PASSWORD_CHANGE_LIMIT)), Setting.class, Variables.col_properties);

        if(passwordLimit.getValue().equals("off"))
            settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.PASSWORD_CHANGE_LIMIT_NUMBER));

        boolean skyroom = Boolean.parseBoolean(mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.SKYROOM_ENABLE)), Setting.class, Variables.col_properties).getValue().toString());

        if (!skyroom)
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("skyroom.api.key"));

        if(Integer.parseInt(pwd.getPwdCheckQuality())==0) {
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("pwdMinLength"));
        }

        return settings;
    }

    @Override
    public List<Setting> retrieveALL() {

        List<Setting> settings = mongoTemplate.find(new Query(), Setting.class, Variables.col_properties);
        PWD pwd = passwordSettings.retrieve();
        for (Setting setting : settings) {
            if (setting.getGroupEN().equalsIgnoreCase("Password") && setting.getValue() == null) {
                switch (setting.get_id()) {
                    case ("pwdCheckQuality"):
                        if(Integer.parseInt(pwd.getPwdCheckQuality())>0)
                            setting.setValue("true");
                        else
                            setting.setValue("false");
                        break;

                    case ("pwdFailureCountInterval"):
                        setting.setValue(pwd.getPwdFailureCountInterval());
                        break;

                    case ("pwdGraceAuthNLimit"):
                        setting.setValue(pwd.getPwdGraceAuthNLimit());
                        break;

                    case ("pwdInHistory"):
                        setting.setValue(pwd.getPwdInHistory());
                        break;

                    case ("pwdLockout"):
                        setting.setValue(pwd.getPwdLockout());
                        break;

                    case ("pwdLockoutDuration"):
                        setting.setValue(pwd.getPwdLockoutDuration());
                        break;

                    case ("pwdMaxAge"):
                        setting.setValue(pwd.getPwdMaxAge());
                        break;

                    case ("pwdMaxFailure"):
                        setting.setValue(pwd.getPwdMaxFailure());
                        break;

                    case ("pwdMinLength"):
                        if(Integer.parseInt(pwd.getPwdCheckQuality())>0)
                            setting.setValue(pwd.getPwdMinLength());
                        break;

                    case ("pwdExpireWarning"):
                            setting.setValue(pwd.getPwdExpireWarning());
                        break;
                }

            }
        }

        return settings;
    }

    @Override
    public HttpStatus update(String doer, List<Property> properties) {
        List<Property> ldapProperties = new ArrayList<>();
        List<Property> mongoProperties = new ArrayList<>();
        PWD ldapPasswords = passwordSettings.retrieve();
        for (Property property:properties) {
            Setting storedSetting = mongoTemplate.findOne(new Query(Criteria.where("_id").is(property.get_id())),Setting.class,Variables.col_properties);
            if (property.get_id().equals("pwdCheckQuality") ||
                    property.get_id().equals("pwdFailureCountInterval") ||
                    property.get_id().equals("pwdGraceAuthNLimit") ||
                    property.get_id().equals("pwdInHistory") ||
                    property.get_id().equals("pwdLockout")||
                    property.get_id().equals("pwdMinLength")||
                    property.get_id().equals("pwdMaxFailure") ||
                    property.get_id().equals("pwdMaxAge")||
                    property.get_id().equals("pwdExpireWarning")||
                    property.get_id().equals("pwdLockoutDuration")) {
                switch (property.get_id()){
                    case "pwdCheckQuality":
                        storedSetting.setValue(ldapPasswords.getPwdCheckQuality());
                        break;

                    case "pwdFailureCountInterval":
                        storedSetting.setValue(ldapPasswords.getPwdFailureCountInterval());
                        break;

                    case "pwdGraceAuthNLimit":
                        storedSetting.setValue(ldapPasswords.getPwdGraceAuthNLimit());
                        break;

                    case "pwdInHistory":
                        storedSetting.setValue(ldapPasswords.getPwdInHistory());
                        break;

                    case "pwdExpireWarning":
                        storedSetting.setValue(ldapPasswords.getPwdExpireWarning());
                        break;

                    case "pwdLockout":
                        storedSetting.setValue(ldapPasswords.getPwdLockout());
                        break;

                    case "pwdMinLength":
                        storedSetting.setValue(ldapPasswords.getPwdMinLength());
                        break;

                    case "pwdMaxFailure":
                        storedSetting.setValue(ldapPasswords.getPwdMaxFailure());
                        break;

                    case "pwdMaxAge":
                        storedSetting.setValue(ldapPasswords.getPwdMaxAge());
                        break;

                    case "pwdLockoutDuration":
                        storedSetting.setValue(ldapPasswords.getPwdLockoutDuration());
                        break;


                }

                if(property.get_id().equals("pwdCheckQuality") && property.getValue().toString().equalsIgnoreCase("true"))
                    property.setValue("1");
                else if(property.get_id().equals("pwdCheckQuality") && property.getValue().toString().equalsIgnoreCase("false"))
                    property.setValue("0");

                    ldapProperties.add(property);
            }
            if (!storedSetting.getValue().equalsIgnoreCase(property.getValue().toString()))
                mongoProperties.add(property);
        }

        for (Property property:mongoProperties) {
                Update update = new Update();
                update.set("value", property.getValue());
                new Thread(() -> {
                    mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(property.get_id())), update, Variables.col_properties);
                    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_SETTINGS, property.get_id(), Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, property.getValue().toString(), ""));
                }).start();
        }
        try {
            passwordSettings.update(doer, ldapProperties);
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus backup(String doerID) {
        List<Setting> allSettings = retrieveALL();
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

    @Override
    public Object retrieveProperties(long id) {
        if(id == 0)
            return mongoTemplate.findAll(ID.class,Variables.col_propertiesBackup);
        return mongoTemplate.find(new Query(Criteria.where("_id").is(id)),Setting.class,Variables.col_propertiesBackup);
    }

    @Override
    public HttpStatus reset(String doerId) throws IOException {
        BackupData backup = mongoTemplate.findOne(new Query(Criteria.where("_id").is(0)),BackupData.class,Variables.col_propertiesBackup);
        List<Setting> settings = backup.getData();
        HttpStatus httpStatus = update(doerId,new Property().settingsToProperty(settings,"FA"));

        if (httpStatus.value() == 200)
            uniformLogger.info(doerId,new ReportMessage(Variables.MODEL_SETTINGS,"",Variables.ACTION_RESET_FACTORY,Variables.RESULT_SUCCESS));
        else
            uniformLogger.warn(doerId,new ReportMessage(Variables.MODEL_SETTINGS,"",Variables.ACTION_RESET_FACTORY,Variables.RESULT_FAILED));

        return httpStatus;

    }

    @Override
    public HttpStatus restore(String doerId, String id) {
        BackupData backupData = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Long.valueOf(id))),BackupData.class,Variables.col_propertiesBackup);
        List<Setting> settings = backupData.getData();
        HttpStatus httpStatus =  update(doerId,new Property().settingsToProperty(settings,"fa"));
        if (httpStatus.value() == 200)
            uniformLogger.info(doerId,new ReportMessage(Variables.MODEL_SETTINGS,id,Variables.ACTION_RESTORE,Variables.RESULT_SUCCESS));
        else
            uniformLogger.warn(doerId,new ReportMessage(Variables.MODEL_SETTINGS,id,Variables.ACTION_RESTORE,Variables.RESULT_FAILED));

        return httpStatus;
    }

    @Override
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
    private class ID{
        private long _id;
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

    @Setter
    @Getter
    public class BackupData{
        long _id;
        List<Setting> data;
    }
}
