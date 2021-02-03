package com.moshna.directory.controller;

import com.moshna.directory.model.Department;
import com.moshna.directory.model.EmployeeExcelExporter;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.repo.EmployeeRepo;

import com.moshna.directory.model.Employee;
import com.moshna.directory.service.MainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Controller
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final MainService mainService;

    public static final String HOME_PAGE = "home";
    public static final String EMPLOYEE_ADDING = "employee-adding";
    public static final String EMPLOYEE_DETAILS = "employee-details";

    @Value("${upload.path}")
    private String uploadPath;

    public EmployeeController(EmployeeRepo employeeRepo,
                              MainService mainService,
                              DepartmentRepo departmentRepo) {
        this.employeeRepo = employeeRepo;
        this.mainService = mainService;
        this.departmentRepo = departmentRepo;
    }

    @GetMapping("/home")
    public String goHome(Model model)
    {

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    @GetMapping("/employee")
    public String employeeAdding(Model model) {
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
        return EMPLOYEE_ADDING;
    }

    @PostMapping("/employee")
    public String employeePostAdd(@Valid Employee employee,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) throws Exception {

        List<Employee> employeeList = mainService.getEmployeeList();

        if(file != null) {
            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            employee.setFileName(resultFileName);
        }



        String message = "";

        try {
            employeeRepo.save(employee);
            model.addAttribute("employees", employeeList);
            return "redirect:/home";
        } catch (Exception e) {
            message = "validation error " + e.getMessage();
            model.addAttribute("validationMessage", message); //TODO:сделать нормальный вывод ошибок
            return EMPLOYEE_ADDING;
        }
    }

    @PostMapping("filterByName")
    public String filterByName(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();

        employeeList.sort(Comparator.comparing(Employee::getFirstName));
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }
    @PostMapping("filterByPosition")
    public String filterByPosition(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();

        employeeList.sort(Comparator.comparing(Employee::getPosition));
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    @GetMapping("{id}")
    public String employeeDetails(@PathVariable(value = "id") long id, Model model) throws Exception {

        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new Exception("Employee not found - " + id));
        Department departmentSelected = departmentRepo.findById(employee.getDepartmentID()).orElseThrow(() ->
                new Exception("Department not found - " + employee.getDepartmentID()));

        List<Department> departmentList = mainService.getDepartmentList();

        model.addAttribute("employee", employee);
        model.addAttribute("departmentSelected", departmentSelected);
        model.addAttribute("departmentList", departmentList);
        return EMPLOYEE_DETAILS;
    }

    @PostMapping("{id}/update")
    public String employeeUpdate(@PathVariable(value = "id") long id,
                                 @RequestParam String firstName,
                                 @RequestParam String secondName,
                                 @RequestParam String thirdName,
                                 @RequestParam String position,
                                 @RequestParam String dateOfBirth,
                                 @RequestParam String mobilePhone,
                                 @RequestParam String email,
                                 @RequestParam Long departmentID,
                                 Model model) throws Exception {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new Exception("Employee not found - " + id));

        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setThirdName(thirdName);
        employee.setPosition(position);
        employee.setDateOfBirth(dateOfBirth);
        employee.setMobilePhone(mobilePhone);
        employee.setEmail(email);
        employee.setDepartmentID(departmentID);
        employeeRepo.save(employee);

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return "redirect:/home";
    }

    @PostMapping("{id}/remove")
    public String employeeDelete(@PathVariable(value = "id") long id, Model model) {
        employeeRepo.deleteById(id);

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return "redirect:/home";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees.xlsx";

        response.setHeader(headerKey, headerValue);

        List<Employee> employeeList = mainService.getEmployeeList();
        List<Department> departmentList = mainService.getDepartmentList();

        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(employeeList, departmentList);
        excelExporter.export(response);

    }

}
