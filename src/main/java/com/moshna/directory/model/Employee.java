package com.moshna.directory.model;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "employees",
    uniqueConstraints = {
        @UniqueConstraint(columnNames ={"firstName", "secondName","Email"})
    })
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String position;
    private String dateOfBirth;
    private String mobilePhone;
    @Email(message = "Email is not correct")
    private String email;
    private Long departmentID;

    private String fileName;

    public Employee() {
    }

    public Employee(String firstName, String secondName, String thirdName, String position, String dateOfBirth,
                    String mobilePhone, String email, Long departmentID) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.departmentID = departmentID;
    }
/*
    public Employee(Long id, String firstName, String secondName, String mobilePhone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.mobilePhone = mobilePhone;
        this.email = email;
    }*/

    public Long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
