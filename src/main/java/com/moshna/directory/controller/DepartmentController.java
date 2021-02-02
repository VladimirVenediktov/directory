package com.moshna.directory.controller;

import com.moshna.directory.model.Department;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DepartmentController {

    private DepartmentRepo departmentRepo;
    private final MainService mainService;

    private static final String DEPARTMENT_ADDING = "department-adding";

    public DepartmentController(DepartmentRepo departmentRepo, MainService mainService) {
        this.departmentRepo = departmentRepo;
        this.mainService = mainService;
    }

    @GetMapping("/department")
    public String departmentAdding() {
        return DEPARTMENT_ADDING;
    }

    @PostMapping("/department")
    public String departmentPostAdd(@Valid Department department,
                                    Model model) {
        //List<Department> departmentList = mainService.getDepartmentList();
        String message = "";

        try {
            departmentRepo.save(department);
            //model.addAttribute("departmentList", departmentList);

            return "redirect:/home";
        } catch (Exception e) {
            message = "validation error";
            //model.addAttribute("validationMessage", message);
            return DEPARTMENT_ADDING;
        }

    }
}
