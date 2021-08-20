package parsso.idman.Helpers.User;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.UserRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExcelAnalyzer {

    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepo userRepo;

    public List excelSheetAnalyze(String doer, Sheet sheet, String ou, boolean hasHeader) throws IOException, ParseException {

        Iterator<Row> rowIterator = sheet.iterator();

        List<String> notExist = new LinkedList<>();

        if (hasHeader == true) rowIterator.next();

        DataFormatter formatter = new DataFormatter();


        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();

            String tempUID = formatter.formatCellValue(row.getCell(0));

            User user;

            if (tempUID != null
                    && !tempUID.equals("")) {
                user = userRepo.retrieveUsers(tempUID);

                if (user == null) {
                    continue;
                }

                if (user.getMemberOf() == null) {
                    List<String> ous = new LinkedList<>();
                    ous.add(ou);
                    user.setMemberOf(ous);
                } else
                    user.getMemberOf().add(ou);
                userRepo.update(doer, tempUID, user);
            }

        }

        return notExist;
    }

    /*public List csvSheetAnalyze(BufferedReader sheet, String ou, boolean hasHeader) throws IOException {

        String row;
        int i = 0;
        List<String> notExist = new LinkedList<>();

        while ((row = sheet.readLine()) != null) {
            if (i == 0 && hasHeader) {
                i++;
                continue;
            }

            ModificationItem[] items = new ModificationItem[1];
            Attribute[] attrs = new Attribute[1];

            String[] data = row.split(",");

            attrs[0] = new BasicAttribute("ou", ou);
            items[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attrs[0]);

            try {
                ldapTemplate.modifyAttributes(new BuildDnUser().buildDn(data[0]), items);

            } catch (Exception e) {
                if (e.getClass().toString().contains("NameNotFoundException"))
                    notExist.add(data[0]);
            }

            i++;

        }
        return notExist;
    }*/

    public List csvSheetAnalyzer(String doer, BufferedReader sheet, String ou, boolean hasHeader) throws IOException, ParseException {

        String row;
        int i = 0;
        List<String> notExist = new LinkedList<>();

        while ((row = sheet.readLine()) != null) {
            String[] data = row.split(",");

            if (i == 0 && hasHeader || data == null || data.length == 0 || data[0] == null || data[0].equals("")) {
                i++;
                continue;
            }


            User user = userRepo.retrieveUsers(data[0]);
            user.getMemberOf().add(ou);
            userRepo.update(doer, data[0], user);

            i++;

        }
        return notExist;
    }
}
