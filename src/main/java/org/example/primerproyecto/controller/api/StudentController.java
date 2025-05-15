package org.example.primerproyecto.controller.api;

import org.example.primerproyecto.dto.CourseDTO;
import org.example.primerproyecto.dto.EnrollmentDTO;
import org.example.primerproyecto.dto.StudentDTO;
import org.example.primerproyecto.service.CourseService;
import org.example.primerproyecto.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getStudentsByCourse(@PathVariable Long courseId) {
        var students = studentService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students);
    }

    @PostMapping()
    public ResponseEntity<StudentDTO> registerStudent(@RequestBody StudentDTO studentDTO) {
        var student = studentService.createStudent(studentDTO);
        return ResponseEntity.status(201).body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {

        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);

    }

    @GetMapping("/program/{program}")
    public ResponseEntity<List<StudentDTO>> getStudentsByProgram(@PathVariable String program) {
        var students = studentService.getByProgram(program);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/sorted-paged")
    public ResponseEntity<?> getStudentsPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page) {
        try {
            Page<StudentDTO> students = studentService.findAllSortedByName(page);

            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error retrieving paginated students: " + e.getMessage());
        }
    }






}
