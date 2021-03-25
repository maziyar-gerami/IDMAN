package parsso.idman.Helpers.User;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExcelAnalyzer {

    @Autowired
    LdapTemplate ldapTemplate;

    public List excelSheetAnalyze(Sheet sheet, String ou, boolean hasHeader) {

        Iterator<Row> rowIterator = sheet.iterator();

        List<String> notExist = new LinkedList<>();

        if (hasHeader == true) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Cell cell = row.getCell(0);
            //Check the cell type and format accordingly
            if (cell == null) break;

            ModificationItem[] items = new ModificationItem[1];
            Attribute[] attrs = new Attribute[1];

            DataFormatter formatter = new DataFormatter();

            attrs[0] = new BasicAttribute("ou", ou);
            items[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attrs[0]);

            try {
                ldapTemplate.modifyAttributes(new BuildDn().buildDn(formatter.formatCellValue(row.getCell(0))), items);

            } catch (Exception e) {
                if (e.getClass().toString().contains("NameNotFoundException"))
                    notExist.add(formatter.formatCellValue(row.getCell(0)));
            }

        }

        return notExist;
    }

    public List csvSheetAnalyze(BufferedReader sheet, String ou, boolean hasHeader) throws IOException {

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
                ldapTemplate.modifyAttributes(new BuildDn().buildDn(data[0]), items);

            } catch (Exception e) {
                if (e.getClass().toString().contains("NameNotFoundException"))
                    notExist.add(data[0]);
            }

            i++;

        }
        return notExist;
    }

    public List csvSheet(BufferedReader sheet, String ou, boolean hasHeader) throws IOException {

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
                ldapTemplate.modifyAttributes(new BuildDn().buildDn(data[0]), items);

            } catch (Exception e) {
                if (e.getClass().toString().contains("NameNotFoundException"))
                    notExist.add(data[0]);
            }

            i++;

        }
        return notExist;
    }
}
