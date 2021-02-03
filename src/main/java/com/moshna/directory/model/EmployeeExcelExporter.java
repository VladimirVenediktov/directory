package com.moshna.directory.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployeeExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Employee> employeeList;
    private List<Department> departmentList;

    public EmployeeExcelExporter(List<Employee> employeeList, List<Department> departmentList) {
        this.employeeList = employeeList;
        this.departmentList = departmentList;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Employees");
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue("Employee ID");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Full name");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Date of birth");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Email");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Mobile phone");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Position");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("Department");
        cell.setCellStyle(style);

    }

    private void writeDataRows() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setFontHeight(14);
        style.setFont(font);

        for (Employee employee: employeeList) {
            Row row =  sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(employee.getId());
            sheet.autoSizeColumn(0);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(employee.getFirstName() + " "
                            + employee.getSecondName() + " "
                            + employee.getThirdName());
            sheet.autoSizeColumn(1);
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(employee.getDateOfBirth());
            sheet.autoSizeColumn(2);
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(employee.getEmail());
            sheet.autoSizeColumn(3);
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(employee.getMobilePhone());
            sheet.autoSizeColumn(4);
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(employee.getPosition());
            sheet.autoSizeColumn(5);
            cell.setCellStyle(style);

            String departmentTitle="";
            for (Department department: departmentList) {
                if(employee.getDepartmentID() == department.getId()) {
                    departmentTitle = department.getTitle();
                }
            }
            cell = row.createCell(6);
            cell.setCellValue(departmentTitle);
            sheet.autoSizeColumn(6);
            cell.setCellStyle(style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
