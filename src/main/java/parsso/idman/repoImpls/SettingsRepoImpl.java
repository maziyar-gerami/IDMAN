package parsso.idman.repoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.SettingsRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SettingsRepoImpl implements SettingsRepo {
    final PasswordSettings passwordSettings;
    final MongoTemplate mongoTemplate;
    final LdapTemplate ldapTemplate;

    @Autowired
    SettingsRepoImpl(PasswordSettings passwordSettings, MongoTemplate mongoTemplate, LdapTemplate ldapTemplate) {
        this.passwordSettings = passwordSettings;
        this.mongoTemplate = mongoTemplate;
        this.ldapTemplate = ldapTemplate;
    }


    @Override
    public List<Setting> retrieve() {

        List<Setting> settings = mongoTemplate.find(new Query(), Setting.class, Variables.col_properties);
        PWD pwd = passwordSettings.retrieve();
        List<Setting> finalSettings = new LinkedList();
        for (Setting setting : settings) {
            if (setting.getGroupEN().equalsIgnoreCase("Password") && setting.getValue() == null) {
                switch (setting.get_id()) {
                    case ("pwdCheckQuality"):
                        setting.setValue(pwd.getPwdCheckQuality());
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
                        setting.setValue(pwd.getPwdMinLength());
                        break;
                }

            }
            /*
            else if (setting.getGroupEN().equalsIgnoreCase("Storage"))
                setting.setValue(((List) setting.getValue()).get(((List) setting.getValue()).size()-1).toString());

             */
        }

        Setting sms_sdk = mongoTemplate.findOne(new Query(Criteria.where("_id").is("SMS.SDK")), Setting.class, Variables.col_properties);

        if (sms_sdk.getValue().toString().equalsIgnoreCase("magfa"))
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("kavenegar.sms.api.key"));

        else if (sms_sdk.getValue().toString().equalsIgnoreCase("kavenegar")) {
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("SMS.Magfa.username"));
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("SMS.Magfa.password"));
        }

        boolean skyroom = Boolean.parseBoolean(mongoTemplate.findOne(new Query(Criteria.where("_id").is("skyroom.enable")), Setting.class, Variables.col_properties).getValue().toString());

        if (!skyroom)
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("skyroom.api.key"));



        return settings;
    }

    @Override
    public HttpStatus update(List<Property> properties) {
        final List<Setting> allSettings = retrieve();
        List<Property> ldapProperties = new ArrayList<>();
        List<Property> mongoProperties = new ArrayList<>();
        List<Property> storageProperties = new ArrayList<>();
        for (Property property:properties) {
            if (property.get_id().equals("pwdCheckQuality") ||
                    property.get_id().equals("pwdFailureCountInterval") ||
                    property.get_id().equals("pwdGraceAuthNLimit") ||
                    property.get_id().equals("pwdInHistory") ||
                    property.get_id().equals("pwdLockout")||
                    property.get_id().equals("pwdMinLength")||
                    property.get_id().equals("pwdMaxFailure") ||
                    property.get_id().equals("pwdMaxAge")||
                    property.get_id().equals("pwdLockoutDuration")||
                    property.get_id().equals("pwdExpireWarning"))
                ldapProperties.add(property);
            else if(property.get_id().equals("metadata.file.path") ||
                    property.get_id().equals("profile.photo.path") ||
                    property.get_id().equals("services.folder.path") ||
                    property.get_id().equals("service.icon.path")){
                storageProperties.add(property);
            }
            else
                mongoProperties.add(property);
        }

        for (Property property:mongoProperties) {
            Update update = new Update();
            update.set("value",property.getValue());
            new Thread(() -> {
                mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(property.get_id())),update, Variables.col_properties);
            }).start();
        }

        /*
        for (Property property:storageProperties) {
            Setting s = allSettings.stream().filter(o-> o.get_id().equalsIgnoreCase(property.get_id())).collect(Collectors.toList()).get(0);
            List values = (List) s.getValue();
            if (!values.contains(property.getValue()))
                values.add(s.getValue());

            Update update = new Update();
            update.set("value",values);

            new Thread(() -> {
                mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(property.get_id())),update, Variables.col_properties);
            }).start();
        }

         */

        try {
            passwordSettings.update(ldapProperties);
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}
