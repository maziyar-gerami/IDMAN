package parsso.idman.repoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        List finalSettings = new LinkedList();
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


        }

        Setting sms_sdk = mongoTemplate.findOne(new Query(Criteria.where("_id").is("SMS.SDK")), Setting.class, Variables.col_properties);

        if (sms_sdk.getValue().equalsIgnoreCase("magfa"))
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("kavenegar.sms.api.key"));

        else if (sms_sdk.getValue().equalsIgnoreCase("kavenegar")) {
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("SMS.Magfa.username"));
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("SMS.Magfa.password"));
        }

        boolean skyroom = Boolean.parseBoolean(mongoTemplate.findOne(new Query(Criteria.where("_id").is("skyroom.enable")), Setting.class, Variables.col_properties).getValue());

        if (!skyroom)
            settings.removeIf(s -> s.get_id().equalsIgnoreCase("skyroom.api.key"));



        return settings;
    }

    @Override
    public HttpStatus update(List<Property> properties) {

        List<Property> ldapProperties = new ArrayList<>();
        Property[] pp = (Property[]) properties.stream().filter(p -> p.getGroup().equalsIgnoreCase("Password")).toArray();
        Collections.addAll(ldapProperties, pp);
        try {
            mongoTemplate.save(properties, Variables.col_properties);
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
}
