package parsso.idman.helpers.excelView;


import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Audit;

import parsso.idman.repos.LogsRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        // create a new Excel sheet
        Sheet sheet = workbook.createSheet("Audits");
        sheet.setDefaultColumnWidth(30);

        long count = mongoTemplate.count(new Query(), Variables.col_audit);


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

        for (int page = 0; page <= Math.ceil( (float)count/Variables.PER_BATCH_COUNT); page++) {

            if (page==1000)
                return;

            int skip = (page == 0) ? 0 : ((page - 1) * Variables.PER_BATCH_COUNT);

            List<Audit> audits = Audit.analyze(mongoTemplate, skip, Variables.PER_BATCH_COUNT);

            for (Audit audit : audits) {

                Row aRow = sheet.createRow(rowCount++);
                aRow.createCell(0).setCellValue(audit.getPrincipal());
                aRow.createCell(1).setCellValue(audit.getResourceOperatedUpon());
                aRow.createCell(2).setCellValue(audit.getActionPerformed());
                aRow.createCell(3).setCellValue(audit.getApplicationCode());
                aRow.createCell(4).setCellValue(audit.getDateString());
                aRow.createCell(5).setCellValue(audit.getTimeString());
                aRow.createCell(6).setCellValue(audit.getClientIpAddress());
                aRow.createCell(7).setCellValue(audit.getClientIpAddress());

            }

        }
    }
}