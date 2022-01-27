package parsso.idman.helpers.oneTimeTasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.SettingsRepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@ComponentScan
public class PostSettings {
    @Autowired
    SettingsRepo settingsRepo;
    @Value("${captcha.length}")
    private String captcha_length;
    @Value("${user.profile.access}")
    private String user_profile_access;
    @Value("${default.user.password}")
    private String default_user_password;
    @Value("${skyroom.api.key}")
    private String skyroom_api_key;
    @Value("${skyroom.enable}")
    private String skyroom_enable;
    @Value("${SMS.SDK}")
    private String SMS_SDK;
    @Value("${SMS.Magfa.username}")
    private String SMS_Magfa_username;
    @Value("${SMS.Magfa.password}")
    private String SMS_Magfa_password;
    @Value("${sms.validation.digits}")
    private String sms_validation_digits;
    @Value("${token.valid.SMS}")
    private String token_valid_SMS;
    @Value("${token.valid.email}")
    private String token_valid_email;
    @Value("${sms.api.key}")
    private String kavenegar_sms_api_key;
    @Value("${metadata.file.path}")
    private String metadata_file_path;
    @Value("${profile.photo.path}")
    private String profile_photo_path;
    @Value("${qr.devices.path}")
    private String qr_devices_path;
    @Value("${password.change.notification}")
    private String password_change_notification;
    @Value("${services.folder.path}")
    private String services_folder_path;
    @Value("${service.icon.path}")
    private String service_icon_path;

    @Autowired
    PasswordSettings passwordSettings;
    public PostSettings(){

    }

    public void run(MongoTemplate mongoTemplate) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = this.getClass().getResourceAsStream("/Properties.json");
        List<Setting> settings = (List<Setting>) mapper.readValue(is, List.class);
        List<Setting> mongoSettings = new LinkedList<>();
        PWD pwd =passwordSettings.retrieve();
        for (Object object : settings) {
            Setting setting = new Setting((LinkedHashMap) object);
            switch (setting.get_id()) {
                case ("captcha.length"):
                    setting.setValue(captcha_length);
                    mongoSettings.add(setting);
                    break;

                case ("user.profile.access"):
                    setting.setValue(user_profile_access);
                    mongoSettings.add(setting);
                    break;

                case ("default.user.password"):
                    setting.setValue(default_user_password);
                    mongoSettings.add(setting);
                    break;

                case ("skyroom.api.key"):
                    setting.setValue(skyroom_api_key);
                    mongoSettings.add(setting);
                    break;

                case ("skyroom.enable"):
                    setting.setValue(skyroom_enable);
                    mongoSettings.add(setting);
                    break;

                case ("SMS.SDK"):
                    setting.setValue(SMS_SDK);
                    mongoSettings.add(setting);
                    break;

                case ("SMS.Magfa.username"):
                    setting.setValue(SMS_Magfa_username);
                    mongoSettings.add(setting);
                    break;

                case ("SMS.Magfa.password"):
                    setting.setValue(SMS_Magfa_password);
                    mongoSettings.add(setting);
                    break;


                case ("sms.validation.digits"):
                    setting.setValue(sms_validation_digits);
                    mongoSettings.add(setting);
                    break;

                case ("token.valid.SMS"):
                    setting.setValue(token_valid_SMS);
                    mongoSettings.add(setting);
                    break;
                case ("token.valid.email"):
                    setting.setValue(token_valid_email);
                    mongoSettings.add(setting);
                    break;

                case ("kavenegar.sms.api.key"):
                    setting.setValue(kavenegar_sms_api_key);
                    mongoSettings.add(setting);
                    break;

                case ("metadata.file.path"):
                    setting.setValue(metadata_file_path);
                    mongoSettings.add(setting);
                    break;

                case ("profile.photo.path"):
                    setting.setValue(profile_photo_path);
                    mongoSettings.add(setting);
                    break;

                case ("qr.devices.path"):
                    setting.setValue(qr_devices_path);
                    mongoSettings.add(setting);
                    break;

                case ("password.change.notification"):
                    if(password_change_notification.equals("on"))
                        password_change_notification = "true";
                    else
                        password_change_notification = "false";
                    setting.setValue(password_change_notification);
                    mongoSettings.add(setting);
                    break;

                case ("services.folder.path"):

                    setting.setValue(services_folder_path);
                    mongoSettings.add(setting);

                    break;

                case ("service.icon.path"):
                    setting.setValue(service_icon_path);
                    mongoSettings.add(setting);

                    break;

                case ("pwdCheckQuality"):
                    mongoSettings.add(setting);
                    break;

                case ("pwdFailureCountInterval"):
                    setting.setValue(pwd.getPwdFailureCountInterval());
                    mongoSettings.add(setting);
                    break;

                case ("pwdExpireWarning"):
                    setting.setValue(pwd.getPwdExpireWarning());
                    mongoSettings.add(setting);
                    break;

                case ("pwdGraceAuthNLimit"):
                    setting.setValue(pwd.getPwdGraceAuthNLimit());
                    mongoSettings.add(setting);
                    break;

                case ("pwdInHistory"):
                    setting.setValue(pwd.getPwdInHistory());
                    mongoSettings.add(setting);
                    break;

                case ("pwdLockout"):
                    setting.setValue(pwd.getPwdLockout());
                    mongoSettings.add(setting);
                    break;

                case ("pwdMinLength"):
                    setting.setValue(pwd.getPwdMinLength());
                    mongoSettings.add(setting);
                    break;

                case ("pwdMaxFailure"):
                    setting.setValue(pwd.getPwdMaxFailure());
                    mongoSettings.add(setting);
                    break;

                case ("pwdMaxAge"):
                    setting.setValue(pwd.getPwdMaxAge());
                    mongoSettings.add(setting);
                    break;

                case ("pwdLockoutDuration"):
                    setting.setValue(pwd.getPwdLockoutDuration());
                    mongoSettings.add(setting);
                    break;


            }
        }
        if (!mongoTemplate.collectionExists(Variables.col_properties)) {
            for (Object setting : mongoSettings) {
                mongoTemplate.save(setting, Variables.col_properties);
            }
        }

        if (!mongoTemplate.collectionExists(Variables.col_propertiesBackup)) {
            mongoTemplate.createCollection(Variables.col_propertiesBackup);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_id",0l);
            jsonObject.put("data",mongoSettings);
            mongoTemplate.save(jsonObject,Variables.col_propertiesBackup);
        }

    }
}