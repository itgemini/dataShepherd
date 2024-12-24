/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.entity;

import com.github.javafaker.Faker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class MockDataGenerator {

    private static final Faker faker = new Faker();
    private static final String[] courseNames = {"Math", "Science", "English", "History", "Geography"};

    public static List<Student> generateStudents(int numberOfStudents) throws IOException {
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        InputStream inputStream = MockDataGenerator.class.getClassLoader().getResourceAsStream("profile.png");
        byte[] bytes = toByteArray(Objects.requireNonNull(inputStream));

        for (int i = 1; i <= numberOfStudents; i++) {
            // Create courses
            for (String courseName : courseNames) {
                Course course = new Course();
                course.setName(courseName);
                course.setScore(faker.number().numberBetween(50, 100)); // Random score between 50 and 100
                course.setIdStudent(i);
                courses.add(course);
                course.setDescription(faker.lorem().sentence());
                course.setStartDate(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365)));
                course.setEndDate(LocalDate.now().plusDays(faker.number().numberBetween(0, 365)));
                course.setPrice(Double.valueOf(faker.commerce().price()));
                course.setLevel(faker.number().randomDouble(3,1, 5));
                course.setOrder(faker.number().numberBetween(1, 10));
            }

            // Create a student
            Student student = new Student();
            student.setIdStudent(i);
            student.setEmail(faker.internet().emailAddress());
            student.setAddress(faker.address().fullAddress());
            student.setPhoneNumber(faker.phoneNumber().phoneNumber());
            student.setAge(faker.number().numberBetween(18, 40)); // Random age between 18 and 40
            student.setName(faker.name().fullName());
            student.setCourses(new HashSet<>(courses)); // Assign the courses to the student
            student.setPhoto(bytes);
            students.add(student);

            courses.clear(); // Clear the courses list for the next student
        }
        //inputStream.close();
        return students;
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}