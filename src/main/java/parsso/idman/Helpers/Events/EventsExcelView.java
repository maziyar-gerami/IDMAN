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
import parsso.idman.utils.Convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

        header.createCell(1).setCellValue("userId");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("application");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Client IP");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Server IP");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Date");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Time");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("Operation system");
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue("Browser");
        header.getCell(8).setCellStyle(style);

        // create data rows
        int rowCount = 1;

        for (Event event : events) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(event.getType());
            aRow.createCell(1).setCellValue(event.getPrincipalId());
            aRow.createCell(2).setCellValue(event.getApplication());
            aRow.createCell(3).setCellValue(event.getClientip());
            aRow.createCell(4).setCellValue(event.getServerip());


            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = parserSDF.parse(event.getCreationTime().substring(0,19));
            Calendar myCal = new GregorianCalendar();
            myCal.setTimeZone(TimeZone.getDefault());
            myCal.setTime(date);

            DateConverter dateConverter = new DateConverter();
            dateConverter.gregorianToPersian(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));

            Time time = new Time(dateConverter, myCal);

            aRow.createCell(5).setCellValue(time.getYear()+"/"+time.getMonth()+"/"+time.getDay());
            aRow.createCell(6).setCellValue(event.getTime().getHours()+":"+event.getTime().getMinutes()+":"+event.getTime().getSeconds());
            aRow.createCell(7).setCellValue(event.getAgentInfo().getOs());
            aRow.createCell(8).setCellValue(event.getAgentInfo().getBrowser());

        }

    }
}