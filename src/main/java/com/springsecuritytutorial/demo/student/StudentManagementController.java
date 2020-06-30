package com.springsecuritytutorial.demo.student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Tester1"),
            new Student(2, "Tester2"),
            new Student(3, "Tester3")
    );

    @GetMapping
    public List<Student> getAllStudents() {
        System.out.print("Get all students: ");
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        System.out.print("Register new Student: ");
        System.out.println(student);
    }

    @DeleteMapping(path="/{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.print("Delete student: ");
        System.out.println(studentId);
    }

    @PutMapping(path="/{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Integer studentId,
            @RequestBody Student student) {

        System.out.print("Update student: ");
        System.out.println(String.format("%s %s", studentId, student));
    }
}
