package parsso.idman.helpers.user;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
public class UsersExcelView extends AbstractXlsView {
    @Autowired
    UserRepo userRepo;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        // get data model which is passed by the Spring container
        List<User> users = userRepo.retrieveUsersFull();

        // create a new Excel sheet
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("users");
        sheet.setDefaultColumnWidth(20);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        // create header row
        HSSFRow header = sheet.createRow(0);

        header.createCell(0).setCellValue("userId");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("firstName");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("lastName");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("displayName");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("mobile");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("mail");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("memberOf");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("description");
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue("status");
        header.getCell(8).setCellStyle(style);

        header.createCell(9).setCellValue("employeeNumber");
        header.getCell(9).setCellStyle(style);

        // create data rows
        int rowCount = 1;

        for (User user : users) {

            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(user.get_id().toString());
            try {
                aRow.createCell(1).setCellValue(user.getFirstName());
            } catch (Exception e) {

                aRow.createCell(1).setCellValue("");
            }

            try {
                aRow.createCell(2).setCellValue(user.getLastName());
            } catch (Exception e) {
                aRow.createCell(2).setCellValue("");
            }

            try {
                aRow.createCell(3).setCellValue(user.getDisplayName());
            } catch (Exception e) {
                aRow.createCell(3).setCellValue("");
            }

            try {
                aRow.createCell(4).setCellValue(user.getMobile());
            } catch (Exception e) {
                aRow.createCell(4).setCellValue("");
            }

            try {
                aRow.createCell(5).setCellValue(user.getMail());
            } catch (Exception e) {
                aRow.createCell(5).setCellValue("");
            }

            try {
                aRow.createCell(6).setCellValue(user.getExportMemberOf());
            } catch (Exception e) {
                aRow.createCell(6).setCellValue("");
            }

            try {
                aRow.createCell(7).setCellValue(user.getDescription());
            } catch (Exception e) {
                aRow.createCell(7).setCellValue("");
            }

            try {
                aRow.createCell(8).setCellValue(user.getStatus());
            } catch (Exception e) {
                aRow.createCell(8).setCellValue("");
            }

            try {
                aRow.createCell(9).setCellValue(user.getEmployeeNumber());
            } catch (Exception e) {
                aRow.createCell(9).setCellValue("");
            }

        }
    }
}