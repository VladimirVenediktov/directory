package com.moshna.directory.service;

import com.moshna.directory.model.Department;
import com.moshna.directory.repo.DepartmentRepo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.validation.Valid;
@Service
public class DepartmentService {

    private DepartmentRepo departmentRepo;

    private static final String DEPARTMENT_ADDING = "department-adding";

    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public String departmentPostAdd(@Valid Department department,
                                    Model model) {
        String message = "";

        try {
            departmentRepo.save(department);
            return "redirect:/home";
        } catch (Exception e) {
            message = "validation error";
            model.addAttribute("validationMessage", message);
            return DEPARTMENT_ADDING;
        }

    }
}
