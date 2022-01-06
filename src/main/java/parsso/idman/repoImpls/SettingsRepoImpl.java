package parsso.idman.repoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.SettingsRepo;

import java.util.List;

@Service
public class SettingsRepoImpl implements SettingsRepo {
    final PasswordSettings passwordSettings;
    final MongoTemplate mongoTemplate;

    @Autowired
    SettingsRepoImpl(PasswordSettings passwordSettings,MongoTemplate mongoTemplate){
        this.passwordSettings = passwordSettings;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Setting> retrieve() {

        List<Setting> settings = mongoTemplate.find(new Query(),Setting.class, Variables.col_properties);
        PWD pwd = passwordSettings.retrieve();
        for (Setting setting:settings)
            if (setting.getGroupEN().equalsIgnoreCase("Password")&&setting.getValue()==null){
                switch (setting.get_id()){
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
        return settings;
    }
}
