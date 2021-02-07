package com.moshna.directory.controller;

import com.moshna.directory.model.Employee;
import com.moshna.directory.service.EmployeeService;
import com.moshna.directory.service.MainService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class EmployeeController {

    private final MainService mainService;
    private final EmployeeService employeeService;



    public EmployeeController(MainService mainService,
                              EmployeeService employeeService) {
        this.mainService = mainService;
        this.employeeService = employeeService;
    }

    @GetMapping("/home")
    public String goHome(Model model)
    {
        return employeeService.goHome(model);
    }

    @GetMapping("/employee")
    public String employeeAdding(Model model) {

        return employeeService.employeeAdding(model);
    }

    @PostMapping("/employee")
    public String employeePostAdd(@Valid Employee employee,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) throws Exception {
        return employeeService.employeePostAdd(employee, file, model);
    }



    @GetMapping("{id}")
    public String employeeDetails(@PathVariable(value = "id") long id, Model model) throws Exception {

        return employeeService.employeeDetails(id, model);
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
        return employeeService.employeeUpdate(id, firstName,secondName, thirdName,
                position, dateOfBirth, mobilePhone, email, departmentID, model);
    }

    @PostMapping("{id}/remove")
    public String employeeDelete(@PathVariable(value = "id") long id, Model model) {
        return employeeService.employeeDelete(id, model);
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        mainService.exportToExcel(response);
    }

    @PostMapping("sortingByName")
    public String sortingByName(Model model) {
        return employeeService.sortingByName(model);
    }
    @PostMapping("sortingByPosition")
    public String sortingByPosition(Model model) {
        return employeeService.sortingByPosition(model);
    }

    @PostMapping("filterByParams")
    public String filterByName(@RequestParam String name,
                               @RequestParam String position,
                               @RequestParam Long departmentID,
                               Model model) {

        return employeeService.filterByParams(name, position, departmentID, model);
    }
}
