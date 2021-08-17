package parsso.idman.Helpers.ExcelView;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import parsso.idman.Helpers.Variables;
import parsso.idman.Repos.TranscriptRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.Map;

public class UsersLicenseExcelView extends AbstractXlsView {
    @Autowired
    TranscriptRepo transcriptRepo;
    ZoneId zoneId = ZoneId.of(Variables.ZONE);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // create a new Excel sheet
        HSSFSheet sheetLicensed = (HSSFSheet) workbook.createSheet("Licensed");
        HSSFSheet sheetUnlicensed = (HSSFSheet) workbook.createSheet("UnLicensed");
        sheetLicensed.setDefaultColumnWidth(30);
        sheetUnlicensed.setDefaultColumnWidth(30);

        // create style for licensedHeader cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create licensedHeader row
        HSSFRow licensedHeader = sheetLicensed.createRow(0);

        licensedHeader.createCell(0).setCellValue("userId");
        licensedHeader.getCell(0).setCellStyle(style);

        licensedHeader.createCell(1).setCellValue("Display Name");
        licensedHeader.getCell(1).setCellStyle(style);

        build("licensed", sheetLicensed);

        HSSFRow header = sheetUnlicensed.createRow(0);

        header.createCell(0).setCellValue("userId");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Display Name");
        header.getCell(1).setCellStyle(style);

        build("unLicensed", sheetUnlicensed);

    }

    private void build(String licenseStatus, HSSFSheet sheet) {

            /*if(licenseStatus.equalsIgnoreCase("licensed"))
            List<License> licenses = transcriptRepo.servicesOfUser();

            // create data rows
            int rowCount = 1;

            for (User user : Users) {
                HSSFRow aRow = sheet.createRow(rowCount++);
                aRow.createCell(0).setCellValue(user.getUserId());
                aRow.createCell(1).setCellValue(user.getDisplayName());

                if (rowCount == 65536)
                    break;
            }*/
    }

}
