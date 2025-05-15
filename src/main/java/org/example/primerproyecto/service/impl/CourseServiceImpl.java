package org.example.primerproyecto.service.impl;

import org.example.primerproyecto.dto.CourseDTO;
import org.example.primerproyecto.dto.CourseWithStudentCountDTO;
import org.example.primerproyecto.entity.Course;
import org.example.primerproyecto.entity.Enrollment;
import org.example.primerproyecto.entity.Professor;
import org.example.primerproyecto.entity.Student;
import org.example.primerproyecto.mapper.CourseMapper;
import org.example.primerproyecto.repository.CourseRepository;
import org.example.primerproyecto.repository.EnrollmentRepository;
import org.example.primerproyecto.repository.ProfessorRepository;
import org.example.primerproyecto.repository.StudentRepository;
import org.example.primerproyecto.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseDTO createCourse(CourseDTO course) {
        var entity = courseMapper.toEntity(course);
        if (courseRepository.findByName(course.getName()).isEmpty()) {
            Course courseEntity = courseRepository.save(entity);
            return courseMapper.toDTO(courseEntity);
        }else{
            throw new RuntimeException("Course already exists with name " + course.getName());
        }

    }

    @Override
    public CourseDTO createCoursewithTeacher(CourseDTO courseDTO) {
        // Validate that professor exists
        Professor professor = professorRepository.findProfessorById(courseDTO.getProfessorId());

        // Create course entity
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setProfessor(professor);

        // Save course
        Course savedCourse = courseRepository.save(course);

        // Convert to DTO and return
        return courseMapper.toDTO(savedCourse);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(entity -> courseMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCoursesPratica() {
        return courseRepository.findAll().stream().map(entity -> courseMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> listCourseOfStudent(long id) {
        var enrollments = enrollmentRepository.findByStudent_Id(id);
        return enrollments.stream().map(enrollment -> courseMapper.toDTO(enrollment.getCourse())).toList();
    }

    @Override
    public CourseDTO getCourseById(long id) {
       var entity =  courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found with id " + id));
       return courseMapper.toDTO(entity);
    }

    @Override
    public void deleteCourse(long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Course not found with id " + id);
        }
    }

    @Override
    public List<Student> listStudentsOfCourse(Course course) {
        var enrollments = enrollmentRepository.findAll();
        return enrollments.stream().filter(enrollment -> enrollment.getCourse().getId() == course.getId())
                .map(enrollment -> enrollment.getStudent()).toList();
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<CourseDTO> getStudentCourses(Long studentId) {
        // Verify student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student with ID " + studentId + " not found"));

        // Get enrollments for this student
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_Id(studentId);

        // Map the courses to DTOs
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .map(course -> {
                    CourseDTO dto = new CourseDTO();
                    dto.setId(course.getId());
                    dto.setName(course.getName());
                    if (course.getProfessor() != null) {
                        dto.setProfessorId(course.getProfessor().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseWithStudentCountDTO> getCoursesWithStudentCount() {

        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {
            CourseWithStudentCountDTO dto = new CourseWithStudentCountDTO();
            dto.setId(course.getId());
            dto.setName(course.getName());

            if (course.getProfessor() != null) {
                dto.setProfessorId(course.getProfessor().getId());
                dto.setProfessorName(course.getProfessor().getName());
            }

            // Count students enrolled in this course
            int studentCount = enrollmentRepository.findByCourseId(course.getId()).size();
            dto.setStudentCount(studentCount);

            return dto;
        }).collect(Collectors.toList());
    }

    @Value("${app.pagination.size}")
    private int pageSize;

    @Override
    public Page<CourseDTO> findByNameContaining(String name, int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        Page<Course> coursePage = courseRepository.findByNameContaining(name, pageable);
        return coursePage.map(course -> courseMapper.toDTO(course));
    }
}
