package parsso.idman.Helpers.Events;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Models.Event;
import parsso.idman.Models.Time;
import parsso.idman.Models.User;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
public class EventsExcelView extends AbstractXlsView {

    @Autowired
    EventRepo eventRepo;



    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get data model which is passed by the Spring container
        List<Event> events = eventRepo.getMainListEvents();

        // create a new Excel sheet
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("Events");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create header row
        HSSFRow header = sheet.createRow(0);

        header.createCell(0).setCellValue("type");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("application");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Client IP");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("date");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Time");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Operation system");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Browser");
        header.getCell(6).setCellStyle(style);

        // create data rows
        int rowCount = 1;

        for (Event event : events) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(event.getType());
            aRow.createCell(1).setCellValue(event.getApplication());
            aRow.createCell(2).setCellValue(event.getClientip());
            Time time = new Time(event.getTime().getYear(),event.getTime().getMonth(),event.getTime().getDay(),
                    event.getTime().getHours(),event.getTime().getMinutes(),event.getTime().getSeconds());
            String jalali = time.convertDateGeorgianToJalali(event.getTime().getYear(),event.getTime().getMonth(),event.getTime().getDay());
            aRow.createCell(3).setCellValue(jalali.substring(0,4)+"/"+jalali.substring(4,6)+"/"+jalali.substring(6));
            aRow.createCell(4).setCellValue(event.getTime().getHours()+":"+event.getTime().getMinutes()+":"+event.getTime().getSeconds());
            aRow.createCell(5).setCellValue(event.getAgentInfo().getOs());
            aRow.createCell(6).setCellValue(event.getAgentInfo().getBrowser());

        }

    }
}