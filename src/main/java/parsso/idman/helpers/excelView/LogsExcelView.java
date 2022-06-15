package parsso.idman.helpers.excelView;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.models.logs.Report;
import parsso.idman.models.other.Time;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class LogsExcelView {
  final MongoTemplate mongoTemplate;

  public LogsExcelView(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Workbook buildExcelDocument() {
    Workbook workbook = new XSSFWorkbook();

    // get data model which is passed by the Spring container
    List<Report> reports = Report.analyze(mongoTemplate, 0, 0);

    // create a new Excel sheet
    Sheet sheet = workbook.createSheet("logs");
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

    header.createCell(0).setCellValue("Functor userID");
    header.getCell(0).setCellStyle(style);

    header.createCell(1).setCellValue("Message");
    header.getCell(1).setCellStyle(style);

    header.createCell(2).setCellValue("Date");
    header.getCell(2).setCellStyle(style);

    header.createCell(3).setCellValue("Time");
    header.getCell(3).setCellStyle(style);

    header.createCell(4).setCellValue("Details");
    header.getCell(4).setCellStyle(style);

    // create data rows
    int rowCount = 1;

    try {

      for (Report report : reports) {

        Row aRow = sheet.createRow(rowCount++);
        Time time = Time.longToPersianTime(report.getMillis());
        aRow.createCell(0).setCellValue(report.getLoggerName());
        aRow.createCell(1).setCellValue(report.getMessage());
        aRow.createCell(2).setCellValue(time.getYear() + "/" + time.getMonth() + "/" + time.getDay());
        aRow.createCell(3).setCellValue(time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
        aRow.createCell(4).setCellValue(report.getDetails());
      }

    } catch (Exception e) {

    }

    return workbook;

  }
}