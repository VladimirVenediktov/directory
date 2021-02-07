package com.moshna.directory.service;

import com.moshna.directory.model.Department;
import com.moshna.directory.model.Employee;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.repo.EmployeeRepo;
import com.moshna.directory.repo.EmployeeRepoCustomImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.moshna.directory.repo.EmployeeRepo.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeRepoCustomImpl employeeRepoCustom;
    private final DepartmentRepo departmentRepo;
    private final MainService mainService;

    public static final String HOME_PAGE = "home";
    public static final String EMPLOYEE_ADDING = "employee-adding";
    public static final String EMPLOYEE_DETAILS = "employee-details";
    public static final String ERROR_PAGE = "error";

    @Value("${upload.path}")
    private String uploadPath;

    public EmployeeService(EmployeeRepo employeeRepo,
                           DepartmentRepo departmentRepo,
                           MainService mainService,
                           EmployeeRepoCustomImpl employeeRepoCustom) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.mainService = mainService;
        this.employeeRepoCustom = employeeRepoCustom;
    }

    public String goHome(Model model) {
        List<Employee> employeeList = mainService.getEmployeeList();
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    public String employeeAdding(Model model) {
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
        return EMPLOYEE_ADDING;
    }

    public String employeePostAdd(Employee employee,
                                  MultipartFile file,
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
            for (Department department:departmentList) {
                if(employee.getDepartmentID() == department.getId()){
                    employee.setDepartmentName(department.getTitle());
                    break;
                }
            }
            employee.setFullName(employee.getFirstName() + " " +
                    employee.getSecondName() + " " +
                    employee.getThirdName());
            employeeRepo.save(employee);
            model.addAttribute("employees", employeeList);
            return "redirect:/home";
        } catch (Exception e) {
            message = "validation error " + e.getMessage();
            model.addAttribute("validationMessage", message);
            return EMPLOYEE_ADDING;
        }
    }

    public String employeeDetails(long id, Model model) throws Exception {

        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new Exception("Employee not found - " + id));
        Department departmentSelected = departmentRepo.findById(employee.getDepartmentID()).orElseThrow(() ->
                new Exception("Department not found - " + employee.getDepartmentID()));

        List<Department> departmentList = mainService.getDepartmentList();

        model.addAttribute("employee", employee);
        model.addAttribute("departmentSelected", departmentSelected);
        model.addAttribute("departmentList", departmentList);
        return EMPLOYEE_DETAILS;
    }

    public String employeeUpdate(long id, String firstName, String secondName,
                                 String thirdName, String position, String dateOfBirth,
                                 String mobilePhone, String email, Long departmentID,
                                 Model model) throws Exception {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new Exception("Employee not found - " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            employee.setFirstName(firstName);
            employee.setSecondName(secondName);
            employee.setThirdName(thirdName);
            employee.setPosition(position);
            employee.setDateOfBirth(dateOfBirth);
            employee.setMobilePhone(mobilePhone);
            employee.setEmail(email);
            employee.setDepartmentID(departmentID);
            employee.setFullName(firstName + " " + secondName);
            employeeRepo.save(employee);

            List<Employee> employeeList = mainService.getEmployeeList();
            model.addAttribute("employees", employeeList);
            return "redirect:/home";
        }
        else {
            return ERROR_PAGE;
        }


    }

    public String employeeDelete(long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            employeeRepo.deleteById(id);

            List<Employee> employeeList = mainService.getEmployeeList();
            model.addAttribute("employees", employeeList);
            return "redirect:/home";
        }
        else {
            return ERROR_PAGE;
        }
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

    public String filterByParams(String name,
                               String position,
                               Long departmentID,
                               Model model) {
        List<Employee> employeeFilteredList = employeeRepo.findAll(where(hasFirstName(name)
                .and(hasPosition(position))
                .or(hasDepartment(departmentID))));
        List<Employee> employeeList = mainService.getEmployeeList();
        List<Department> departmentList = mainService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("employees", employeeList);
        model.addAttribute("employees", employeeFilteredList);

        return HOME_PAGE;
    }
}
