package parsso.idman.helpers.excelView;


import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@SuppressWarnings("ALL")
public class UsersLicenseExcelView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        // create a new Excel sheet
        Sheet sheetLicensed = (Sheet) workbook.createSheet("Licensed");
        Sheet sheetUnlicensed = (Sheet) workbook.createSheet("UnLicensed");
        sheetLicensed.setDefaultColumnWidth(30);
        sheetUnlicensed.setDefaultColumnWidth(30);

        // create style for licensedHeader cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        // create licensedHeader row
        Row licensedHeader = sheetLicensed.createRow(0);

        licensedHeader.createCell(0).setCellValue("userId");
        licensedHeader.getCell(0).setCellStyle(style);

        licensedHeader.createCell(1).setCellValue("Display Name");
        licensedHeader.getCell(1).setCellStyle(style);


        Row header = sheetUnlicensed.createRow(0);

        header.createCell(0).setCellValue("userId");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Display Name");
        header.getCell(1).setCellStyle(style);

    }
}
