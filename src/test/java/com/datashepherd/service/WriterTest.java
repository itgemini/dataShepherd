/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.entity.MockDataGenerator;
import com.datashepherd.entity.Page;
import com.datashepherd.entity.Student;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static com.datashepherd.entity.MockDataGenerator.toByteArray;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterTest {

    @Test
    void writeStudentDataToExcel() throws IOException {
        List<Student> data = MockDataGenerator.generateStudents(100);
        InputStream inputStream = MockDataGenerator.class.getClassLoader().getResourceAsStream("profile.png");
        byte[] bytes = toByteArray(Objects.requireNonNull(inputStream));
        WriterService writer = new WriterService();
        try (FileInputStream template = new FileInputStream("src/test/resources/template.xlsx")) {
            writer.xlsx(template)
                    .cover(new Page("2.3","3.5","Mohamed Zarrouki","alfa","goa",bytes))
                    .writeToExcel(data,Student.class);
        }
        // Save the Excel file to the test resources directory
        writer.saveExcelTo("src/test/resources/student_data");

        // Assert that the file was created
        File file = new File("src/test/resources/student_data.xlsx");
        assertTrue(file.exists());
    }

    @Test
    void writeStudentDataToLargeExcel() throws IOException {
        WriterService writer = new WriterService().xlsx().writeToExcel(MockDataGenerator.generateStudents(1000),Student.class);

        // Save the Excel file to the test resources directory
        writer.saveExcelTo("src/test/resources/student_data");

        // Assert that the file was created
        File file = new File("src/test/resources/student_data.xlsx");
        assertTrue(file.exists());
    }

    @Test
    void writeStudentDataToLargeExcelWithTemplate() throws IOException {
        FileInputStream template = new FileInputStream("src/test/resources/template.xlsx");
        WriterService writer = new WriterService().xlsx(template).xlsxLargeWithTemplateAndRowAccessWindowSize(1,true).writeToExcel(MockDataGenerator.generateStudents(1000),Student.class);

        // Save the Excel file to the test resources directory
        writer.saveExcelTo("src/test/resources/student_data");

        // Assert that the file was created
        File file = new File("src/test/resources/student_data.xlsx");
        assertTrue(file.exists());
    }

    @Test
    void writeStudentDataToExcelWithTemplate() throws IOException {
        WriterService writer = new WriterService().xls().writeToExcel(MockDataGenerator.generateStudents(10),Student.class);

        // Save the Excel file to the test resources directory
        writer.saveExcelTo("src/test/resources/student_data_template");

        // Assert that the file was created
        File file = new File("src/test/resources/student_data_template.xls");
        assertTrue(file.exists());
    }
}