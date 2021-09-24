package parsso.idman.Helpers.ExcelView;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Time;
import parsso.idman.RepoImpls.audits.Retrieves;
import parsso.idman.Repos.audits.AuditRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class AuditsExcelView extends AbstractXlsView {
	public static String mainCollection = "MongoDbCasAuditRepository";
	AuditRepo auditRepo;
	ZoneId zoneId = ZoneId.of(Variables.ZONE);
	MongoTemplate mongoTemplate;

	@Autowired
	public AuditsExcelView(MongoTemplate mongoTemplate, AuditRepo auditRepo){
		this.auditRepo = auditRepo;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		// get data model which is passed by the Spring container
		List<Audit> audits = new Retrieves(mongoTemplate).analyze(mainCollection, 0, 0);

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
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(audit.getPrincipal());
			aRow.createCell(1).setCellValue(audit.getResourceOperatedUpon());
			aRow.createCell(2).setCellValue(audit.getActionPerformed());
			aRow.createCell(3).setCellValue(audit.getApplicationCode());

			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			cal.setTime(audit.getWhenActionWasPerformed());

			ZonedDateTime eventDate = OffsetDateTime.ofInstant(audit.getWhenActionWasPerformed().toInstant(), zoneId).atZoneSameInstant(zoneId);
			Time time = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
					eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());

			aRow.createCell(4).setCellValue(time.getYear() + "/" + time.getMonth() + "/" + time.getDay());
			aRow.createCell(5).setCellValue(time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
			aRow.createCell(6).setCellValue(audit.getClientIpAddress());
			aRow.createCell(7).setCellValue(audit.getClientIpAddress());

			if (rowCount == 65536)
				break;
		}

	}
}