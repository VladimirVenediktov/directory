package com.moshna.directory.controller;

import com.moshna.directory.repo.EmployeeRepo;

import com.moshna.directory.model.Employee;
import com.moshna.directory.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;


@Controller
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final MainService mainService;

    public static final String HOME_PAGE = "home";
    public static final String EMPLOYEE_ADDING = "employee-adding";
    public static final String EMPLOYEE_DETAILS = "employee-details";

    public EmployeeController(EmployeeRepo employeeRepo,
                              MainService mainService) {
        this.employeeRepo = employeeRepo;
        this.mainService = mainService;
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
        return EMPLOYEE_ADDING;
    }

    @PostMapping("/employee")
    public String employeePostAdd(@Valid Employee employee,
                                  Model model) {

        /*Employee employee = new Employee(firstName, secondName, thirdName,
                position, dateOfBirth, mobilePhone, email);*/
        List<Employee> employeeList = mainService.getEmployeeList();
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
        model.addAttribute("employee", employee);

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
                                 Model model) throws Exception {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new Exception("Employee not found - " + id));

        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setThirdName(thirdName);
        employee.setPosition(position);
        employee.setDateOfBirth(dateOfBirth);
        employee.setMobilePhone(mobilePhone);
        employee.setEmail(email);
        employeeRepo.save(employee);

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return HOME_PAGE;
    }

    @PostMapping("{id}/remove")
    public String employeeDelete(@PathVariable(value = "id") long id, Model model) {
        employeeRepo.deleteById(id);

        List<Employee> employeeList = mainService.getEmployeeList();
        model.addAttribute("employees", employeeList);
        return "redirect:/home";
    }



}
