/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.template;

import com.datashepherd.service.WriterService;
import org.junit.jupiter.api.Test;

import java.util.List;

class WriterTest {

    @Test
    void writeRelationalShipStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx("src/test/resources/template.xlsx").writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudent.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx("src/test/resources/template.xlsx").xlsxLarge().writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudentLarge.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTable() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls("src/test/resources/template_.xls").writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudentOld.xls");
    }

    @Test
    void writeRelationalShipStudentTableWithCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(1000);
        new WriterService().xlsx().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/relationalShipStudentCover.xlsx");
    }

    @Test
    void writeRelationalShipLargeStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(10000);
        new WriterService().xlsx("src/test/resources/template.xlsx").xlsxLarge().cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudentLargeCover.xlsx");
    }

    @Test
    void writeRelationalShipXlsStudentTableCover() {
        List<Student> data = MockDataGenerator.generateRelationalShipStudentList(20);
        new WriterService().xls("src/test/resources/template_.xls").cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudentOldCover.xls");
    }
}
