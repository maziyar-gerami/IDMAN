package parsso.idman.helpers.excelView;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Event;
import parsso.idman.models.other.Time;
import parsso.idman.repos.LogsRepo;
import parsso.idman.utils.convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EventsExcelView extends AbstractXlsxView {
    final ZoneId zoneId = ZoneId.of(Variables.ZONE);
    @Autowired
    LogsRepo.EventRepo eventRepo;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        // get data model which is passed by the Spring container
        List<Event> events = eventRepo.analyze(0, 0);

        // create a new Excel sheet
        Sheet sheet = workbook.createSheet("events");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

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

        for (Event event : events) {
            //TODO: remove this limitation
            if(rowCount<Variables.LOGS_LIMIT)
                return;
            Row aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(event.getType());
            aRow.createCell(1).setCellValue(event.getPrincipalId());
            aRow.createCell(2).setCellValue(event.getApplication());
            aRow.createCell(3).setCellValue(event.getClientIP());

            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time);

            DateConverter dateConverter = new DateConverter();
            dateConverter.gregorianToPersian(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth());
            aRow.createCell(4).setCellValue(dateConverter.getYear() + "/" + dateConverter.getMonth() + "/" + dateConverter.getDay());
            aRow.createCell(5).setCellValue(event.getTime().getHours() + ":" + event.getTime().getMinutes() + ":" + event.getTime().getSeconds());
            aRow.createCell(6).setCellValue(event.getAgentInfo().getOs());
            aRow.createCell(7).setCellValue(event.getAgentInfo().getBrowser());

        }

    }
}