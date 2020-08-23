package parsso.idman.RepoImpls;

import org.json.simple.JSONArray;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Setting;
import parsso.idman.Repos.ConfigRepo;
import parsso.idman.utils.JSON.JSONencoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Service
public class ConfigRepoImpl implements ConfigRepo {
    @Override
    public String retrieveSetting() throws IOException {

        Resource resource = new ClassPathResource("application.properties");
        File file = resource.getFile();
        String fullFileName = file.getName();
        int equalIndex = fullFileName.indexOf('.');
        String system = fullFileName.substring(0, equalIndex);

        Scanner myReader = new Scanner(file);

        List<Setting> settings = parser(myReader, system);

        JSONencoder jsonEncoder = new JSONencoder(settings);

        JSONArray jsonArray = jsonEncoder.encode(settings);

        return jsonArray.toString();
    }

    @Override
    public String updateSettings(List<Setting> settings) throws IOException {

        String file_properties = "";


        for (Setting setting : settings) {

            if (file_properties=="")
                file_properties += "###"+setting.getGroup();
            else {

                if (file_properties.indexOf(setting.getGroup())==-1)
                    file_properties += "\n\n\n###" + setting.getGroup();

            }

            int index = file_properties.indexOf(setting.getGroup())+setting.getGroup().length();

            String temp = "";
            temp += "\n##"+ setting.getDescription() + "\n";
            temp += setting.getName() + "="+setting.getValue();

            file_properties=file_properties.substring(0, index) + temp + file_properties.substring(index);


            }





        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Files.copy(is, Paths.get(this.getClass().getClassLoader()+"/backup/"+date+"_application.properties"));
        } catch (IOException e) {
            // An error occurred copying the resource
        }



        try {
            //File file = new File("d:/new folder/t1.txt");

            Resource resource = new ClassPathResource("application.properties");
            File file = resource.getFile().getAbsoluteFile();


            File newFile = new File(file.getAbsolutePath());



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

            name = line.substring(0, equalIndex);
            ;

            value = line.substring(equalIndex + 1);


            setting.setName(name);

            setting.setValue(value.trim());

            setting.setDescription(description);

            setting.setGroup(groupName.trim());

            setting.setSystem(system.trim());

            settings.add(setting);

            setting = new Setting();

        }

        return settings;
    }

}
