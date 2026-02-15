package com.datashepherd.excel;

import java.util.ArrayList;
import java.util.List;

import com.datashepherd.excel.annotation.Cell;
import com.datashepherd.excel.annotation.Child;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Image;
import com.datashepherd.excel.annotation.Parent;
import com.datashepherd.excel.annotation.Sheet;
import com.datashepherd.excel.annotation.ValidationComment;
import com.datashepherd.excel.annotation.ValidationStatus;
import com.datashepherd.excel.enums.ImageType;

public class MockDataGenerator {

    public static List<ProfileEntity> generateProfileData(int count) {
        List<ProfileEntity> data = new ArrayList<>();
        byte[] image = loadImage("EXD434.jpg");
        for (int i = 1; i <= count; i++) {
            data.add(new ProfileEntity(i, "User " + i, image));
        }
        return data;
    }

    public static List<FormattedEntity> generateFormattedData(int count) {
        List<FormattedEntity> list = new ArrayList<>();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        for (int i = 1; i <= count; i++) {
            cal.set(1990 + i, 0, i);
            list.add(new FormattedEntity(i, cal.getTime(), 0.85 + (i * 0.01), 2500.0 + (i * 100)));
        }
        return list;
    }

    public static List<MultiStyledEntity> generateMultiStyledData(int count) {
        List<MultiStyledEntity> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new MultiStyledEntity(i, "Entity " + i, i % 2 == 0 ? "Active" : "Inactive"));
        }
        return list;
    }

    public static List<StyledEntity> generateStyledData(int count) {
        List<StyledEntity> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new StyledEntity(i, "Styled Entity " + i));
        }
        return list;
    }

    public static List<SimpleEntity> generateSimpleData(int count) {
        List<SimpleEntity> list = new ArrayList<>(Math.min(count, 10000));
        for (int i = 1; i <= count; i++) {
            list.add(new SimpleEntity(i, "Entity " + i));
            // To avoid memory issues in the generator itself for 1M records,
            // we might want a streaming approach or just a very simple list.
            // For now, let's keep it simple but be aware of heap size.
        }
        return list;
    }

    public static java.util.stream.Stream<SimpleEntity> streamSimpleData(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                                         .mapToObj(i -> new SimpleEntity(i, "Entity " + i));
    }

    public static List<EntityWithImage> generateImageData(int count) {
        List<EntityWithImage> list = new ArrayList<>();
        byte[] image = loadImage("EXD434.jpg");
        for (int i = 1; i <= count; i++) {
            list.add(new EntityWithImage(i, image));
        }
        return list;
    }

    public static List<ParentEntity> generateRelationalData(int parentCount, int childrenPerParent) {
        List<ParentEntity> parents = new ArrayList<>();
        for (int i = 1; i <= parentCount; i++) {
            List<ChildEntity> children = new ArrayList<>();
            for (int j = 1; j <= childrenPerParent; j++) {
                children.add(new ChildEntity(i * 100 + j, "Child " + j + " of Parent " + i, i));
            }
            parents.add(new ParentEntity(i, "Parent " + i, children));
        }
        return parents;
    }

    private static byte[] loadImage(String name) {
        try (var is = MockDataGenerator.class.getClassLoader().getResourceAsStream(name)) {
            if (is == null) {
                // Fallback to direct file access if stream is null (e.g. running from IDE vs Maven)
                java.nio.file.Path path = java.nio.file.Paths.get("src/test/resources", name);
                if (java.nio.file.Files.exists(path)) {
                    return java.nio.file.Files.readAllBytes(path);
                }
                throw new RuntimeException("Image not found: " + name);
            }
            return is.readAllBytes();
        }
        catch (java.io.IOException e) {
            throw new RuntimeException("Failed to load image: " + name, e);
        }
    }

    @Sheet(name = "Profile")
    public static class ProfileEntity {
        @ExcelColumn(name = "ID")
        private int id;

        @ExcelColumn(name = "Full Name")
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.WHITE, fontHeightInPoints = 12, fontStyle = com.datashepherd.excel.enums.FontStyle.BOLD),
                backgroundColor = com.datashepherd.excel.enums.Color.CORNFLOWER_BLUE,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER
        )
        private String fullName;

        @Image(extension = ImageType.PICTURE_TYPE_JPEG, width = 100, height = 100)
        @ExcelColumn(name = "Profile Photo")
        private byte[] profilePhoto;

        public ProfileEntity() {
        }

        public ProfileEntity(int id, String fullName, byte[] profilePhoto) {
            this.id = id;
            this.fullName = fullName;
            this.profilePhoto = profilePhoto;
        }

        public int getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public byte[] getProfilePhoto() {
            return profilePhoto;
        }
    }

    @Sheet(name = "Formatted")
    public static class FormattedEntity {
        @ExcelColumn(name = "ID")
        private int id;

        @ExcelColumn(name = "Birthday", format = com.datashepherd.excel.enums.DateFormat.ISO_DATE)
        private java.util.Date birthday;

        @ExcelColumn(name = "Success Rate", format = com.datashepherd.excel.enums.PercentageFormat.PERCENTAGE_WITH_DECIMALS)
        private double successRate;

        @ExcelColumn(name = "Salary", format = com.datashepherd.excel.enums.CurrencyFormat.EURO)
        private double salary;

        public FormattedEntity() {
        }

        public FormattedEntity(int id, java.util.Date birthday, double successRate, double salary) {
            this.id = id;
            this.birthday = birthday;
            this.successRate = successRate;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public java.util.Date getBirthday() {
            return birthday;
        }

        public double getSuccessRate() {
            return successRate;
        }

        public double getSalary() {
            return salary;
        }
    }

    @Sheet(name = "MultiStyled")
    public static class MultiStyledEntity {
        @ExcelColumn(name = "ID")
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.BLACK, fontHeightInPoints = 12, fontStyle = com.datashepherd.excel.enums.FontStyle.BOLD),
                backgroundColor = com.datashepherd.excel.enums.Color.YELLOW,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT
        )
        private int id;

        @ExcelColumn(name = "Name")
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.WHITE, fontHeightInPoints = 14, fontStyle = com.datashepherd.excel.enums.FontStyle.ITALIC),
                backgroundColor = com.datashepherd.excel.enums.Color.BLUE,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER
        )
        private String name;

        @ExcelColumn(name = "Status")
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.DARK_RED, fontHeightInPoints = 10, fontStyle = com.datashepherd.excel.enums.FontStyle.NORMAL),
                backgroundColor = com.datashepherd.excel.enums.Color.LIGHT_GREEN,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT
        )
        private String status;

        public MultiStyledEntity() {
        }

        public MultiStyledEntity(int id, String name, String status) {
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @Sheet(name = "Styled")
    public static class StyledEntity {
        @ExcelColumn(name = "ID")
        private int id;

        @ExcelColumn(name = "Name")
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.WHITE, fontHeightInPoints = 14, fontStyle = com.datashepherd.excel.enums.FontStyle.BOLD),
                backgroundColor = com.datashepherd.excel.enums.Color.BLUE,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER
        )
        private String name;

        public StyledEntity() {
        }

        public StyledEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Sheet(name = "Simple")
    public static class SimpleEntity {
        @ExcelColumn(name = "ID")
        private int id;
        @ExcelColumn(name = "Name")
        @ValidationComment(comment = TestCommentCondition.class)
        private String name;

        public SimpleEntity() {
        }

        public SimpleEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestCommentCondition implements com.datashepherd.excel.helper.writer.CellCommentCondition {
        @Override
        public <T> String applyCondition(T fieldValue) {
            return "Comment for " + fieldValue;
        }
    }

    @Sheet(name = "Cover")
    public static class CoverEntity {
        @Cell(row = 1, column = 1)
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.WHITE, fontHeightInPoints = 16, fontStyle = com.datashepherd.excel.enums.FontStyle.BOLD),
                backgroundColor = com.datashepherd.excel.enums.Color.DARK_BLUE,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER
        )
        private final String title = "Data Report";

        @Cell(row = 3, column = 1)
        @com.datashepherd.excel.annotation.style.ExcelStyle(
                font = @com.datashepherd.excel.annotation.style.Font(color = com.datashepherd.excel.enums.Color.BLACK, fontHeightInPoints = 12, fontStyle = com.datashepherd.excel.enums.FontStyle.ITALIC),
                backgroundColor = com.datashepherd.excel.enums.Color.LIGHT_YELLOW,
                horizontalAlignment = org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT
        )
        private final String description = "This report contains generated data for testing purposes.";

        @Image(extension = ImageType.PICTURE_TYPE_JPEG, width = 200, height = 200)
        @Cell(row = 5, column = 1)
        private final byte[] coverImage;

        public CoverEntity() {
            this.coverImage = loadImage("EXD434.jpg");
        }
    }

    @Sheet(name = "Images")
    public static class EntityWithImage {
        @ExcelColumn(name = "ID")
        private final int id;
        @Image(extension = ImageType.PICTURE_TYPE_JPEG, width = 100, height = 100)
        @ExcelColumn(name = "Photo")
        private final byte[] photo;

        public EntityWithImage(int id, byte[] photo) {
            this.id = id;
            this.photo = photo;
        }
    }

    @Sheet(name = "Parent")
    public static class ParentEntity {
        @ExcelColumn(name = "Parent ID")
        private int id;
        @ExcelColumn(name = "Parent Name")
        private String name;
        @Child(mappedBy = ChildEntity.class, referencedBy = "parentId")
        private List<ChildEntity> children;

        public ParentEntity() {
        }

        public ParentEntity(int id, String name, List<ChildEntity> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildEntity> getChildren() {
            return children;
        }

        public void setChildren(List<ChildEntity> children) {
            this.children = children;
        }
    }

    @Sheet(name = "Child")
    public static class ChildEntity {
        @ExcelColumn(name = "Child ID")
        private int id;
        @ExcelColumn(name = "Child Name")
        private String name;
        @Parent(reference = "id")
        @ExcelColumn(name = "Parent ID Ref")
        private int parentId;

        public ChildEntity() {
        }

        public ChildEntity(int id, String name, int parentId) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
    }

    @Sheet(name = "InvalidData")
    public static class InvalidDataEntity {
        @ExcelColumn(name = "ID")
        private int id;
        @ExcelColumn(name = "Value")
        @ValidationComment(comment = InvalidValueCondition.class)
        @ValidationStatus(status = InvalidValueStatusCondition.class)
        private String value;

        public InvalidDataEntity() {
        }

        public InvalidDataEntity(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class InvalidValueCondition implements com.datashepherd.excel.helper.writer.CellCommentCondition {
        @Override
        public <T> String applyCondition(T fieldValue) {
            if (fieldValue != null && fieldValue.toString().contains("Invalid")) {
                return "This value is invalid!";
            }
            return null;
        }
    }

    public static class InvalidValueStatusCondition implements com.datashepherd.excel.helper.writer.style.condional.DataStatusCondition {
        @Override
        public <T> com.datashepherd.excel.enums.Status applyCondition(T fieldValue) {
            if (fieldValue != null && fieldValue.toString().contains("Invalid")) {
                return com.datashepherd.excel.enums.Status.ERROR;
            }
            return com.datashepherd.excel.enums.Status.SUCCESS;
        }
    }

    @Sheet(name = "ImportData")
    public static class CleanImportEntity {
        @ExcelColumn(name = "Email")
        private String email;

        public CleanImportEntity() {
        }

        public CleanImportEntity(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Sheet(name = "ImportData")
    public static class ImportEntity {
        @ExcelColumn(name = "Email")
        @ValidationStatus(status = EmailValidator.class)
        @ValidationComment(comment = EmailErrorComment.class)
        private String email;

        public ImportEntity() {
        }

        public ImportEntity(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class EmailValidator implements com.datashepherd.excel.helper.writer.style.condional.DataStatusCondition {
        @Override
        public <T> com.datashepherd.excel.enums.Status applyCondition(T fieldValue) {
            String email = (String) fieldValue;
            return (email != null && email.contains("@")) ? com.datashepherd.excel.enums.Status.SUCCESS : com.datashepherd.excel.enums.Status.ERROR;
        }
    }

    public static class EmailErrorComment implements com.datashepherd.excel.helper.writer.CellCommentCondition {
        @Override
        public <T> String applyCondition(T fieldValue) {
            String email = (String) fieldValue;
            return (email != null && email.contains("@")) ? "" : "Invalid email format";
        }
    }
}
