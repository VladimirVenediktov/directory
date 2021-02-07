package com.moshna.directory.service;

import com.moshna.directory.model.Department;
import com.moshna.directory.model.Employee;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final MainService mainService;

    public static final String HOME_PAGE = "home";
    public static final String EMPLOYEE_ADDING = "employee-adding";
    public static final String EMPLOYEE_DETAILS = "employee-details";

    @Value("${upload.path}")
    private String uploadPath;

    public EmployeeService(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, MainService mainService) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.mainService = mainService;
    }

    public String goHome(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    public String employeeAdding(Model model) {
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
        return EMPLOYEE_ADDING;
    }

    public String employeePostAdd(@Valid Employee employee,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) throws Exception {

        List<Employee> employeeList = mainService.getEmployeeList();
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
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

    public String employeeDelete(@PathVariable(value = "id") long id, Model model) {
        employeeRepo.deleteById(id);

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return "redirect:/home";
    }

    public String sortingByName(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();

        employeeList.sort(Comparator.comparing(Employee::getFirstName));
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    public String sortingByPosition(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();

        employeeList.sort(Comparator.comparing(Employee::getPosition));
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    public String filterByName(@RequestParam String name,
                               @RequestParam String position,
                               Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();
        //List<Employee> e = mainService.getFilteredEmployeeListByFullName(name);

        //TODO: сделать поиск

        return HOME_PAGE;
    }
}
