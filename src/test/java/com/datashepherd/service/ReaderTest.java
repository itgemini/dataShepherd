/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ReaderTest {

    @Test
    void readXlsx() {
        ReaderService xls = new ReaderService().xlsx("src/test/resources/relationalShipStudentCover.xlsx");
        List<Student> students = xls.readFromExcel(Student.class);
        xls.saveExcelTo("src/test/resources/validated_student_data");
        Assertions.assertFalse(students.isEmpty());
    }

    @Test
    void readXls() {
        ReaderService xls = new ReaderService().xls("src/test/resources/student_data_template.xls");
        List<Student> students = xls.readFromExcel(Student.class);
        xls.saveExcelTo("src/test/resources/validated_student_data");
        Assertions.assertFalse(students.isEmpty());
    }

}