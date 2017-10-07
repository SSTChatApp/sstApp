package com.example.mine.mychatapplication;



class Department {
    public String name;
    public String departmentCode;
    public String divisionCode;
    public String closed;

    public Department(String name, String departmentCode, String divisionCode, String closed) {
        this.name = name;
        this.departmentCode = departmentCode;
        this.divisionCode = divisionCode;
        this.closed = closed;
    }

    public Department() {
        this.name = " ";
        this.departmentCode = " ";
        this.divisionCode = " ";
        this.closed = " ";
    }
}
