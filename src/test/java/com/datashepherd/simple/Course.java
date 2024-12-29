/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.simple;

import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Parent;
import com.datashepherd.annotation.Sheet;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Sheet(name = "Course")
public class Course {
    @ExcelColumn
    private final String name;
    @ExcelColumn
    private final int score;
    @Parent(reference = "IdStudent")
    @ExcelColumn
    private final int IdStudent;
    @ExcelColumn
    private final String description;
    @ExcelColumn
    private final LocalDateTime startDate;
    @ExcelColumn
    private final LocalDate endDate;
    @ExcelColumn
    private final Double price;
    @ExcelColumn
    private final Double level;
    @ExcelColumn
    private final Integer order;

    public Course(String name, int score, int idStudent, String description, LocalDateTime startDate, LocalDate endDate, Double price, Double level, Integer order) {
        this.name = name;
        this.score = score;
        this.IdStudent = idStudent;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.level = level;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getIdStudent() {
        return IdStudent;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getPrice() {
        return price;
    }

    public Double getLevel() {
        return level;
    }

    public Integer getOrder() {
        return order;
    }
}