package parsso.idman.Helpers.Events;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.logs.events.EventRepo;
import parsso.idman.Utils.Convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventsExcelView extends AbstractXlsView {
	public static String mainCollection = "MongoDbCasEventRepository";
	@Autowired
	EventRepo eventRepo;
	ZoneId zoneId = ZoneId.of(Variables.ZONE);

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		// get data model which is passed by the Spring container
		List<Event> events = eventRepo.analyze(0, 0);

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
			HSSFRow aRow = sheet.createRow(rowCount++);
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