/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.order;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockDataGenerator {
    private static final Faker faker = new Faker();
    private static final List<String> courseNames = List.of("Math", "Science", "English", "History", "Geography");

    public static List<Student> generateSimpleStudentList(int contentNumber) {
        List<Student> students = new ArrayList<>();
        IntStream.range(0,contentNumber).forEach(i-> students.add(new Student(i,faker.name().fullName(),faker.internet().emailAddress(),faker.number().numberBetween(18, 40),faker.address().fullAddress(),faker.phoneNumber().phoneNumber())));
        return students;
    }

    public static List<Student> generateRelationalShipStudentList(int contentNumber) {
        return generateSimpleStudentList(contentNumber).stream().peek(student->{
            List<Course> courses = new ArrayList<>();
            courseNames.forEach(courseName-> courses.add(new Course(courseName,faker.number().numberBetween(50, 100),student.getIdStudent(),faker.lorem().sentence(), LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365)), LocalDate.now().plusDays(faker.number().numberBetween(0, 365)),Double.valueOf(faker.commerce().price()),faker.number().randomDouble(3,1, 5),faker.number().numberBetween(1, 10))));
            student.setCourses(courses);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Profile profile() {
        try (InputStream inputStream = com.datashepherd.entity.MockDataGenerator.class.getClassLoader().getResourceAsStream("profile.png")){
            return new Profile(faker.phoneNumber().phoneNumber(),faker.address().fullAddress(),faker.name().fullName(), Objects.requireNonNull(inputStream).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
