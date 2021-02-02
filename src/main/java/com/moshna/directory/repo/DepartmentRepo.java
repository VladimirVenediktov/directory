package com.moshna.directory.repo;

import com.moshna.directory.model.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepo extends CrudRepository<Department, Long> {
}
