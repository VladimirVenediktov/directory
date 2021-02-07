package com.moshna.directory.controller;

import com.moshna.directory.model.Department;
import com.moshna.directory.repo.DepartmentRepo;
import com.moshna.directory.service.DepartmentService;
import com.moshna.directory.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class DepartmentController {

    private final DepartmentService departmentService;

    private static final String DEPARTMENT_ADDING = "department-adding";

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/department")
    public String departmentAdding() {
        return DEPARTMENT_ADDING;
    }

    @PostMapping("/department")
    public String departmentPostAdd(@Valid Department department,
                                    Model model) {
        return departmentService.departmentPostAdd(department, model);
    }
}
