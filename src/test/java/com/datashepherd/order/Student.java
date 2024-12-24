/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.order;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Sheet;

import java.util.List;

@Sheet(name = "Student",leftHeader = "Student Data",centerHeader = "Page Number &P",rightHeader = "Page &N of &M")
public class Student {
    @ExcelColumn(position = 1)
    private int IdStudent;
    @ExcelColumn(position = 3)
    private String name;
    @ExcelColumn(position = 4)
    private String email;
    @ExcelColumn(position = 5)
    private Integer age;
    @ExcelColumn(position = 6)
    private String address;
    @ExcelColumn(position = 8)
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