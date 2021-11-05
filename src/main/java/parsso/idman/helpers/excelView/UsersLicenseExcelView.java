package parsso.idman.helpers.excelView;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsersLicenseExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

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


        HSSFRow header = sheetUnlicensed.createRow(0);

        header.createCell(0).setCellValue("userId");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Display Name");
        header.getCell(1).setCellStyle(style);


    }
}