package parsso.idman.helpers.excelView;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONArray;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import parsso.idman.impls.groups.RetrieveGroup;
import parsso.idman.models.groups.Group;
import parsso.idman.models.license.License;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.ServiceRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GroupsExcelView extends AbstractXlsView {
  final GroupRepo.Retrieve retrieveGroup;
  final MongoTemplate mongoTemplate;
  final ServiceRepo serviceRepo;

  public GroupsExcelView(RetrieveGroup retrieveGroup, MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.retrieveGroup = retrieveGroup;
    this.mongoTemplate = mongoTemplate;
    this.serviceRepo = serviceRepo;
  }

  public License servicesOfGroup(String ouID) {
    List<MicroService> licensed = new LinkedList<>();

    List<Service> allServices = serviceRepo.listServicesFull();

    for (Service service : allServices)
      if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
        for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes()
            .get("ou"))).get(1))
          if (ouID.equalsIgnoreCase(name.toString()))
            licensed.add(new MicroService(service));

    return new License(licensed, null);

  }


  @Override
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
      HttpServletResponse response) {

    // get data model which is passed by the Spring container
    List<Group> groups = retrieveGroup.retrieve();

    for (Group group : groups)
      group.setService(servicesOfGroup(group.getId()));

    // create a new Excel sheet
    HSSFSheet sheet = (HSSFSheet) workbook.createSheet("grpups");
    sheet.setDefaultColumnWidth(20);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    font.setBold(true);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

    // create header row
    HSSFRow header = sheet.createRow(0);

    header.createCell(0).setCellValue("groupID");
    header.getCell(0).setCellStyle(style);

    header.createCell(1).setCellValue("name");
    header.getCell(1).setCellStyle(style);

    header.createCell(2).setCellValue("description");
    header.getCell(2).setCellStyle(style);

    header.createCell(3).setCellValue("licensed services");
    header.getCell(3).setCellStyle(style);

    header.createCell(4).setCellValue("unLicensed services");
    header.getCell(4).setCellStyle(style);

    // create data rows
    int rowCount = 1;

    for (Group group : groups) {

      HSSFRow aRow = sheet.createRow(rowCount++);
      aRow.createCell(0).setCellValue(group.getId().toString());
      try {
        aRow.createCell(1).setCellValue(group.getName());
      } catch (Exception e) {

        aRow.createCell(1).setCellValue("");
      }

      try {
        aRow.createCell(2).setCellValue(group.getDescription());
      } catch (Exception e) {
        aRow.createCell(2).setCellValue("");
      }

      LinkedList<MicroService> licensedServices = (LinkedList<MicroService>) group.getService().getLicensed();
      LinkedList<MicroService> unlicensedServices = (LinkedList<MicroService>) group.getService().getUnLicensed();
      String licensed = "";
      String unlicenced = "";

      if (licensedServices != null) {
        for (MicroService l : licensedServices)
          licensed += licensed.equals("") ? l.getDescription() : ", " + l.getDescription();
      }

      try {
        aRow.createCell(3).setCellValue(licensed);
      } catch (Exception e) {
        aRow.createCell(3).setCellValue("");
      }

      if (unlicensedServices != null) {
        for (MicroService u : unlicensedServices)
          licensed += unlicenced.equals("") ? u.getDescription() : ", " + u.getDescription();
      }

      try {
        aRow.createCell(4).setCellValue(unlicenced);
      } catch (Exception e) {
        aRow.createCell(4).setCellValue("");
      }
    }
  }
}