package com.spring.SMS.Service;

import com.spring.SMS.Entity.Student;
import com.spring.SMS.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        student = studentRepository.save(student);
        verifyEmail(student);
        return student;
    }

    private void verifyEmail(Student student) {
        student.setToken(UUID.randomUUID().toString());
        String url = "http://localhost:8080/students/verify/" + student.getId() + "?token=" + student.getToken();
        //email pathune
        System.out.println(url);
        studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get(); //optional return garne vayera .get haleko
    }

    @Override
    public Student updateStudent(Student student) {
        if (!student.getVerified()) {
            verifyEmail(student);
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public boolean verify(Long id, String token) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (student.getToken().equals(token)) {
                student.setVerified(true);
                studentRepository.save(student);
                return true;
            }
        }
        return false;
    }


}
