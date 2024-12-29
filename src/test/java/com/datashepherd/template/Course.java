/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.template;

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
    private final String name;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BLUE_GREY,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final int score;
    @Parent(reference = "IdStudent")
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BROWN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final int IdStudent;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.AUTOMATIC,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    @ValidationStatus(status = DataStatusConditionImpl.class)
    private final String description;
    @ExcelColumn(format = DateFormat.FULL_DATE_TIME,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.CORAL,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final LocalDateTime startDate;
    @ExcelColumn(format = DateFormat.FULL_DATE,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.INDIGO,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final LocalDate endDate;
    @ExcelColumn(format = CurrencyFormat.US_DOLLAR,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LAVENDER,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final Double price;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE_WITH_DECIMALS,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.DARK_TEAL,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final Double level;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE,headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LIME,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
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