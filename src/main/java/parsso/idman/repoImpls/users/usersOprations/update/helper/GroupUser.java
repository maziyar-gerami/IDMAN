package parsso.idman.repoImpls.users.usersOprations.update.helper;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.user.ExcelAnalyzer;
import parsso.idman.repos.UserRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class GroupUser {

    ExcelAnalyzer excelAnalyzer;
    UserRepo.UsersOp.Retrieve retrieveOp;

    public GroupUser(ExcelAnalyzer excelAnalyzer, String bASE_DN) {
        this.excelAnalyzer = excelAnalyzer;
    }

    public GroupUser(UserRepo.UsersOp.Retrieve retrieveOp) {
        this.retrieveOp = retrieveOp;
    }

    public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
        List<String> result = null;
        InputStream insfile = file.getInputStream();

        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbookXLSX;
            workbookXLSX = new XSSFWorkbook(insfile);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbookXLSX.getSheetAt(0);
            result = excelAnalyzer.excelSheetAnalyze(doer, sheet, ou, true);
            workbookXLSX.close();

        } else if (file.getOriginalFilename().endsWith(".xls")) {
            HSSFWorkbook workbookXLS;

            workbookXLS = new HSSFWorkbook(insfile);

            HSSFSheet xlssheet = workbookXLS.getSheetAt(0);

            result = excelAnalyzer.excelSheetAnalyze(doer, xlssheet, ou, true);
            workbookXLS.close();

        } else if (file.getOriginalFilename().endsWith(".csv")) {

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(insfile));

            result = excelAnalyzer.csvSheetAnalyzer(doer, csvReader, ou, true);

        }
        return result;
    }

}


