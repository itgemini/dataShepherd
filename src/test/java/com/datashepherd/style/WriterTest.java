/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.style;

import com.datashepherd.service.WriterService;
import org.junit.jupiter.api.Test;

import java.util.List;

class WriterTest {

    @Test
    void writeRelationalShipStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudent.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudentLarge.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudentOld.xls");
    }

    @Test
    void writeRelationalShipStudentTableWithCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudentCover.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudentLargeCover.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/style/relationalShipStudentOldCover.xls");
    }
}
