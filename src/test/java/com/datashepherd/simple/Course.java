/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.simple;

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
    @ExcelColumn
    private String name;
    @ExcelColumn
    private int score;
    @Parent(reference = "IdStudent")
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    private String description;
    @ExcelColumn
    private LocalDateTime startDate;
    @ExcelColumn
    private LocalDate endDate;
    @ExcelColumn
    private Double price;
    @ExcelColumn
    private Double level;
    @ExcelColumn
    private Integer order;

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
}