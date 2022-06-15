package parsso.idman.helpers.excelView;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Event;
import parsso.idman.repos.LogsRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class EventsExcelView {
  final LogsRepo.EventRepo eventRepo;
  final MongoTemplate mongoTemplate;

  public EventsExcelView(LogsRepo.EventRepo eventRepo, MongoTemplate mongoTemplate) {
    this.eventRepo = eventRepo;
    this.mongoTemplate = mongoTemplate;

  }

  public Workbook buildExcelDocument() {

    Workbook workbook = new XSSFWorkbook();

    long count = mongoTemplate.count(new Query(), Variables.col_casEvent);
    // get data model which is passed by the Spring container

    // create a new Excel sheet
    Sheet sheet = workbook.createSheet("events");
    sheet.setDefaultColumnWidth(30);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    font.setBold(true);
    style.setFont(font);
    style.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());

    // create header row
    Row header = sheet.createRow(0);

    header.createCell(0).setCellValue("type");
    header.getCell(0).setCellStyle(style);

    header.createCell(1).setCellValue("userId");
    header.getCell(1).setCellStyle(style);

    header.createCell(2).setCellValue("application");
    header.getCell(2).setCellStyle(style);

    header.createCell(3).setCellValue("Client IP");
    header.getCell(3).setCellStyle(style);

    header.createCell(4).setCellValue("Date");
    header.getCell(4).setCellStyle(style);

    header.createCell(5).setCellValue("Time");
    header.getCell(5).setCellStyle(style);

    header.createCell(6).setCellValue("Operation system");
    header.getCell(6).setCellStyle(style);

    header.createCell(7).setCellValue("Browser");
    header.getCell(7).setCellStyle(style);

    // create data rows
    int rowCount = 1;

    for (int page = 0; page <= Math.ceil(Variables.PER_BATCH_COUNT / (float) count); page++) {

      int skip = (page == 1) ? 0 : ((page - 1) * Variables.PER_BATCH_COUNT);

      List<Event> events = eventRepo.analyze(skip, Variables.PER_BATCH_COUNT, "");

      try {
        for (Event event : events) {
          Row aRow = sheet.createRow(rowCount++);
          aRow.createCell(0).setCellValue(event.getType());
          aRow.createCell(1).setCellValue(event.getPrincipalId());
          aRow.createCell(2).setCellValue(event.getApplication());
          aRow.createCell(3).setCellValue(event.getClientIP());
          aRow.createCell(4).setCellValue(event.getDateString());
          aRow.createCell(5).setCellValue(event.getTimeString());
          aRow.createCell(6).setCellValue(event.getAgentInfo().getOs());
          aRow.createCell(7).setCellValue(event.getAgentInfo().getBrowser());

        }

      } catch (Exception e) {

      }
    }

    return workbook;
  }
}