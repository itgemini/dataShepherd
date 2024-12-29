/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.template;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Sheet;

import java.util.List;

@Sheet(name = "Student",leftHeader = "Student Data",centerHeader = "Page Number &P",rightHeader = "Page &N of &M")
public class Student {
    @ExcelColumn(position = 1)
    private final int IdStudent;
    @ExcelColumn(position = 3)
    private final String name;
    @ExcelColumn(position = 4)
    private final String email;
    @ExcelColumn(position = 5)
    private final Integer age;
    @ExcelColumn(position = 6)
    private final String address;
    @ExcelColumn(position = 8)
    private final String phoneNumber;
    @Child(mappedBy = Course.class, referencedBy = "IdStudent")
    private List<Course> courses;

    public Student(int idStudent, String name, String email, Integer age, String address, String phoneNumber) {
        this.IdStudent = idStudent;
        this.name = name;
        this.email = email;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Student(int idStudent, String name, String email, Integer age, String address, String phoneNumber,List<Course> courses) {
        this.IdStudent = idStudent;
        this.name = name;
        this.email = email;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.courses = courses;
    }

    public int getIdStudent() {
        return IdStudent;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Course> getCourses() {
        return courses;
    }
}