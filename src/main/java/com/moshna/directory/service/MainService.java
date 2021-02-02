package com.moshna.directory.service;

import com.moshna.directory.dto.EmployeeRepo;
import com.moshna.directory.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MainService {

    private final EmployeeRepo employeeRepo;

    public MainService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> getEmployeeList() {
        try {
            Iterable<Employee> employees = employeeRepo.findAll();
            List<Employee> employeeList = new ArrayList<>();
            employees.forEach(employee -> employeeList.add(employee));
            return employeeList;
        }
        catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
