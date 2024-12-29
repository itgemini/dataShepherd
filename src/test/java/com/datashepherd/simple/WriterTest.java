/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.simple;

import com.datashepherd.service.WriterService;
import org.junit.jupiter.api.Test;

import java.util.List;

class WriterTest {
    @Test
    void writeSimpleStudentTable() {
        List<Student> data = MockDataGenerator.generateSimpleStudentList(1000);
        new WriterService().xlsx().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/simple.xlsx");
    }

    @Test
    void writeSimpleLargeStudentTable() {
        List<Student> data = MockDataGenerator.generateSimpleStudentList(10000);
        new WriterService().xlsx().xlsxLarge().writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/simpleLarge.xlsx");
    }

    @Test
    void writeSimpleXlsStudentTable() {
        List<Student> data = MockDataGenerator.generateSimpleStudentList(1000);
        new WriterService().xls().writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/simpleOld.xls");
    }

    @Test
    void writeRelationalShipStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/relationalShipStudent.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/relationalShipStudentLarge.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xls().writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/relationalShipStudentOld.xls");
    }

    @Test
    void writeRelationalShipStudentTableWithCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/relationalShipStudentCover.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().cover(MockDataGenerator.profile()).writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/relationalShipStudentLargeCover.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xls().cover(MockDataGenerator.profile()).writeToExcel(data,Student.class).saveExcelTo("src/test/resources/simple/relationalShipStudentOldCover.xls");
    }
}
