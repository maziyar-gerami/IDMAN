package parsso.idman.Helpers.User;

import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ImportUsers {

    @Autowired
    private UserRepo userRepo;

    public JSONObject excelSheetAnalyze(Sheet sheet, int[] sequence, boolean hasHeader) {
        JSONArray jsonArray = new JSONArray();

        Iterator<Row> rowIterator = sheet.iterator();

        int count=0;
        int nUnSuccessful =0;

        if (hasHeader == true) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            User user = new User();

            JSONObject temp;

            Cell cell = row.getCell(0);
            //Check the cell type and format accordingly

            if (cell == null) break;

            DataFormatter formatter = new DataFormatter();
            user.setUserId(formatter.formatCellValue(row.getCell(sequence[0])));
            user.setFirstName(formatter.formatCellValue(row.getCell(sequence[1])));
            user.setLastName(formatter.formatCellValue(row.getCell(sequence[2])));
            user.setDisplayName(formatter.formatCellValue(row.getCell(sequence[3])));
            user.setMobile(formatter.formatCellValue(row.getCell(sequence[4])));
            user.setMail(formatter.formatCellValue(row.getCell(sequence[5])));
            user.setMemberOf(extractGroups(formatter.formatCellValue(row.getCell(sequence[6]))));
            user.setDescription(formatter.formatCellValue(row.getCell(sequence[7])));
            user.setStatus(formatter.formatCellValue(row.getCell(sequence[8])));
            user.setEmployeeNumber(formatter.formatCellValue(row.getCell(sequence[9])));
            user.setUserPassword(formatter.formatCellValue(row.getCell(sequence[10])));

            temp = userRepo.createUserImport(user);

            if (temp.size()>0) {
                jsonArray.add(temp);
                nUnSuccessful++;
            }

            count++;
        }

        JSONObject finalJson = new JSONObject();
        finalJson.put("count",count);
        finalJson.put("nUnSuccessful",nUnSuccessful);
        finalJson.put("nSuccessful",count-nUnSuccessful);

        finalJson.put("list",jsonArray);

        return finalJson;
    }

    public JSONObject compareUsers(User oldUser, User newUser) {

        List<String> conflicts = new LinkedList<>();

        if (oldUser.getUserId().equals(newUser.getUserId())) conflicts.add("userId");
        if (oldUser.getFirstName().equals(newUser.getFirstName())) conflicts.add("firsName");
        if (oldUser.getLastName().equals(newUser.getLastName())) conflicts.add("lastName");
        if (oldUser.getDisplayName().equals(newUser.getDisplayName())) conflicts.add("displayName");
        if (oldUser.getMail().equals(newUser.getMail())) conflicts.add("mail");
        if (oldUser.getMobile().equals(newUser.getMobile())) conflicts.add("mobile");
        if (oldUser.getDescription().equals(newUser.getDescription())) conflicts.add("description");
        if (oldUser.isEnabled() == (newUser.isEnabled())) conflicts.add("status");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("old", oldUser);
        jsonObject.put("new", newUser);
        jsonObject.put("conflicts", conflicts);

        return jsonObject;
    }

    public JSONObject csvSheetAnalyze(BufferedReader sheet, int[] sequence, boolean hasHeader) throws IOException {

        String row;
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        int count=0;
        int nUnSuccessful =0;

        while ((row = sheet.readLine()) != null) {
            if (i == 0 && hasHeader) {
                i++;

                continue;
            }

            String[] data = row.split(",");
            // do something with the data

            User user = new User();

            user.setUserId(data[sequence[0]]);
            user.setFirstName(data[sequence[1]]);
            user.setLastName(data[sequence[2]]);
            user.setDisplayName(data[sequence[3]]);
            user.setMobile(data[sequence[4]]);
            user.setMail(data[sequence[5]]);
            user.setMemberOf(extractGroups(data[sequence[6]]));
            user.setDescription((data[sequence[7]]));
            user.setStatus(data[sequence[8]]);
            user.setEmployeeNumber(data[sequence[9]]);
            user.setUserPassword((data[sequence[10]]));

            i++;

            JSONObject temp = userRepo.createUserImport(user);

            if (temp.size()>0) {
                jsonArray.add(temp);
                nUnSuccessful++;
            }

            count++;
        }

        JSONObject finalJson = new JSONObject();
        finalJson.put("count",count);
        finalJson.put("nUnSuccessful",nUnSuccessful);
        finalJson.put("nSuccessful",count-nUnSuccessful);
        finalJson.put("list",jsonArray);

        return finalJson;
    }

    List<String> extractGroups(String strMain) {
        String[] arrSplit = (strMain.split(","));
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < arrSplit.length; i++)
            ls.add(arrSplit[i].trim());
        return ls;
    }

    public JSONObject importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException {

        JSONObject lsusers = new JSONObject();
        InputStream insfile = file.getInputStream();

        if (file.getOriginalFilename().endsWith(".xlsx")) {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbookXLSX = null;
            workbookXLSX = new XSSFWorkbook(insfile);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbookXLSX.getSheetAt(0);

            lsusers = excelSheetAnalyze(sheet, sequence, hasHeader);

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS = null;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            lsusers = excelSheetAnalyze(xlssheet, sequence, hasHeader);

        } else if (file.getOriginalFilename().endsWith(".csv")) {

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            lsusers = csvSheetAnalyze(csvReader, sequence, hasHeader);

            csvReader.close();
        } else if (file.getOriginalFilename().endsWith(".ldif")) {

            final LDIFReader ldifReader = new LDIFReader(insfile);

            lsusers = ldifAnalayze(ldifReader, sequence, hasHeader);
        }

        return lsusers;
    }

    private JSONObject ldifAnalayze(LDIFReader ldifReader, int[] sequence, boolean hasHeader) {
        Entry entry = null;
        while (true) {
            try {
                entry = ldifReader.readEntry();

                if (entry == null) {
                    break;
                }

                extractAttrEntry(entry);
            } catch (IOException | LDIFException ldifE) {
                //errorCount++;
                ldifE.printStackTrace();
                break;
            }
        }
        return null;
    }

    private List<User> extractAttrEntry(Entry entry) {

        List<User> lsUserConflicts = new LinkedList();

        User user = new User();

        user.setUserId(entry.getAttributeValue("uid"));
        user.setFirstName(entry.getAttributeValue("givenName"));
        user.setLastName(entry.getAttributeValue("sn"));
        user.setDisplayName(entry.getAttributeValue("displayName"));
        user.setMobile(entry.getAttributeValue("mobile"));
        user.setMail(entry.getAttributeValue("mail"));
        int nGroups = (null == entry.getAttributeValue("ou") ? 0 : entry.getAttributeValue("ou").length());
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < nGroups; i++) ls.add(entry.getAttributeValue("ou"));
        user.setMemberOf(null != entry.getAttributeValue("ou") ? ls : null);
        user.setDescription(entry.getAttributeValue("description"));
        user.setPhotoName(entry.getAttributeValue("status"));
        user.setStatus(entry.getAttributeValue("employeeNumber"));
        user.setPhotoName(entry.getAttributeValue("userPassword"));

        return lsUserConflicts;
    }
}