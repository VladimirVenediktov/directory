package com.moshna.directory.dto;

public class DepartmentDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String title;

    public DepartmentDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DepartmentDto(Long id, String title) {
        this.title = title;
        this.id = id;
    }

}
