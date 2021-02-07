package com.moshna.directory.repo;

import com.moshna.directory.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepo extends CrudRepository<Employee, Long> {

    /*@Query("select e from Employee e where concat(e.firstName, ' ', e.secondName) like '%name%' ")
    List<Employee> findAllByFullName(@Param("name") String name);*/

   /* @Query("select e from Employee e where('lower(e.fullName) LIKE LOWER(?)',\"%#{name}%\"))
    List<Employee> findAllByFullName(@Param("name") String name);*/


}
