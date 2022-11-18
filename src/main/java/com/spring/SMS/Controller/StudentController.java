package com.spring.SMS.Controller;

import com.spring.SMS.Entity.Student;
import com.spring.SMS.Service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        //creating empty object to hold student form data
        Student student = new Student();
        model.addAttribute("student", student);
        model.addAttribute("error", new Student());
        return "create_student";
    }

    @PostMapping("/students")
    //model attribute to directly bind form data to the object(student to student entity)
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        Student error = validateStudent(student);
        if (error == null) {
            studentService.saveStudent(student);
            return "redirect:/students";
        }
        model.addAttribute("error", error);
        model.addAttribute("student", student);
        return "create_student";
    }

    private Student validateStudent(Student student) {
        Student error = null;
        // trim: agadi pachadi space chha vane hataucha, eg: "a " - > "a"
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            error = new Student();
            error.setFirstName("First name required");
        }

        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            if (error == null)
                error = new Student();
            error.setLastName("Last name required");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            if (error == null)
                error = new Student();
            error.setEmail("Email required");
        }
        return error;
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student student, Model model) {
        //getting student fro database by id
        Student existingStudent = studentService.getStudentById(id);
        existingStudent.setId(id);
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        if (!student.getEmail().equals(existingStudent.getEmail()))
            existingStudent.setVerified(false);
        existingStudent.setEmail(student.getEmail());

        //saving updated student object
        studentService.updateStudent(existingStudent);
        return "redirect:/students";
    }

    //delete handler
    @GetMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";

    }

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    @GetMapping("/students/verify/{id}")
    public String home(@PathVariable("id") Long id, @RequestParam("token") String token, Model model) {
        if (studentService.verify(id, token))
            model.addAttribute("verified", true);
        else model.addAttribute("verified", false);
        return "verify";
    }
}
