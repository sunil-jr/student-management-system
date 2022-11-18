package com.spring.SMS.Service;

import com.spring.SMS.Entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student saveStudent(Student student);

    Student getStudentById(Long id);

    Student updateStudent(Student student);

    void deleteStudentById(Long id);

    boolean verify(Long id, String token);
}
