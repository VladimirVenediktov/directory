package com.moshna.directory.controller;

import com.moshna.directory.dto.EmployeeRepo;

import com.moshna.directory.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {

    private final EmployeeRepo employeeRepo;

    public static final String HOME_PAGE = "home";
    public static final String EMPLOYEE_DETAILS = "employee-details";

    public EmployeeController(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("/home")
    public String goHome()
    {
        return HOME_PAGE;
    }

    @GetMapping("/employee")
    public String employeeDetails(Model model) {
        return EMPLOYEE_DETAILS;
    }

    @PostMapping("/employee")
    public String employeePostAdd(@RequestParam String firstName,
                                  @RequestParam String secondName,
                                  @RequestParam String thirdName,
                                  @RequestParam String position,
                                  @RequestParam String dateOfBirth,
                                  @RequestParam String mobilePhone,
                                  @RequestParam String email) {

        Employee employee = new Employee(firstName, secondName, thirdName,
                position, dateOfBirth, mobilePhone, email);
        employeeRepo.save(employee);
        return HOME_PAGE;
    }















}
