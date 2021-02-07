package com.moshna.directory.repo;

import com.moshna.directory.model.Employee;

import java.util.List;

public interface EmployeeRepoCustom {
    List<Employee> findEmployees(String firstName, String secondName, String position);
}
