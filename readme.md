# DataShepherd Excel Library

## Overview
Working with Excel files in Java has always been a repetitive and cumbersome task. Each time I needed to create or read an Excel file, I found myself starting from scratch—determining which class to use (XSSFWorkbook for .xlsx files or HSSFWorkbook for .xls files), and then writing large amounts of boilerplate code to navigate sheets, rows, and cells. Simple tasks often led to messy, unclean code, while more complex scenarios required even larger, harder-to-maintain implementations. This inefficiency and complexity inspired me to create a library that simplifies working with Excel files in Java.

The goal of this library is to provide a cleaner, more intuitive API for generating and reading Excel files. By abstracting away the low-level details and repetitive steps, developers can focus on their application's core logic rather than worrying about verbose boilerplate. Whether you need to generate simple reports or handle complex data processing, this library ensures your code remains concise, maintainable, and easy to read.

## Why I Built This Library
After years of repeatedly writing similar code for Excel file manipulation, I realized there had to be a better way. Existing solutions were either too verbose or lacked the flexibility needed for different use cases. I wanted to create a tool that would:

1) Save time by eliminating the need to rewrite the same boilerplate code.
2) Provide an intuitive and developer-friendly API.
3) Make working with Excel files less of a chore and more of an efficient process.

## Who This Library Is For
This library is designed for Java developers who:

- Regularly work with Excel files for reporting, data exchange, or analytics.
- Are frustrated by the verbosity and complexity of existing libraries.
- Want to write clean, maintainable code without sacrificing functionality.

## How It Works
The library simplifies common tasks by providing:

- **Convenient Abstractions:** No need to manually deal with classes like XSSFWorkbook or HSSFWorkbook. The library handles these behind the scenes.
- **High-Level Operations:** Easily create or edit sheets, rows, and cells with minimal code.
- **Error Handling:** Built-in mechanisms to manage common issues when working with Excel files, ensuring a smoother development experience.

## Benefits
- Faster development time by removing repetitive tasks.
- Clean and readable code that focuses on business logic.
- Scalability to handle both small and large Excel-related tasks with ease.

## Why Use DataShepherd?
- **Simplified API:** A streamlined approach to interacting with Excel files, reducing the need for verbose code.
- **Read and Write Support:** Easily read from and write to both .xlsx and .xls files.
- **Customizable Data Handling:** Flexibility to handle rows, sheets, and cells with clean and intuitive methods.
- **Code Maintainability:** Minimal boilerplate ensures the code remains easy to understand and maintain, even as your requirements evolve.
- **Reduce Development Time:** Annotation-driven design eliminates repetitive code.
- **Maintain Clean Code:** Keep models declarative and easy to understand.
- **Modern Java Support:** Fully compatible with Java 17+.
- **Comprehensive Features:** Covers templates, large data handling, custom styles, and more.

## Key Features
1. **Annotation-Driven Configuration**
   - Map models to Excel using annotations like `@Sheet`, `@ExcelColumn`, `@Cell`, `@Image`, `@ValidationComment`, `@ValidationStatus`, `@ConditionalExcelCellStyle`, `@ExcelStyle`, `@Font`, `@Child`, and `@Parent`.

2. **Excel Writing**
   - Supports XSSF, SXSSF (streaming for large files), and HSSF.
   - Handles relational data, styles, templates, and dynamic covers.

3. **Excel Reading**
   - Reads Excel data back into Java objects using annotations.
   - Supports validation and transformation during import.

4. **Styling and Validation**
   - Define static and conditional styles with `@ConditionalExcelCellStyle`.
   - Apply custom logic for comments `@ValidationComment`, colors `@ExcelStyle` , and statuses `@ValidationStatus`.

5. **Templates and Covers**
   - Use existing Excel files as templates.
   - Generate custom cover sheets for a polished look.

## Installation
Add the library to your project:

**Maven:**
```xml
<dependency>
    <groupId>com.datashepherd</groupId>
    <artifactId>excel-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle:**
```gradle
implementation 'com.datashepherd:excel-library:1.0.0'
```

Ensure your project uses **Java 17+**.

## Usage Examples with Explanations

### 1. Writing Excel Files

**Basic Excel Writing:**  
Use the `WriterService` to write a list of Java objects to an Excel file.
```java
@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    private Integer age;
    @ExcelColumn
    private String address;
    @ExcelColumn
    private String phoneNumber;
}
public class example {
    public void write() {
       List<Student> students = MockDataGenerator.generateStudentList();
       new WriterService()
               .xlsx() // Specifies modern Excel format (.xlsx)
               .writeToExcel(students, Student.class) // Maps the `Student` class annotations to Excel columns
               .saveExcelTo("output/students.xlsx"); // Saves the file to the specified location
    }

   public byte[] write() {
      List<Student> students = MockDataGenerator.generateStudentList();
      return new WriterService()
              .xlsx() // Specifies modern Excel format (.xlsx)
              .writeToExcel(students, Student.class) // Maps the `Student` class annotations to Excel columns
              .content(); // return the binary file content
   }
}
```
- **`xlsx()`**: Configures the writer for `.xlsx` format (Excel 2007+).
- **`writeToExcel()`**: Takes the data (a list of Java objects) and maps it to Excel based on annotations.
- **`saveExcelTo()`**: Saves the generated file to disk.
- **`content()`**: return the binary file content.

**Large Data Handling:**  
Stream large datasets to Excel without memory issues using `xlsxLarge()`.
```java
public class example {
    public void write(){
       new WriterService()
               .xlsx()
               .xlsxLarge() // Optimized for handling large data
               .writeToExcel(students, Student.class)
               .saveExcelTo("output/large_students.xlsx");
    }
}

```
- **`xlsxLarge()`**: Uses SXSSF for streaming large datasets the call of xlsx() before xlsxLarge() is mandatory .

**Relational Data Writing:**  
Handle hierarchical or relational data, such as a `Student` with a list of `Course` objects.
```java
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
}

@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    private Integer age;
    @ExcelColumn
    private String address;
    @ExcelColumn
    private String phoneNumber;
    @Child(mappedBy = Course.class, referencedBy = "IdStudent")
    private List<Course> courses;
}

public class example {
    public void write() {
       List<Student> students = MockDataGenerator.generateRelationalData();
       new WriterService()
               .xlsx()
               .writeToExcel(students, Student.class) // Automatically handles relationships with @Child and @Parent
               .saveExcelTo("output/relational_data.xlsx");
    }
}
```
- **`@Child` and `@Parent`**: Define relationships between entities, enabling automatic writing of related records.

---

### 2. Reading Excel Files
Use `ReaderService` to load Excel data into Java objects.
```java
@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    @ValidationComment(comment = CellCommentConditionImpl.class)
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    @ValidationStatus(status = DataStatusConditionImpl.class)
    private Integer age;
    @ExcelColumn
    private String address;
    private byte[] photo;
    @ExcelColumn
    private String phoneNumber;
    @Child(mappedBy = Course.class, referencedBy = "IdStudent")
    private Set<Course> courses;
}

public class example {
    public List<Student> read() {
       ReaderService reader = new ReaderService()
               .xlsx("input/students.xlsx"); // Specify the source Excel file
       List<Student> students = reader.readFromExcel(Student.class); // Map Excel rows to Student objects and there relationShip with Courses
       reader.saveExcelTo("output/validated_students.xlsx"); // Optionally save a validated version
    }
}
```
- **`readFromExcel()`**: Parses the Excel file and converts rows into Java objects.
- **`saveExcelTo()`**: Saves the file, potentially including any applied validation or transformation.

---

### 3. Using Templates
Templates let you start with a pre-defined Excel file, preserving styles and layouts.
```java
public class example {
    public void write() {
       new WriterService()
               .xlsx("templates/student_template.xlsx") // Load an existing template
               .writeToExcel(students, Student.class) // Fill the template with data
               .saveExcelTo("output/students_from_template.xlsx");
    }
}

```
- Templates retain existing formatting, making them ideal for generating polished reports.

---

### 4. Adding Covers
Generate a dynamic cover sheet as the first page of your Excel file.
```java
@Sheet(name = "Profile")
public class Profile {
    @Image
    @Cell(firstRow = 1, firstColumn = 1, lastRow = 4, lastColumn = 3)
    private byte[] photo;
    @Cell(firstRow = 6, firstColumn = 1, lastRow = 7, lastColumn = 2)
    private final String nameLabel = "Name";
    @Cell(firstRow = 6, firstColumn = 3, lastRow = 7, lastColumn = 4)
    private String name;
    @Cell(firstRow = 9, firstColumn = 1, lastRow = 10, lastColumn = 2)
    private final String addressLabel = "Address";
    @Cell(firstRow = 9, firstColumn = 3, lastRow = 10, lastColumn = 4)
    private String address;
    @Cell(firstRow = 12, firstColumn = 1, lastRow = 13, lastColumn = 2)
    private final String phoneLabel = "Phone";
    @Cell(firstRow = 12, firstColumn = 3, lastRow = 13, lastColumn = 4)
    private String phone;
}
public class example {
    public void write() {
       Profile cover = MockDataGenerator.generateProfile(); // Create a cover page object
       new WriterService()
               .xlsx()
               .cover(cover) // Add the cover sheet
               .writeToExcel(students, Student.class)
               .saveExcelTo("output/students_with_cover.xlsx");
    }
}

```
- **`cover()`**: Adds an annotated object (e.g., `Profile`) as the first sheet in the file.

---

### 5. Styling and Formatting

**Static Styles:**  
Apply predefined styles like background color and font.
```java
@ExcelColumn(headerStyle = @ExcelStyle(
    backgroundColor = Color.LIGHT_BLUE,
    font = @Font(color = Color.WHITE, fontHeightInPoints = 12)
))
private String name;
```
- **`@ExcelStyle`**: Defines cell styles for headers or data.

**Conditional Styles:**  
Change styles dynamically based on cell values.
```java
@ConditionalExcelCellStyle(
    colorCondition = ColorConditionalImpl.class,
    backgroundColorCondition = BackgroundColorConditionImpl.class
)
private String status;
```
- **`@ConditionalExcelCellStyle`**: Applies dynamic styles using custom logic.

```java
@Sheet(name = "Course")
public class Course {
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.DARK_GREEN, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    @ConditionalExcelCellStyle(colorCondition = ColorConditionalImpl.class, backgroundColorCondition = BackgroundColorConditionImpl.class)
    private String name;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BLUE_GREY, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private int score;
    @Parent(reference = "IdStudent")
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.BROWN, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private int IdStudent;
    @ExcelColumn(headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.AUTOMATIC, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    @ValidationStatus(status = DataStatusConditionImpl.class)
    private String description;
    @ExcelColumn(format = DateFormat.FULL_DATE_TIME, headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.CORAL, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private LocalDateTime startDate;
    @ExcelColumn(format = DateFormat.FULL_DATE, headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.INDIGO, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private LocalDate endDate;
    @ExcelColumn(format = CurrencyFormat.US_DOLLAR, headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LAVENDER, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private Double price;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE_WITH_DECIMALS, headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.DARK_TEAL, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private Double level;
    @ExcelColumn(format = PercentageFormat.PERCENTAGE, headerStyle = @ExcelStyle(backgroundColor = Color.LIGHT_BLUE, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12)))
    @ExcelStyle(backgroundColor = Color.LIME, horizontalAlignment = HorizontalAlignment.LEFT, font = @Font(color = Color.WHITE, fontHeightInPoints = 12))
    private Integer order;
}

@Sheet(name = "Student")
public class Student {
    @ExcelColumn
    private int IdStudent;
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String email;
    @ExcelColumn
    private Integer age;
    @ExcelColumn
    private String address;
    @ExcelColumn
    private String phoneNumber;
    @Child(mappedBy = Course.class, referencedBy = "IdStudent")
    private List<Course> courses;
}

public class Test {
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
      new WriterService().xlsx("src/test/resources/template.xlsx").cover(MockDataGenerator.profile()).writeToExcel(data, Student.class).saveExcelTo("src/test/resources/template/relationalShipStudentCover.xlsx");
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
```
---

### 6. Validation and Comments

**Custom Comments:**  
Add comments to cells based on their values.
```java
public class CellCommentConditionImpl implements CellCommentCondition {
    @Override
    public String applyCondition(Object fieldValue) {
        return "Custom comment logic based on value";
    }
}
```

**Status Markers:**  
Use conditions to assign statuses like SUCCESS, WARNING, or ERROR.
```java
public class DataStatusConditionImpl implements DataStatusCondition {
    @Override
    public <T> Status applyCondition(T fieldValue) {
        if (fieldValue instanceof Integer value && value > 50) {
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }
}
```

## Examples
- **Relational Data:** Handle relationships using `@Child` and `@Parent`.
- **Custom Styles:** Apply styles dynamically with `@ExcelStyle` or `@ConditionalExcelCellStyle`.
- **Validation:** Implement `CellCommentCondition` or `DataStatusCondition` for tailored validation logic.

## Contributing
Contributions are welcome! Please submit pull requests or report issues to improve the library.

## License
This library is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more details.

