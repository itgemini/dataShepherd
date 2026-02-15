# 📊 DataShepherd: The Ultimate Java Excel API

[![Java Version](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-2.5.2-blue)](https://search.maven.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

DataShepherd is a high-performance, annotation-driven Java library that transforms Excel operations from complex to
simple. Built on Apache POI, it provides a declarative, clean API using Java annotations that handles everything from
basic data export to complex multi-sheet relationships, validation, styling, and image embedding.

---

## 🎯 Why DataShepherd?

### Traditional Apache POI Approach (50+ lines)

```java
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Users");
Row headerRow = sheet.createRow(0);
Cell cell1 = headerRow.createCell(0);
cell1.

setCellValue("ID");

CellStyle headerStyle = workbook.createCellStyle();
Font font = workbook.createFont();
font.

setBold(true);
headerStyle.

setFont(font);
cell1.

setCellStyle(headerStyle);
// ... 40 more lines for data, styling, etc.
```

### DataShepherd Approach (5 lines)

```java

@Sheet(name = "Users")
public class User {
    @ExcelColumn(name = "ID")
    private int id;
    @ExcelColumn(name = "Name")
    private String name;
}

new

WriterService().

xlsx().

writeToExcel(users, User .class).

saveExcelTo("users.xlsx");
```

**Result: 90% less code, 100% more readable**

---

## 🚀 Quick Start

### Maven Installation
```xml
<dependency>
    <groupId>io.github.itgemini</groupId>
    <artifactId>dataShepherd</artifactId>
    <version>2.2.2</version>
</dependency>
```

### Requirements

- **Java:** 21 (required)
- **Apache POI:** Automatically managed as transitive dependency
- **Memory:** Minimal footprint with streaming support for large files

---

## 📝 Writing Mode: Complete Examples

### Example 1: Basic Writing - Automatic Column Mapping

DataShepherd automatically maps your Java fields to Excel columns in declaration order. No configuration needed for
simple exports.

**📋 Use Case:** Quick data exports, simple reports, CSV replacements

**Java Entity:**
```java

@Sheet(name = "BasicData")
public class User {
    @ExcelColumn(name = "ID")
    private int id;

    @ExcelColumn(name = "Full Name")
    private String name;

    // Constructor, getters, setters
}
```

**Write Code:**

```java
List<User> users = List.of(
        new User(1, "John Doe"),
        new User(2, "Jane Smith"),
        new User(3, "Bob Johnson")
);

new

WriterService()
    .

xlsx()
    .

writeToExcel(users, User .class)
    .

saveExcelTo("basic.xlsx");
```

**Direct Byte Content:**
Need to return the file as a web response or store it in a database? Get the `byte[]` directly:

```java
byte[] content = new WriterService()
        .xlsx()
        .writeToExcel(users, User.class)
        .content(); // Returns byte array directly
```

**How it Works:**

- `@Sheet(name = "BasicData")` → Creates Excel tab named "BasicData"
- `@ExcelColumn(name = "ID")` → Column A header = "ID", automatically uses numeric type
- `@ExcelColumn(name = "Full Name")` → Column B header = "Full Name", uses string type
- Fields are mapped left-to-right in declaration order (id → Column A, name → Column B)

---

### Example 2: Manual Column Positioning & Header Row Control

For professional reports where you need blank rows at the top (for logos, titles) or specific column ordering, use
`position` and `headerRow`.

**📋 Use Case:** Financial reports, executive summaries, forms with custom headers

**Java Entity:**
```java

@Sheet(name = "FormattedData", headerRow = 2) // Header starts at row 3 (index 2)
public class Product {
    @ExcelColumn(name = "SKU", position = 1)  // Column B
    private String sku;

    @ExcelColumn(name = "Price", position = 0) // Column A (appears before SKU)
    private double price;

    @ExcelColumn(name = "Stock", position = 2) // Column C
    private int stock;
}
```

**Write Code:**

```java
List<Product> products = List.of(
        new Product("PROD-001", 99.99, 50),
        new Product("PROD-002", 149.99, 30)
);

new

WriterService()
    .

xlsx()
    .

writeToExcel(products, Product .class)
    .

saveExcelTo("formatted.xlsx");
```

**Key Features:**

- `headerRow = 2`: Rows 0 and 1 left empty for your custom content (logos, titles)
- `position = 0`: Explicitly sets column index (overrides declaration order)
- Perfect for matching existing templates or corporate formats

**💡 Pro Tip:** Use the empty rows above the header for:

- Company logos (`@Picture` annotation in `@Sheet`)
- Report titles (`@Cell` annotation)
- Date ranges, filters, or metadata

---

### Example 3: Professional Styling - Static & Conditional

Apply corporate branding and highlight important data with zero manual style management. DataShepherd automatically
caches styles to avoid Excel's 64k style limit.

**📋 Use Case:** Branded reports, data quality indicators, executive dashboards

**Custom Style Condition:**
```java
public class PriceColorCondition implements ColorCondition {
    @Override
    public <T> Color applyCondition(T value) {
        double price = ((Number) value).doubleValue();
        if (price > 1000) return Color.RED;      // High-value items
        if (price > 100) return Color.ORANGE;    // Medium-value items
        return Color.GREEN;                       // Budget items
    }
}

public class PriceBgCondition implements BackgroundColorCondition {
    @Override
    public <T> Color applyCondition(T value) {
        return Color.WHITE; // Default background
    }
}
```

**Java Entity:**

```java

@Sheet(name = "StyledProducts")
public class StyledProduct {
    // Static styling: Blue header with white bold text
    @ExcelColumn(name = "Product Name",
            headerStyle = @ExcelStyle(
                    backgroundColor = Color.BLUE,
                    font = @Font(color = Color.WHITE,
                            fontHeightInPoints = 12,
                            fontStyle = FontStyle.BOLD)
            ))
    @ExcelStyle(backgroundColor = Color.WHITE, font = @Font(color = Color.BLACK, fontHeightInPoints = 11)) // Data cells
    private String name;

    // Conditional styling: Color changes based on price value
    @ExcelColumn(name = "Price ($)")
    @ConditionalExcelCellStyle(
            colorCondition = PriceColorCondition.class,
            backgroundColorCondition = PriceBgCondition.class
    )
    private double price;

    @ExcelColumn(name = "Category")
    @ExcelStyle(backgroundColor = Color.LIGHT_GRAY, font = @Font(color = Color.BLACK, fontHeightInPoints = 11))
    private String category;
}
```

**Write Code:**

```java
List<StyledProduct> products = List.of(
        new StyledProduct("Premium Laptop", 1299.99, "Electronics"),
        new StyledProduct("Wireless Mouse", 29.99, "Accessories"),
        new StyledProduct("4K Monitor", 599.99, "Electronics")
);

new

WriterService()
    .

xlsx()
    .

writeToExcel(products, StyledProduct .class)
    .

saveExcelTo("styled.xlsx");
```

**Advanced Features:**

- **Style Caching:** Styles are automatically pooled by `ExcelStyleManager` to prevent Excel's 64k limit
- **Conditional Logic:** Different cells in the same column can have different colors
- **Full Control:** Font family, size, color, bold/italic, background, borders, alignment

---

### Example 4: Data Validation with Status Colors & Comments

Generate "Report Cards" for your data imports. DataShepherd validates data and marks errors with colored cells and Excel
comments - perfect for user feedback loops.

**📋 Use Case:** Data import validation, quality reports, user error feedback

**Validation Logic:**
```java
// Validator: Determines cell background color
public class EmailValidator implements DataStatusCondition {
    @Override
    public <T> Status applyCondition(T fieldValue) {
        String email = (String) fieldValue;
        if (email == null || email.isEmpty()) {
            return Status.WARNING;  // Yellow background
        }
        if (!email.contains("@") || !email.contains(".")) {
            return Status.ERROR;    // Red background
        }
        return Status.SUCCESS;      // Green background
    }
}

// Comment Generator: Provides detailed error messages
public class EmailErrorComment implements CellCommentCondition {
    @Override
    public <T> String applyCondition(T fieldValue) {
        String email = (String) fieldValue;
        if (email == null || email.isEmpty()) {
            return "Email is required";
        }
        if (!email.contains("@")) {
            return "Invalid email format: missing @ symbol";
        }
        if (!email.contains(".")) {
            return "Invalid email format: missing domain extension";
        }
        return ""; // No comment for valid emails
    }
}
```

**Java Entity:**

```java

@Sheet(name = "ValidatedUsers")
public class ValidatedUser {
    @ExcelColumn(name = "User ID")
    private int id;

    @ExcelColumn(name = "Email Address")
    @ValidationStatus(status = EmailValidator.class)        // Adds color
    @ValidationComment(comment = EmailErrorComment.class)   // Adds comment
    private String email;
}
```

**Write Code:**
```java
List<ValidatedUser> users = List.of(
        new ValidatedUser(1, "john@example.com"),
        new ValidatedUser(2, "invalid-email"),     // Will be red
        new ValidatedUser(3, ""),                   // Will be yellow
        new ValidatedUser(4, "jane@test.com")      // Will be green
);

new

WriterService()
    .

xlsx()
    .

writeToExcel(users, ValidatedUser .class)
    .

saveExcelTo("validated.xlsx");
```

**Status Colors:**

- `Status.SUCCESS` → Green background
- `Status.ERROR` → Red background
- `Status.WARNING` → Yellow background
- `Status.DEFAULT` → White background (no highlighting)

**💡 Real-World Workflow:**

1. User uploads data file to your system
2. You read it with DataShepherd + validation annotations
3. System generates new file with errors highlighted
4. User downloads corrected file, fixes errors (red cells)
5. User re-uploads → repeat until all green

---

### Example 5: Images as Column Data

Embed images directly in cells - perfect for product catalogs, employee directories, or any data with visual components.

**📋 Use Case:** Product catalogs, employee badges, real estate listings, inventory with photos

**Java Entity:**
```java

@Sheet(name = "ProductCatalog")
public class ProductWithImage {
    @ExcelColumn(name = "SKU")
    private String sku;

    @ExcelColumn(name = "Product Name")
    private String name;

    @ExcelColumn(name = "Photo")
    @Image(extension = ImageType.PICTURE_TYPE_JPEG, width = 100, height = 100)
    private byte[] photo;  // Image data as byte array
}
```

**Write Code:**

```java
// Load image from file
byte[] photo1 = Files.readAllBytes(Path.of("product1.jpg"));

List<ProductWithImage> products = List.of(
        new ProductWithImage("PROD-001", "Laptop", photo1)
);

new

WriterService()
    .

xlsx()
    .

writeToExcel(products, ProductWithImage .class)
    .

saveExcelTo("catalog.xlsx");
```

**Image Configuration:**

- `extension`: JPEG, PNG, GIF, BMP, etc. (use `ImageType` enum)
- `width` & `height`: Dimensions in pixels
- Images are automatically centered in cells
- Row height adjusts to fit image

---

### Example 6: Professional Cover Sheets with Images

Create presentation-ready workbooks with branded cover pages. Perfect for reports sent to executives or clients.

**📋 Use Case:** Executive reports, client deliverables, quarterly reviews, audits

**Cover Sheet Definition:**
```java

@Sheet(name = "CoverPage",
        picture = @Picture(
                path = "company_logo.png",  // Path to your logo
                startColumn = 1,             // Logo spans B1:D5
                startRow = 1,
                endColumn = 3,
                endRow = 5
        ))
public class ReportCover {
    @Cell(row = 6, column = 1)
    private final String reportTitle = "Q4 2024 Performance Report";

    @Cell(row = 7, column = 1)
    private final String reportSubtitle = "Financial Analysis & Projections";

    @Cell(row = 8, column = 1)
    private final String preparedBy = "Prepared by: Finance Team";
}
```

**Write Code:**
```java
ReportCover cover = new ReportCover();
List<FinancialRecord> data = loadFinancialData();

new

WriterService()
    .

xlsx()
    .

cover(cover)  // Injects cover sheet FIRST
    .

writeToExcel(data, FinancialRecord .class)  // Then data sheets
    .

saveExcelTo("Q4_Report.xlsx");
```

**Cover Sheet Features:**

- **`@Picture`**: Embed logo/image spanning multiple cells in `@Sheet`
- **`@Cell`**: Place text at specific coordinates
- **Multi-Sheet**: Cover + data sheets in one workbook

**Multi-Sheet Structure:**

```
📄 Q4_Report.xlsx
├── 📋 CoverPage (Tab 1)
│   ├── 🖼️ Company Logo (B1:D5)
│   ├── "Q4 2024 Performance Report" (B6)
│   └── "Prepared by: Finance Team" (B8)
│
└── 📋 FinancialData (Tab 2)
    ├── Department | Revenue | Growth
    ├── Sales | 45.2M | +12.5%
    └── Marketing | 18.7M | -3.2%
```

---

### Example 7: Master-Detail Relationships (Parent-Child Data)

Handle complex hierarchical data with automatic relationship management. DataShepherd splits your object graph into
normalized sheets and maintains referential integrity.

**📋 Use Case:** Orders & line items, departments & employees, projects & tasks

**Parent Entity (Order):**
```java

@Sheet(name = "Orders")
public class Order {
    @ExcelColumn(name = "Order ID")
    private int id;

    @ExcelColumn(name = "Customer Name")
    private String customerName;

    // Child relationship: writes items to separate sheet
    @Child(mappedBy = OrderItem.class, referencedBy = "orderId")
    private List<OrderItem> items;
}
```

**Child Entity (OrderItem):**
```java

@Sheet(name = "OrderItems")
public class OrderItem {
    @ExcelColumn(name = "Item ID")
    private int id;

    @ExcelColumn(name = "Product Name")
    private String productName;

    // Parent reference: links back to Order.id
    @Parent(reference = "id")
    @ExcelColumn(name = "Order ID (Ref)")
    private int orderId;
}
```

**Write Code:**
```java
// DataShepherd automatically splits into 2 sheets
new WriterService()
    .

xlsx()
    .

writeToExcel(orders, Order .class)
    .

saveExcelTo("orders.xlsx");
```

**How It Works:**

1. `@Child` on `Order.items` tells DataShepherd to write items to separate sheet
2. `referencedBy = "orderId"` specifies the foreign key field in `OrderItem`
3. `@Parent(reference = "id")` on `OrderItem.orderId` links back to `Order.id`
4. DataShepherd automatically writes both sheets with correct references

---

### Example 8: Streaming Mode for Large Files (1M+ Rows)

For enterprise-scale exports, DataShepherd uses Apache POI's `SXSSF` streaming API. Writes to temp files on disk,
keeping memory constant regardless of row count.

**📋 Use Case:** Data warehouse exports, bulk data migrations, regulatory reports

**Write Code with Streaming:**

```java
List<DataRecord> millionRecords = generateLargeDataset(1_000_000);

new

WriterService()
    .

xlsx()
    .

xlsxLarge() 
    .

writeToExcel(millionRecords, DataRecord .class)
    .

saveExcelTo("massive_export.xlsx");
```

**Performance Benchmarks:**

| Rows      | Memory Usage | Write Time |
|-----------|--------------|------------|
| 10,000    | ~50 MB       | ~0.5s      |
| 100,000   | ~50 MB       | ~2s        |
| 1,000,000 | ~50 MB       | ~5s        |

**Key Features:**

- **Constant Memory:** ~50MB regardless of row count
- **Same API:** Just add `.xlsxLarge()` - annotations work identically

---

## 📖 Reading Mode: Complete Examples

### Example 1: Basic Reading

Reading is symmetric with writing. DataShepherd maps Excel columns back to Java fields by matching `@ExcelColumn` names.

**📋 Use Case:** Import user uploads, parse templates, load configuration

**Read Code:**

```java
// Reads from "BasicData" sheet
List<User> users = new ReaderService()
                .xlsx("users.xlsx")
                .readFromExcel(User.class);
```

**How It Works:**

1. `@Sheet(name = "BasicData")` tells DataShepherd which sheet to read
2. Column headers are matched case-insensitively with `@ExcelColumn(name = ...)`
3. Data types are automatically converted

**Supported Data Types:**

- Primitives: `int`, `long`, `double`, `float`, `boolean`
- Wrappers: `Integer`, `Long`, `Double`, `Float`, `Boolean`
- Strings: `String`
- Dates: `LocalDate`, `LocalDateTime`, `Date`
- Enums: Mapped by name

---

### Example 2: Reading with Parent-Child Relationships

When reading relational files, DataShepherd automatically rebuilds your object graph using an efficient O(N + M)
indexing strategy.

**📋 Use Case:** Import orders with line items, organizational charts, project hierarchies

**Read Code:**

```java
// Reads BOTH "Orders" and "OrderItems" sheets
// Automatically joins and populates Order.items lists
List<Order> orders = new ReaderService()
                .xlsx("orders.xlsx")
                .readFromExcel(Order.class);
```

**Performance:** O(N + M) where N = parents, M = children

- 10,000 parents with 100,000 children: ~1s

---

### Example 3: Read-Validate-Write Workflow (Unique Feature!)

**This is DataShepherd's killer feature for data import pipelines.**

Scenario: You receive a "clean" Excel file from users. You want to validate it, add error highlighting, and return it
for correction.

**Workflow Code:**

```java
// 1. Load the clean file
ReaderService readerService = new ReaderService()
                .xlsx("raw_data.xlsx");

// 2. Read and validate (triggers validators and marks workbook in memory)
List<ImportEntity> data = readerService.readFromExcel(ImportEntity.class);

// 3. Save the marked workbook back to Excel
readerService.

saveExcelTo("data_with_errors.xlsx");
```

**Result (data_with_errors.xlsx):**

- Green cells: valid data
- Red cells with comments: what needs fixing
- Yellow cells: warnings

---

## 🏗️ Architecture & Design

### Annotation Reference

#### Class-Level Annotations

| Annotation | Purpose                                 | Attributes                                                                            |
|------------|-----------------------------------------|---------------------------------------------------------------------------------------|
| `@Sheet`   | Defines Excel sheet name and properties | `name`, `headerRow`, `picture`, `skipHeader`, `endSheet`, `centerHeader/Footer`, etc. |

#### Field-Level Annotations

| Annotation                   | Purpose                     | Key Attributes                                    |
|------------------------------|-----------------------------|---------------------------------------------------|
| `@ExcelColumn`               | Maps field to column        | `name`, `position`, `headerStyle`, `format`       |
| `@ExcelStyle`                | Static cell styling         | `font`, `backgroundColor`, `horizontalAlignment`  |
| `@ConditionalExcelCellStyle` | Dynamic styling             | `colorCondition`, `backgroundColorCondition`      |
| `@ValidationStatus`          | Background color validation | `status` (Class<? extends DataStatusCondition>)   |
| `@ValidationComment`         | Excel cell comments         | `comment` (Class<? extends CellCommentCondition>) |
| `@Image`                     | Embeds image in cell        | `extension`, `width`, `height`                    |
| `@Child`                     | Parent-child relationship   | `mappedBy`, `referencedBy`                        |
| `@Parent`                    | Child-parent reference      | `reference`                                       |
| `@Cell`                      | Fixed cell positioning      | `row`, `column`, `format`                         |

---

## ⚡ Performance & Optimization

### Write Performance

| Dataset Size | Mode          | Memory  | Time |
|--------------|---------------|---------|------|
| 100K rows    | Standard      | ~200 MB | 2s   |
| 1M rows      | **Streaming** | ~50 MB  | 5s   |

### Optimization Tips

**For Writing:**

- **Use streaming for large files:** `.xlsxLarge()`
- **Style Caching:** DataShepherd handles this automatically via `ExcelStyleManager`.

**For Reading:**

- **Let DataShepherd handle relationships:** It uses an O(N+M) join which is much faster than manual nested loops.

---

## 📜 License

Licensed under **Apache License 2.0**.

---

## 📢 Show Your Support

If DataShepherd saved you time, give it a ⭐ on GitHub!

**Simple. Powerful. Productive.**

---

**Made with ❤️ by Gemini-IT. Happy coding! 🚀**

