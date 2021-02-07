package com.moshna.directory.repo;

import com.moshna.directory.model.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepo extends CrudRepository<Employee, Long>, EmployeeRepoCustom, JpaSpecificationExecutor<Employee> {

    static Specification<Employee> hasFirstName(String fullName) {
        return (employee, cq, cb) -> cb.like(employee.get("fullName"), "%" + fullName + "%");
    }


    static Specification<Employee> hasPosition(String position) {
        return (employee, cq, cb) -> cb.like(employee.get("position"), "%" + position + "%");
    }

    static Specification<Employee> hasDepartment(Long departmentID) {
        return (employee, cq, cb) -> cb.equal(employee.get("departmentID"),  departmentID);
    }
/*    static Specification<Employee> hasDepartment(Long departmentID) {
        return (employee, cq, cb) -> cb.like(employee.get("departmentID"), "%" + departmentID + "5");
    }*/

    /*@Query("select e from Employee e where concat(e.firstName, ' ', e.secondName) like '%name%' ")
    List<Employee> findAllByFullName(@Param("name") String name);*/

    /*@Query("select e from Employee e where e.fullName like '%name%'")
    List<Employee> findAllByFullName(@Param("name") String name);*/

   /* @Select({"<script>",
    "SELECT *",
            "FROM employees",
    "<set>",
            *//*"<if name != null> fullName like #{name}</if>",*//*
            "<if positionFront != null> position like #{positionFront}</if>",
            "</set>",
            "</script>"
    })
    List<Employee> findAllByFullName(*//*@Param("name") String name,*//* @Param("positionFront") String positionFront);*/


}
