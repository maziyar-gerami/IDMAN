package parsso.idman.repoImpls;


import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Config;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.logs.Setting;
import parsso.idman.models.other.Time;
import parsso.idman.utils.convertor.DateConverter;
import parsso.idman.utils.json.JSONencoder;
import parsso.idman.repos.ConfigRepo;
import parsso.idman.repos.UserRepo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
@Service
public class ConfigRepoImpl implements ConfigRepo {
    PasswordSettings passwordSettings;
    MongoTemplate mongoTemplate;
    InstantMessage instantMessage;
    UserRepo userRepo;
    UniformLogger uniformLogger;
    private final ApplicationContext appContext;
    @Value("${external.config}")
    private String pathToProperties;
    @Value("${external.config.backup}")
    private String backUpOfProperties;
    @Value("${backup.path}")
    private String backUpPath;

    @Autowired
    public ConfigRepoImpl(UniformLogger uniformLogger, UserRepo userRepo,ApplicationContext appContext,
                          InstantMessage instantMessage, MongoTemplate mongoTemplate,PasswordSettings passwordSettings) {
        this.uniformLogger = uniformLogger;
        this.userRepo = userRepo;
        this.appContext = appContext;
        this.instantMessage = instantMessage;
        this.mongoTemplate = mongoTemplate;
        this.passwordSettings = passwordSettings;
    }

    public static List<Setting> parser(Scanner reader, String system) {

        Setting setting = new Setting();
        List<Setting> settings = new LinkedList<>();
        String groupName = null;
        String description = null;
        String name;
        String value;

        String line;

        while (reader.hasNextLine()) {

            line = reader.nextLine();

            while (line.equals("")) {
                settings.add(setting);
                setting = new Setting();

                if (reader.hasNextLine()) line = reader.nextLine();
                else return settings;
            }

            if (line.charAt(0) == '#') {

                if (line.charAt(1) == '#') {

                    if (line.charAt(2) == '#') {

                        groupName = line.substring(3);

                        continue;

                    }

                    description = line.substring(2);

                    continue;

                }

                continue;

            }

            int equalIndex = line.indexOf('=');

            if (equalIndex > 0) {
                name = line.substring(0, equalIndex);

                value = line.substring(equalIndex + 1);

                setting.setName(name);

                setting.setValue(value.trim());

                setting.setDescription(description);

                setting.setGroupEN(Objects.requireNonNull(groupName).trim());

                setting.setSystem(system.trim());

                settings.add(setting);

            }
            setting = new Setting();


        }

        return settings;
    }


    @Override
    public String retrieveSetting() throws IOException {

        Resource resource = appContext.getResource("file:" + pathToProperties);
        File file = resource.getFile();
        String fullFileName = file.getName();
        int equalIndex = fullFileName.indexOf('.');
        String system = fullFileName.substring(0, equalIndex);

        Scanner myReader = new Scanner(file);

        List<Setting> settings = parser(myReader, system);

        JSONencoder jsonEncoder = new JSONencoder(settings);

        JSONArray jsonArray = jsonEncoder.encode(settings);

        myReader.close();

        return jsonArray.toString();
    }

    @Override
    public boolean updateSettings(String doerID, List<Setting> settings) {

        StringBuilder file_properties = new StringBuilder();

        for (Setting setting : settings) {

            if (file_properties.toString().equals(""))
                file_properties.append("###").append(setting.getGroupEN());
            else {

                if (!file_properties.toString().contains(setting.getGroupEN()))
                    file_properties.append("\n\n\n###").append(setting.getGroupEN());

            }

            int index = file_properties.indexOf(setting.getGroupEN()) + setting.getGroupEN().length();

            String temp = "";
            temp += "\n##" + setting.getDescription() + "\n";
            temp += setting.getName() + "=" + setting.getValue();

            file_properties = new StringBuilder(file_properties.substring(0, index) + temp + file_properties.substring(index));

        }

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Files.copy(Objects.requireNonNull(is), Paths.get(this.getClass().getClassLoader() + "/backup/" + date + "_application.properties"));
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_CONFIG, "", "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, settings, ""));

        } catch (IOException e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_CONFIG, "", "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, settings, "unknown error"));

        }

        try {

            File newFile = new File(pathToProperties);

            if(newFile.createNewFile())
                return false;

            FileWriter fw = new FileWriter(newFile);

            BufferedWriter out = new BufferedWriter(fw);

            out.write(file_properties.toString());
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;

    }

    @Override
    public HttpStatus backupConfig() {

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        Path copied = Paths.get(pathToProperties);
        String s = backUpPath + date + "_application.properties";

        if(new File(backUpPath).mkdirs())
            return HttpStatus.FORBIDDEN;

        Path originalPath = Paths.get(s);
        try {
            Files.copy(copied, originalPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpStatus.OK;

    }

    @Override
    public HttpStatus factoryReset(String doerID) throws IOException {

        Path copied = Paths.get(pathToProperties);
        Resource resource = new ClassPathResource(backUpOfProperties);
        File file = resource.getFile().getAbsoluteFile();
        Path originalPath = Paths.get(file.getAbsolutePath());
        try {
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_CONFIG, "", "", Variables.ACTION_RESET, Variables.RESULT_SUCCESS, ""));

        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_CONFIG, "", "", Variables.ACTION_RESET, Variables.RESULT_FAILED, ""));

        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus restore(String doerID, String name) {

        List<Config> configs = listBackedUpConfigs();

        for (Config config : configs) {
            if (config.getName().equals(name)) {

                Path copied = Paths.get(pathToProperties);
                String s = backUpPath + config.getName();
                Path originalPath = Paths.get(s);
                try {
                    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
                    uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_CONFIG, config.getName(), "",
                            Variables.ACTION_RESTORE, Variables.RESULT_SUCCESS, config.getName(), ""));

                } catch (Exception e) {
                    e.printStackTrace();
                    uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_CONFIG, config.getName(), "",
                            Variables.ACTION_RESTORE, Variables.RESULT_FAILED, config.getName(), "Restoring file"));

                }
                return HttpStatus.OK;
            }
        }
        return HttpStatus.OK;
    }

    @Override
    public List<Config> listBackedUpConfigs() {
        File folder = new File(backUpPath); // ./services/
        String[] files = folder.list();
        List<Config> configs = new LinkedList<>();
        Config config = null;
        if (files != null)
            for (String file : files) {
                if (file.endsWith(".properties"))
                    config = new Config();

                try {

                    Objects.requireNonNull(config).setName(file);
                    SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

                    Date date = parserSDF.parse(file);

                    Calendar myCal = new GregorianCalendar();
                    myCal.setTime(date);

                    DateConverter dateConverter = new DateConverter();

                    dateConverter.gregorianToPersian(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));

                    int inPersianDay = dateConverter.getDay();
                    int inPersianMonth = dateConverter.getMonth();
                    int inPersianYear = dateConverter.getYear();

                    Time time = new Time(
                            inPersianYear,
                            inPersianMonth,
                            inPersianDay,

                            myCal.get(Calendar.HOUR_OF_DAY),
                            myCal.get(Calendar.MINUTE),
                            myCal.get(Calendar.SECOND)
                    );

                    config.setDateTime(time);
                    Resource resource = new ClassPathResource(backUpOfProperties);
                    File filetemp = resource.getFile().getAbsoluteFile();
                    Scanner myReader = new Scanner(filetemp);

                    int dot = file.indexOf('.');
                    String system = file.substring(20, dot);

                    List<Setting> settings = parser(myReader, system);
                    config.setSettingList(settings);

                } catch (Exception e) {
                    continue;
                }
                configs.add(config);
            }
        return configs;
    }

    @Override
    public HttpStatus saveToMongo() throws IOException {

        Resource resource = appContext.getResource("file:" + pathToProperties);
        File file = resource.getFile();
        String fullFileName = file.getName();
        int equalIndex = fullFileName.indexOf('.');
        String system = fullFileName.substring(0, equalIndex);

        Scanner myReader = new Scanner(file);

        List<Setting> settings = parser(myReader, system);

        for (Setting setting : settings)
            if (setting.getGroupEN() != null)
                mongoTemplate.save(setting, Variables.col_properties);


        return HttpStatus.OK;
    }

}
