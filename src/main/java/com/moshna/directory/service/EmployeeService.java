package com.moshna.directory.service;

import com.moshna.directory.dto.DepartmentDto;
import com.moshna.directory.dto.EmployeeDto;
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
import java.util.ArrayList;
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

        List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
        List<DepartmentDto> departmentDtoList = getDepartmentDtoList(mainService.getDepartmentList());
        model.addAttribute("departmentList", departmentDtoList);
        model.addAttribute("employees", employeeDtoList);
        return HOME_PAGE;
    }

    public String employeeAdding(Model model) {
        List<DepartmentDto> departmentDtoList = getDepartmentDtoList(mainService.getDepartmentList());
        model.addAttribute("departmentList", departmentDtoList);
        return EMPLOYEE_ADDING;
    }

    public String employeePostAdd(Employee employee,
                                  MultipartFile file,
                                  Model model) throws Exception {
        List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
        List<DepartmentDto> departmentDtoList = getDepartmentDtoList(mainService.getDepartmentList());

        model.addAttribute("departmentList", departmentDtoList);
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
            for (DepartmentDto department:departmentDtoList) {
                if(employee.getDepartmentID() == department.getId()){
                    employee.setDepartmentName(department.getTitle());
                    break;
                }
            }
            employee.setFullName(employee.getFirstName() + " " +
                    employee.getSecondName() + " " +
                    employee.getThirdName());
            employeeRepo.save(employee);
            model.addAttribute("employees", employeeDtoList);
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

        EmployeeDto employeeDto = getEmployeeDto(employee);
        DepartmentDto departmentDtoSelected = getDepartmentDto(departmentSelected);
        List<DepartmentDto> departmentDtoList = getDepartmentDtoList(departmentList);


        model.addAttribute("employee", employeeDto);
        model.addAttribute("departmentSelected", departmentDtoSelected);
        model.addAttribute("departmentList", departmentDtoList);
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

            List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
            model.addAttribute("employees", employeeDtoList);
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

            List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
            model.addAttribute("employees", employeeDtoList);
            return "redirect:/home";
        }
        else {
            return ERROR_PAGE;
        }
    }

    public String sortingByName(Model model) {
        List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
        employeeDtoList.sort(Comparator.comparing(EmployeeDto::getFirstName));
        model.addAttribute("employees", employeeDtoList);
        return HOME_PAGE;
    }

    public String sortingByPosition(Model model) {
        List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
        employeeDtoList.sort(Comparator.comparing(EmployeeDto::getPosition));
        model.addAttribute("employees", employeeDtoList);
        return HOME_PAGE;
    }

    public String filterByParams(String name,
                               String position,
                               Long departmentID,
                               Model model) {
        List<Employee> employeeFilteredList = employeeRepo.findAll(where(hasFirstName(name)
                .and(hasPosition(position))
                .or(hasDepartment(departmentID))));
        List<EmployeeDto> employeeDtoList = getEmployeeDtoList(mainService.getEmployeeList());
        List<DepartmentDto> departmentDtoList = getDepartmentDtoList(mainService.getDepartmentList());

        List<EmployeeDto> employeeDtoFilteredList = getEmployeeDtoList(employeeFilteredList);
        model.addAttribute("departmentList", departmentDtoList);
        model.addAttribute("employees", employeeDtoList);
        model.addAttribute("employees", employeeDtoFilteredList);

        return HOME_PAGE;
    }

    public List<DepartmentDto> getDepartmentDtoList(List<Department> departmentList) {
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        for (Department department: departmentList) {
            departmentDtoList.add(new DepartmentDto(department.getId(),
                    department.getTitle()));
        }
        return departmentDtoList;
    }

    public List<EmployeeDto> getEmployeeDtoList(List<Employee> employeeList) {
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        for (Employee e: employeeList) {
            employeeDtoList.add(new EmployeeDto(e.getId(), e.getFirstName(), e.getSecondName(), e.getThirdName(),
                    e.getPosition(), e.getDateOfBirth(), e.getMobilePhone(),
                    e.getEmail(), e.getDepartmentID(), e.getFileName(),
                    e.getDepartmentName(), e.getFullName()));
        }
        return employeeDtoList;
    }

    public DepartmentDto getDepartmentDto(Department department) {
        return new DepartmentDto(department.getId(), department.getTitle());
    }

    public EmployeeDto getEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.getId(), employee.getFirstName(), employee.getSecondName(), employee.getThirdName(),
                employee.getPosition(), employee.getDateOfBirth(), employee.getMobilePhone(),
                employee.getEmail(), employee.getDepartmentID(), employee.getFileName(),
                employee.getDepartmentName(), employee.getFullName());
    }
}
