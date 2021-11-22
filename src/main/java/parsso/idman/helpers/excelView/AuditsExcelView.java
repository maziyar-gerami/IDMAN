package parsso.idman.helpers.excelView;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import parsso.idman.helpers.TimeHelper;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.other.Time;
import parsso.idman.repos.LogsRepo;
import parsso.idman.utils.convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class AuditsExcelView extends AbstractXlsxView {
    @SuppressWarnings("unused")
    final LogsRepo.AuditRepo auditRepo;
    final MongoTemplate mongoTemplate;

    @Autowired
    public AuditsExcelView(MongoTemplate mongoTemplate, LogsRepo.AuditRepo auditRepo) {
        this.auditRepo = auditRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        // get data model which is passed by the Spring container
        List<Audit> audits = Audit.analyze(mongoTemplate, 0, 0);

        // create a new Excel sheet
        Sheet sheet = workbook.createSheet("Audits");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Principal");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Resource OperatedUpon");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Action Performed");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Application Code");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Date");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Time");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Client IP Address");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("Server IP Address");
        header.getCell(7).setCellStyle(style);

        // create data rows
        int rowCount = 1;

        for (Audit audit : audits) {

            //TODO: remove this limitation
            if(rowCount<Variables.LOGS_LIMIT)
                return;

            Row aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(audit.getPrincipal());
            aRow.createCell(1).setCellValue(audit.getResourceOperatedUpon());
            aRow.createCell(2).setCellValue(audit.getActionPerformed());
            aRow.createCell(3).setCellValue(audit.getApplicationCode());

            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTime(audit.getWhenActionWasPerformed());

            ZonedDateTime eventDate = OffsetDateTime.ofInstant(audit.getWhenActionWasPerformed().toInstant(), ZoneId.of(Variables.ZONE)).atZoneSameInstant(ZoneId.of(Variables.ZONE));
            DateConverter date = new DateConverter();
            date.gregorianToPersian(eventDate.getYear(),eventDate.getMonthValue(),eventDate.getDayOfMonth());
            Time time = new Time(date.getYear(), date.getMonth(), date.getDay(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());

            aRow.createCell(4).setCellValue(time.getYear() + "/" + time.getMonth() + "/" + time.getDay());
            aRow.createCell(5).setCellValue(time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
            aRow.createCell(6).setCellValue(audit.getClientIpAddress());
            aRow.createCell(7).setCellValue(audit.getClientIpAddress());

        }

    }
}