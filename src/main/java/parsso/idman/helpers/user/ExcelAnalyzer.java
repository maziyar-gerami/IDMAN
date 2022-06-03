package parsso.idman.helpers.user;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtyoes")
public class ExcelAnalyzer {
  final UsersRetrieveRepo usersOpRetrieve;
  final UsersUpdateRepo usersOpUpdate;

  public ExcelAnalyzer(
      UsersRetrieveRepo usersOpRetrieve, UsersUpdateRepo usersOpUpdate) {
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
  }

  public List<String> excelSheetAnalyze(String doer, Sheet sheet, String ou, boolean hasHeader) {

    Iterator<Row> rowIterator = sheet.iterator();

    List<String> notExist = new LinkedList<>();

    if (hasHeader == true)
      rowIterator.next();

    DataFormatter formatter = new DataFormatter();

    while (rowIterator.hasNext()) {

      Row row = rowIterator.next();

      String tempUID = formatter.formatCellValue(row.getCell(0));

      User user;

      if (tempUID != null
          && !tempUID.equals("")) {
        user = usersOpRetrieve.retrieveUsers(tempUID);

        if (user == null) {
          notExist.add(tempUID);
          continue;
        }

        if (user.getMemberOf() == null) {
          List<String> ous = new LinkedList<>();
          ous.add(ou);
          user.setMemberOf(ous);
        } else {
          if (!user.getMemberOf().contains(ou))
            user.getMemberOf().add(ou);
          else
            continue;

        }
        usersOpUpdate.update(doer, tempUID, user);
      }

    }

    return notExist;
  }

  @SuppressWarnings("rawtypes")
  public List csvSheetAnalyzer(String doer, BufferedReader sheet, String ou, boolean hasHeader) throws IOException {

    String row;
    int i = 0;
    List<String> notExist = new LinkedList<>();

    while ((row = sheet.readLine()) != null) {
      String[] data = row.split(",");

      if (i == 0 && hasHeader || data.length == 0 || data[0] == null || data[0].equals("")) {
        i++;
        continue;
      }

      User user = usersOpRetrieve.retrieveUsers(data[0]);
      if (user == null) {
        notExist.add(data[0]);
        continue;
      }
      if (user.getMemberOf() == null) {
        List<String> ous = new LinkedList<>();
        ous.add(ou);
        user.setMemberOf(ous);
      } else {
        if (!user.getMemberOf().contains(ou))
          user.getMemberOf().add(ou);
        else
          continue;
      }
      usersOpUpdate.update(doer, data[0], user);

      i++;

    }
    return notExist;
  }
}
