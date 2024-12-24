/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.style;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Sheet;

import java.util.List;

@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    private Integer age;
    @ExcelColumn
    private String address;
    @ExcelColumn
    private String phoneNumber;
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
}