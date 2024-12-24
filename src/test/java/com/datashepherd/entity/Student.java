/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.entity;

import com.datashepherd.annotation.*;
import com.datashepherd.service.CellCommentConditionImpl;
import com.datashepherd.service.DataStatusConditionImpl;

import java.util.Set;

@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    @ValidationComment(comment = CellCommentConditionImpl.class)
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    @ValidationStatus(status = DataStatusConditionImpl.class)
    private Integer age;
    @ExcelColumn
    private String address;
    private byte[] photo;
    @ExcelColumn
    private String phoneNumber;
    @Child(mappedBy = Course.class, referencedBy = "IdStudent")
    private Set<Course> courses;

    public Student() {
    }

    public Student(String name, Set<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourse() {
        return courses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(int idStudent) {
        IdStudent = idStudent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}