package com.moshna.directory.service;

import com.moshna.directory.model.Department;
import com.moshna.directory.model.EmployeeExcelExporter;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.repo.EmployeeRepo;
import com.moshna.directory.model.Employee;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MainService {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public MainService(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {

        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    public List<Employee> getEmployeeList() {
        try {
            Iterable<Employee> employees = employeeRepo.findAll();
            List<Employee> employeeList = new ArrayList<>();
            employees.forEach(employee -> employeeList.add(employee));
            return employeeList;
        }
        catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Department> getDepartmentList() {
        try {
            Iterable<Department> departments = departmentRepo.findAll();
            List<Department> departmentList = new ArrayList<>();
            departments.forEach(employee -> departmentList.add(employee));
            return departmentList;
        }
        catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees.xlsx";

        response.setHeader(headerKey, headerValue);

        List<Employee> employeeList = getEmployeeList();
        List<Department> departmentList = getDepartmentList();

        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(employeeList, departmentList);
        excelExporter.export(response);

    }
    /*public List<Employee> getFilteredEmployeeListByFullName(String name) {
        return employeeRepo.findAllByFullName(name);
    }*/
}
