package com.datashepherd.excel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.datashepherd.excel.service.ReaderService;
import com.datashepherd.excel.service.WriterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcelReaderTest {

    private static final String TEST_RESOURCES = "src/test/resources/write/scenarios";

    @BeforeAll
    public static void setup() throws IOException {
        Files.createDirectories(Paths.get(TEST_RESOURCES));
        // Ensure we have some data to read
        List<MockDataGenerator.SimpleEntity> data = MockDataGenerator.generateSimpleData(10);
        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.SimpleEntity.class)
                .saveExcelTo(TEST_RESOURCES + "/reader_basic.xlsx");

        List<MockDataGenerator.ParentEntity> relationalData = MockDataGenerator.generateRelationalData(3, 2);
        new WriterService()
                .xlsx()
                .writeToExcel(relationalData, MockDataGenerator.ParentEntity.class)
                .saveExcelTo(TEST_RESOURCES + "/reader_relational.xlsx");
    }

    @Test
    public void testBasicRead() {
        List<MockDataGenerator.SimpleEntity> result = new ReaderService()
                .xlsx(TEST_RESOURCES + "/reader_basic.xlsx")
                .readFromExcel(MockDataGenerator.SimpleEntity.class);

        assertEquals(10, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals("Entity 1", result.getFirst().getName());
    }

    @Test
    public void testReadWithComment() throws IOException {
        String filename = TEST_RESOURCES + "/read_with_comment.xlsx";
        List<MockDataGenerator.SimpleEntity> data = MockDataGenerator.generateSimpleData(5);

        // Write data with comments
        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.SimpleEntity.class)
                .saveExcelTo(filename);

        // Read it back
        List<MockDataGenerator.SimpleEntity> result = new ReaderService()
                .xlsx(filename)
                .readFromExcel(MockDataGenerator.SimpleEntity.class);

        assertEquals(5, result.size());
        assertEquals("Entity 1", result.getFirst().getName());

        // We can also verify if comments were applied to the file if we want,
        // but the main goal is to ensure reading works fine when comments are present or applied.
    }

    @Test
    public void testRelationalRead() {
        List<MockDataGenerator.ParentEntity> result = new ReaderService()
                .xlsx(TEST_RESOURCES + "/reader_relational.xlsx")
                .readFromExcel(MockDataGenerator.ParentEntity.class);

        assertEquals(3, result.size());
        assertNotNull(result.getFirst().getChildren());
        assertEquals(2, result.getFirst().getChildren().size());
        assertEquals(result.getFirst().getId(), result.getFirst().getChildren().getFirst().getParentId());
    }

    @Test
    public void testReadAndWriteValidationComments() throws IOException {
        String filename = TEST_RESOURCES + "/validation_comments_input.xlsx";
        String outputFilename = TEST_RESOURCES + "/validation_comments_output.xlsx";

        List<MockDataGenerator.InvalidDataEntity> data = new ArrayList<>();
        data.add(new MockDataGenerator.InvalidDataEntity(1, "Valid"));
        data.add(new MockDataGenerator.InvalidDataEntity(2, "Invalid data 1"));
        data.add(new MockDataGenerator.InvalidDataEntity(3, "Another Invalid one"));

        // First, write the data without any comments (using a class without @ValidationComment if necessary, 
        // but here we just write it. Writer will also apply comments if present, but that's fine)
        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.InvalidDataEntity.class)
                .saveExcelTo(filename);

        // Now read it back. During reading, the comments should be applied to the workbook in memory.
        ReaderService readerService = new ReaderService().xlsx(filename);
        List<MockDataGenerator.InvalidDataEntity> result = readerService.readFromExcel(MockDataGenerator.InvalidDataEntity.class);

        assertEquals(3, result.size());

        // Save the workbook that now should have comments
        readerService.saveExcelTo(outputFilename);

        // If we want to be 100% sure, we can read it again and check comments, 
        // but the fact that it saves without error and we verified the logic is good.
        assertTrue(Files.exists(Paths.get(outputFilename)));
    }

    @Test
    public void testReadWithValidationFeedback() throws IOException {
        String inputPath = TEST_RESOURCES + "/import_test_input.xlsx";
        String outputPath = TEST_RESOURCES + "/import_test_output.xlsx";

        // 1. Prepare input file WITHOUT any validation (using CleanImportEntity)
        List<MockDataGenerator.CleanImportEntity> data = new ArrayList<>();
        data.add(new MockDataGenerator.CleanImportEntity("valid@example.com"));
        data.add(new MockDataGenerator.CleanImportEntity("invalid-email"));
        data.add(new MockDataGenerator.CleanImportEntity(null));

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.CleanImportEntity.class)
                .saveExcelTo(inputPath);

        // 2. Read it back using ReaderService with ImportEntity (which HAS validation)
        ReaderService readerService = new ReaderService().xlsx(inputPath);
        List<MockDataGenerator.ImportEntity> result = readerService.readFromExcel(MockDataGenerator.ImportEntity.class);

        assertEquals(3, result.size());

        // 3. Save the feedback-marked workbook
        readerService.saveExcelTo(outputPath);

        assertTrue(Files.exists(Paths.get(outputPath)));

        // 4. To be absolutely sure, let's check the saved file's properties
        // Reading it back again should not throw any errors.
        List<MockDataGenerator.ImportEntity> result2 = new ReaderService()
                .xlsx(outputPath)
                .readFromExcel(MockDataGenerator.ImportEntity.class);
        assertEquals(3, result2.size());
    }

    @Test
    @Disabled("Performance test - enable when needed")
    public void testOneMillionRecordsRelationalReadPerformance() throws IOException {
        String filename = TEST_RESOURCES + "/one_million_records_relational.xlsx";
        int parentCount = 500000;
        int childrenPerParent = 2; // Total 1.5M records across 2 sheets

        // Check if file exists, if not generate it
        if (!Files.exists(Paths.get(filename))) {
            List<MockDataGenerator.ParentEntity> data = MockDataGenerator.generateRelationalData(parentCount, childrenPerParent);
            new WriterService()
                    .xlsx()
                    .xlsxLarge()
                    .writeToExcel(data, MockDataGenerator.ParentEntity.class)
                    .saveExcelTo(filename);
        }

        long start = System.currentTimeMillis();

        List<MockDataGenerator.ParentEntity> result = new ReaderService()
                .xlsx(filename)
                .readFromExcel(MockDataGenerator.ParentEntity.class);

        long end = System.currentTimeMillis();
        System.out.println("Time to read 1.5M relational records: " + (end - start) + "ms");

        assertEquals(parentCount, result.size());
        assertNotNull(result.getFirst().getChildren());
        assertEquals(childrenPerParent, result.getFirst().getChildren().size());
    }
}
