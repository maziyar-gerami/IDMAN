package parsso.idman.Helpers;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Models.Log;
import parsso.idman.Repos.LogRepo;
import parsso.idman.Utils.Convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class LogsExcelView extends AbstractXlsView {

    public static String mainCollection = "IDMAN_Log";
    @Autowired
    LogRepo logRepo;
    ZoneId zoneId = ZoneId.of("UTC+03:30");


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get data model which is passed by the Spring container
        List<Log> logs = logRepo.analyze(mainCollection, 0, 0);

        // create a new Excel sheet
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("Logs");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create header row
        HSSFRow header = sheet.createRow(0);

        header.createCell(0).setCellValue("userId");
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
        DateConverter dateConverter = new DateConverter();

        for (Log log : logs) {
            dateConverter.gregorianToPersian(log.getDateTime().getYear(), log.getDateTime().getMonth(), log.getDateTime().getDay());
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(log.getLoggerName());
            aRow.createCell(1).setCellValue(log.getMessage());
            aRow.createCell(2).setCellValue(dateConverter.getYear() + "/" + dateConverter.getMonth() + "/" + dateConverter.getDay());
            aRow.createCell(3).setCellValue(log.getDateTime().getHours() + ":" + log.getDateTime().getMinutes() + ":" + log.getDateTime().getSeconds());
            aRow.createCell(4).setCellValue(log.getDetails());
        }

    }
}