package parsso.idman.Helpers;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.other.Time;
import parsso.idman.Utils.Convertor.DateConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
public class LogsExcelView extends AbstractXlsView {
	MongoTemplate mongoTemplate;

	@Autowired
			public LogsExcelView(MongoTemplate mongoTemplate){
		this.mongoTemplate = mongoTemplate;
	}



	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		// get data model which is passed by the Spring container
		List<Report> reports = Report.analyze(mongoTemplate, 0, 0);

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
		DateConverter dateConverter = new DateConverter();

		for (Report report : reports) {
			dateConverter.gregorianToPersian(report.getDateTime().getYear(), report.getDateTime().getMonth(), report.getDateTime().getDay());
			HSSFRow aRow = sheet.createRow(rowCount++);
			Time time = TimeHelper.longToPersianTime(report.getMillis());
			aRow.createCell(0).setCellValue(report.getLoggerName());
			aRow.createCell(1).setCellValue(report.getMessage());
			aRow.createCell(2).setCellValue(time.getYear() + "/" + time.getMonth() + "/" + time.getDay());
			aRow.createCell(3).setCellValue(time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
			aRow.createCell(4).setCellValue(report.getDetails());
		}

	}
}