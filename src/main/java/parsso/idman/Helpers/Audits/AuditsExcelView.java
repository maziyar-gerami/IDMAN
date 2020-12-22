package parsso.idman.Helpers.Audits;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Models.Audit;
import parsso.idman.Models.Time;
import parsso.idman.Repos.AuditRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.*;
import java.util.*;

@Service
public class AuditsExcelView extends AbstractXlsView {

    @Autowired
    AuditRepo auditRepo;



    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get data model which is passed by the Spring container
        List<Audit> audits = auditRepo.getMainListAudits();

        // create a new Excel sheet
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("Audits");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create header row
        HSSFRow header = sheet.createRow(0);

        header.createCell(0).setCellValue("Principal");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Resource OperatedUpon");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Action Performed");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Application Code");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("When Action Was Performed");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Client IP Address");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Server IP Address");
        header.getCell(6).setCellStyle(style);

        // create data rows
        int rowCount = 1;

        for (Audit audit : audits) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(audit.getPrincipal());
            aRow.createCell(1).setCellValue(audit.getResourceOperatedUpon());
            aRow.createCell(2).setCellValue(audit.getActionPerformed());
            aRow.createCell(3).setCellValue(audit.getApplicationCode());

            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTime(audit.getWhenActionWasPerformed());

            Time time = new Time(cal);

            String jalali = time.convertDateGeorgianToJalali(time.getYear(),time.getMonth(),time.getDay());
            aRow.createCell(4).setCellValue(jalali.substring(0,4)+"/"+jalali.substring(4,6)+"/"+jalali.substring(6)+" "+time.getHours()+":"+time.getMinutes()+":"+time.getSeconds());
            aRow.createCell(5).setCellValue(audit.getClientIpAddress());
            aRow.createCell(6).setCellValue(audit.getClientIpAddress());

        }

    }
}