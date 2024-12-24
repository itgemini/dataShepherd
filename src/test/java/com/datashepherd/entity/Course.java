/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.entity;

import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Parent;
import com.datashepherd.annotation.Sheet;
import com.datashepherd.annotation.ValidationStatus;
import com.datashepherd.annotation.style.ConditionalExcelCellStyle;
import com.datashepherd.annotation.style.ExcelStyle;
import com.datashepherd.annotation.style.Font;
import com.datashepherd.enums.Color;
import com.datashepherd.enums.CurrencyFormat;
import com.datashepherd.enums.DateFormat;
import com.datashepherd.enums.PercentageFormat;
import com.datashepherd.service.BackgroundColorConditionImpl;
import com.datashepherd.service.ColorConditionalImpl;
import com.datashepherd.service.DataStatusConditionImpl;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Sheet(name = "Course")
public class Course {
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    @ConditionalExcelCellStyle(colorCondition = ColorConditionalImpl.class, backgroundColorCondition = BackgroundColorConditionImpl.class)
    private String name;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BLUE_GREY,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private int score;
    @Parent(reference = "IdStudent")
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BROWN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private int IdStudent;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.AUTOMATIC,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    @ValidationStatus(status = DataStatusConditionImpl.class)
    private String description;
    @ExcelColumn(format = DateFormat.FULL_DATE_TIME,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.CORAL,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private LocalDateTime startDate;
    @ExcelColumn(format = DateFormat.FULL_DATE,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.INDIGO,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private LocalDate endDate;
    @ExcelColumn(format = CurrencyFormat.US_DOLLAR,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LAVENDER,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private Double price;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE_WITH_DECIMALS,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.DARK_TEAL,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private Double level;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LIME,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private Integer order;

    public Course() {
    }

    public Course(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(int idStudent) {
        IdStudent = idStudent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}