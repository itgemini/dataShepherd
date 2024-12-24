/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.order;

import com.datashepherd.service.WriterService;
import org.junit.jupiter.api.Test;

import java.util.List;

class WriterTest {

    @Test
    void writeRelationalShipStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudent.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudentLarge.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudentOld.xls");
    }

    @Test
    void writeRelationalShipStudentTableWithCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudentCover.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx().xlsxLarge().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudentLargeCover.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/order/relationalShipStudentOldCover.xls");
    }
}
