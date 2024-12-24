/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.entity;

import com.datashepherd.annotation.Cell;
import com.datashepherd.annotation.Image;
import com.datashepherd.annotation.Sheet;
import com.datashepherd.annotation.style.ExcelStyle;
import com.datashepherd.annotation.style.Font;
import com.datashepherd.enums.Color;
import org.apache.poi.ss.usermodel.HorizontalAlignment;


@Sheet(name = "page", centerHeader = "Test Report", rightHeader = "Page 1", leftHeader = "Test Report")
public class Page {
    @Cell(firstRow = 0,lastRow = 0, firstColumn = 0, lastColumn = 1)
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final String columnNameColumbusVersion = "Columbus version";
    @Cell(firstRow = 1,lastRow = 1, firstColumn = 0, lastColumn = 1)
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final String columnNameF2ttVersion = "F2tt Version";
    @Cell(firstRow = 2,lastRow = 2, firstColumn = 0, lastColumn = 1)
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final String columnNameTesterName = "Tester Name";
    @Cell(firstRow = 3,lastRow = 3, firstColumn = 0, lastColumn = 1)
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final String columnNameProjectName = "project name";
    @Cell(firstRow = 4,lastRow = 4, firstColumn = 0, lastColumn = 1)
    @ExcelStyle(backgroundColor = Color.DARK_GREEN,horizontalAlignment = HorizontalAlignment.LEFT,font = @Font(color = Color.WHITE,fontHeightInPoints = 12))
    private final String columnNameGoAName = "Alfa";
    @Cell(firstRow = 0,lastRow = 0, firstColumn = 2, lastColumn = 3)
    private String columbusVersion;
    @Cell(firstRow = 1,lastRow = 1, firstColumn = 2, lastColumn = 3)
    private String f2ttVersion;
    @Cell(firstRow = 2,lastRow = 2, firstColumn = 2, lastColumn = 3)
    private String testerName;
    @Cell(firstRow = 3,lastRow = 3, firstColumn = 2, lastColumn = 3)
    private String projectName;
    @Cell(firstRow = 4,lastRow = 4, firstColumn = 2, lastColumn = 3)
    private String goAName;
    @Cell(firstRow = 0,lastRow = 4, firstColumn = 4, lastColumn = 7)
    @Image
    private byte[] photo;

    public Page(String columbusVersion, String f2ttVersion, String testerName, String projectName, String goAName,byte[] photo) {
        this.photo = photo;
        this.columbusVersion = columbusVersion;
        this.f2ttVersion = f2ttVersion;
        this.testerName = testerName;
        this.projectName = projectName;
        this.goAName = goAName;
    }

    public String getColumbusVersion() {
        return columbusVersion;
    }

    public void setColumbusVersion(String columbusVersion) {
        this.columbusVersion = columbusVersion;
    }

    public String getF2ttVersion() {
        return f2ttVersion;
    }

    public void setF2ttVersion(String f2ttVersion) {
        this.f2ttVersion = f2ttVersion;
    }

    public String getTesterName() {
        return testerName;
    }

    public void setTesterName(String testerName) {
        this.testerName = testerName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGoAName() {
        return goAName;
    }

    public void setGoAName(String goAName) {
        this.goAName = goAName;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
