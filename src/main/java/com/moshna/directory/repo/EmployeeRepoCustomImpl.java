package com.moshna.directory.repo;

import com.moshna.directory.model.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Repository
public class EmployeeRepoCustomImpl implements EmployeeRepoCustom{

    EntityManager entityManager;

    public EmployeeRepoCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findEmployees(String firstName, String secondName, String position) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

        Root<Employee> employeeRoot = cq.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        return entityManager.createQuery(cq).getResultList();
    }
}
