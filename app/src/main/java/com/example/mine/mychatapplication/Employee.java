package com.example.mine.mychatapplication;



class Employee {
    public String nationalID;
    public String name;
    public String birthDate;
    public String sectionCode;
    public String departmentCode;
    public String positionCode;
    public String hiringDate;
    public String sex;
    public String birthDestination;
    public String sectionName;
    public String departmentName;

    public Employee(String nationalID, String name, String birthDate,
                    String sectionCode, String positionCode, String hiringDate, String sex, String birthDestination) {
        this.nationalID = nationalID;
        this.name = name;
        this.birthDate = birthDate;
        this.sectionCode = sectionCode;
        this.positionCode = positionCode;
        this.hiringDate = hiringDate;
        this.sex = sex;
        this.birthDestination = birthDestination;
    }


    public Employee() {
        this.nationalID = "";
        this.name = "";
        this.birthDate = "";
        this.sectionCode = "";
        this.positionCode = "";
        this.hiringDate = "";
        this.sex = "";
        this.birthDestination = "";
        this.departmentCode="";
        this.departmentName="";
        this.sectionName="";
    }

}
