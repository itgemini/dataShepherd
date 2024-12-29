/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.simple;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Sheet;

import java.util.List;

@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private final int IdStudent;
    @ExcelColumn
    private final String name;
    @ExcelColumn
    private final String email;
    @ExcelColumn
    private final Integer age;
    @ExcelColumn
    private final String address;
    @ExcelColumn
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