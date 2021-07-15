package parsso.idman.RepoImpls;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Config;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Logs.Setting;
import parsso.idman.Models.Time;
import parsso.idman.Repos.ConfigRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Convertor.DateConverter;
import parsso.idman.Utils.JSON.JSONencoder;

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

    @Autowired
    PasswordSettings passwordSettings;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    InstantMessage instantMessage;
    @Autowired
    UserRepo userRepo;
    String model = "Config";
    @Autowired
    private ApplicationContext appContext;
    @Value("${external.config}")
    private String pathToProperties;
    @Value("${external.config.backup}")
    private String backUpOfProperties;
    @Value("${backup.path}")
    private String backUpPath;

    public static List<Setting> parser(Scanner reader, String system) {

        Setting setting = new Setting();
        List<Setting> settings = new LinkedList<>();
        String groupName = null;
        String description = null;
        String name = null;
        String value = null;

        String line;

        while (reader.hasNextLine()) {

            line = reader.nextLine();

            while (line.equals("")) {
                if (!(setting.equals(null))) {
                    settings.add(setting);
                    setting = new Setting();
                }

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

            if (equalIndex>0) {
                name = line.substring(0, equalIndex);

                value = line.substring(equalIndex + 1);

                setting.setName(name);

                setting.setValue(value.trim());

                setting.setDescription(description);

                setting.setGroup(groupName.trim());

                setting.setSystem(system.trim());

                settings.add(setting);

            }
            setting = new Setting();


        }

        return settings;
    }

    @Override
    public List<Setting> retrieveTFSetting() throws IOException {

        Resource resource = appContext.getResource("file:" + pathToProperties);
        File file = resource.getFile();
        String fullFileName = file.getName();
        int equalIndex = fullFileName.indexOf('.');
        String system = fullFileName.substring(0, equalIndex);

        Scanner myReader = new Scanner(file);

        List<Setting> allSettings = parser(myReader, system);

        List<Setting> relatedSettings = new LinkedList<>();

        for (Setting setting : allSettings) {
            if (setting.getValue() != null && (setting.getValue().equalsIgnoreCase("true") || setting.getValue().equalsIgnoreCase("false")))
                relatedSettings.add(setting);
        }

        return relatedSettings;
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
    public String updateSettings(String doerID, List<Setting> settings) {

        Logger logger = LogManager.getLogger(doerID);

        String file_properties = "";

        for (Setting setting : settings) {

            if (file_properties == "")
                file_properties += "###" + setting.getGroup();
            else {

                if (file_properties.indexOf(setting.getGroup()) == -1)
                    file_properties += "\n\n\n###" + setting.getGroup();

            }

            int index = file_properties.indexOf(setting.getGroup()) + setting.getGroup().length();

            String temp = "";
            temp += "\n##" + setting.getDescription() + "\n";
            temp += setting.getName() + "=" + setting.getValue();

            file_properties = file_properties.substring(0, index) + temp + file_properties.substring(index);

        }


        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Files.copy(is, Paths.get(this.getClass().getClassLoader() + "/backup/" + date + "_application.properties"));
            logger.warn(new ReportMessage(model, "", "", "update", "success", "").toString());

        } catch (IOException e) {
            logger.warn(new ReportMessage(model, "", "", "update", "failed", "unknown error").toString());

        }

        try {

            File newFile = new File(pathToProperties);

            newFile.createNewFile();

            FileWriter fw = new FileWriter(newFile);

            BufferedWriter out = new BufferedWriter(fw);

            out.write(file_properties);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return file_properties;
    }

    @Override
    public HttpStatus backupConfig() {

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        Path copied = Paths.get(pathToProperties);
        String s = backUpPath + date + "_application.properties";
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

        Logger logger = LogManager.getLogger(doerID);

        Path copied = Paths.get(pathToProperties);
        Resource resource = new ClassPathResource(backUpOfProperties);
        File file = resource.getFile().getAbsoluteFile();
        Path originalPath = Paths.get(file.getAbsolutePath());
        try {
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
            logger.warn(new ReportMessage(model, "", "", "resetFactory", "success", "").toString());

        } catch (Exception e) {
            logger.warn(new ReportMessage(model, "", "", "resetFactory", "failed", "").toString());

        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus restore(String doerID, String name) throws IOException, ParseException {
        Logger logger = LogManager.getLogger(doerID);

        List<Config> configs = listBackedUpConfigs();

        for (Config config : configs) {
            if (config.getName().equals(name)) {

                Path copied = Paths.get(pathToProperties);
                String s = backUpPath + config.getName();
                Path originalPath = Paths.get(s);
                try {
                    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
                    logger.warn(new ReportMessage(model, config.getName(), "", "restore", "success", ""));

                } catch (Exception e) {
                    logger.warn(new ReportMessage(model, config.getName(), "", "restore", "failed", "config " + config.getName()).toString());

                }
                return HttpStatus.OK;
            }
        }
        return HttpStatus.OK;
    }

    @Override
    public List<Config> listBackedUpConfigs() throws IOException, ParseException {
        File folder = new File(backUpPath); // ./services/
        String[] files = folder.list();
        List<Config> configs = new LinkedList<>();
        Config config = null;
        for (String file : files) {
            if (file.endsWith(".properties"))
                config = new Config();

            try {

                config.setName(file);
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

        for (Setting setting : settings) {
            if (setting.getGroup() != null) {
                setting.setChangable(!setting.getGroup().equalsIgnoreCase("Main"));


                mongoTemplate.save(setting, Variables.col_properties);
            }
        }

        return HttpStatus.OK;
    }

}
