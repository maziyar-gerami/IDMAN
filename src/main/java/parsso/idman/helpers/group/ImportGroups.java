package parsso.idman.helpers.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;
import parsso.idman.repos.GroupRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings("unchecked")
public class ImportGroups {
  private GroupRepo.Create groupCreateRepo;

  public ImportGroups(GroupRepo.Create groupCreateRepo) {
    this.groupCreateRepo = groupCreateRepo;
  }

  public JSONObject excelSheetAnalyze(String doerId, Sheet sheet, boolean hasHeader) throws ParseException {
    JSONArray jsonArray = new JSONArray();

    Iterator<Row> rowIterator = sheet.iterator();

    int count = 0;
    int nUnSuccessful = 0;
    int nGroupIdEmpty = 0;

    if (hasHeader)
      rowIterator.next();

    List<JSONObject> invalidGroups = new LinkedList<>();
    List<JSONObject> invalidParameter = new LinkedList<>();
    List<JSONObject> repetitiveGroups = new LinkedList<>();

    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      count++;

      Group group = new Group();

      Cell cell = row.getCell(0);
      // Check the cell type and format accordingly

      if (cell == null)
        break;

      DataFormatter formatter = new DataFormatter();
      group.setId(formatter.formatCellValue(row.getCell(0)));
      group.setName(formatter.formatCellValue(row.getCell(1)));
      group.setDescription(formatter.formatCellValue(row.getCell(2)));

      if (!group.getId().equals("")) {

        if (group.getId() == null || group.getId().equals("")) {
          if (group.getName() == null || group.getName().equals(""))
            continue;

          nGroupIdEmpty++;
          nUnSuccessful++;
          continue;
        }
        HttpStatus httpStatus = null;
        ;
        try {
          httpStatus = groupCreateRepo.create(doerId, group);
        } catch (Exception e) {
          nUnSuccessful++;
          continue;
        }

        if (httpStatus == HttpStatus.FOUND) {
          ObjectMapper mapper = new ObjectMapper();
          try {
            String jsonString = mapper.writeValueAsString(group);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonString);
            repetitiveGroups.add(json);
            nUnSuccessful++;
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        }

        continue;

      }
    }

    JSONObject finalJson = new JSONObject();
    finalJson.put("invalidGroups", invalidGroups);
    finalJson.put("repetitiveGroups", repetitiveGroups);
    finalJson.put("count", count);
    finalJson.put("nUnSuccessful", nUnSuccessful);
    finalJson.put("nSuccessful", count - nUnSuccessful);
    finalJson.put("nRepetitive", repetitiveGroups.size());
    finalJson.put("nInvalidGroups", invalidGroups.size());
    finalJson.put("nEssentialParameterInvalid", invalidParameter.size());
    finalJson.put("nUserIdEmpty", nGroupIdEmpty);

    finalJson.put("list", jsonArray);

    return finalJson;
  }

  public JSONObject compareGroups(User oldUser, User newUser) {

    List<String> conflicts = new LinkedList<>();

    if (oldUser.get_id().equals(newUser.get_id()))
      conflicts.add("userId");
    if (oldUser.getFirstName().equals(newUser.getFirstName()))
      conflicts.add("firsName");
    if (oldUser.getLastName().equals(newUser.getLastName()))
      conflicts.add("lastName");
    if (oldUser.getDisplayName().equals(newUser.getDisplayName()))
      conflicts.add("displayName");
    if (oldUser.getMail().equals(newUser.getMail()))
      conflicts.add("mail");
    if (oldUser.getMobile().equals(newUser.getMobile()))
      conflicts.add("mobile");
    if (oldUser.getDescription() != null && oldUser.getDescription().equals(newUser.getDescription()))
      conflicts.add("description");
    if (oldUser.isEnabled() == (newUser.isEnabled()))
      conflicts.add("status");

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("old", oldUser);
    jsonObject.put("new", newUser);
    jsonObject.put("conflicts", conflicts);

    return jsonObject;
  }

  public JSONObject csvSheetAnalyze(String doerId, BufferedReader sheet, boolean hasHeader)
      throws IOException {

    String row;
    JSONArray jsonArray = new JSONArray();
    int i = 0;
    int count = 0;
    int nUnSuccessful = 0;
    int nGroupIdEmpty = 0;

    while ((row = sheet.readLine()) != null) {
      if (i == 0 && hasHeader) {
        i++;

        continue;
      }

      String[] data = row.split(",");
      // do something with the data

      Group group = new Group();

      group.setId(data[0]);
      group.setName(data[1]);
      group.setDescription(data[2]);

      i++;

      if (!group.getId().equals("")) {

        if (group.getId() == null || group.getId().equals("")) {
          if (group.getName() == null || group.getName().equals(""))
            continue;

          nGroupIdEmpty++;
          nUnSuccessful++;
          continue;
        }
        try {
          groupCreateRepo.create(doerId, group);
        } catch (Exception e) {
          nUnSuccessful++;
          continue;
        }

        count++;

      }
      count++;

    }

    JSONObject finalJson = new JSONObject();
    finalJson.put("count", count);
    finalJson.put("nUnSuccessful", nUnSuccessful);
    finalJson.put("groupIdEmpty", nGroupIdEmpty);
    finalJson.put("nSuccessful", count - nUnSuccessful);
    finalJson.put("repUser", jsonArray);

    return finalJson;
  }

  List<String> extractGroups(String strMain) {
    String[] arrSplit = (strMain.split(","));
    List<String> ls = new LinkedList<>();
    for (String s : arrSplit)
      ls.add(s.trim());
    return ls;
  }

  public JSONObject importFileGroups(String doerId, MultipartFile file, boolean hasHeader)
      throws IOException, ParseException {

    JSONObject lsGroups = new JSONObject();
    InputStream insfile = file.getInputStream();

    if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
      // Create Workbook instance holding reference to .xlsx file
      XSSFWorkbook workbookXLSX = new XSSFWorkbook(insfile);

      // Get first/desired sheet from the workbook
      XSSFSheet sheet = workbookXLSX.getSheetAt(0);

      workbookXLSX.close();

      lsGroups = excelSheetAnalyze(doerId, sheet, hasHeader);

    } else if (file.getOriginalFilename().endsWith(".xls")) {
      HSSFWorkbook workbookXLS;

      workbookXLS = new HSSFWorkbook(insfile);

      HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

      workbookXLS.close();

      lsGroups = excelSheetAnalyze(doerId, xlssheet, hasHeader);

    } else if (file.getOriginalFilename().endsWith(".csv")) {

      BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

      lsGroups = csvSheetAnalyze(doerId, csvReader, hasHeader);

      csvReader.close();
    } else if (file.getOriginalFilename().endsWith(".ldif")) {

      final LDIFReader ldifReader = new LDIFReader(insfile);

      lsGroups = ldifAnalayze(ldifReader);
    }

    return lsGroups;
  }

  private JSONObject ldifAnalayze(LDIFReader ldifReader) {
    Entry entry;
    while (true) {
      try {
        entry = ldifReader.readEntry();

        if (entry == null) {
          break;
        }

        extractAttrEntry(entry);
      } catch (IOException | LDIFException ldifE) {
        // errorCount++;
        ldifE.printStackTrace();
        break;
      }
    }
    return null;
  }

  private void extractAttrEntry(Entry entry) {

    Group group = new Group();

    group.setId(entry.getAttributeValue("uid"));
    group.setName(entry.getAttributeValue("name"));
    group.setDescription(entry.getAttributeValue("description"));

  }
}
