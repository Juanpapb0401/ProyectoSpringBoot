package org.example.primerproyecto.service;

import org.example.primerproyecto.dto.CourseDTO;
import org.example.primerproyecto.dto.CourseWithStudentCountDTO;
import org.example.primerproyecto.entity.Course;
import org.example.primerproyecto.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    CourseDTO createCoursewithTeacher(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> listCourseOfStudent(long id);

    CourseDTO getCourseById(long id);

    void deleteCourse(long id);

    List<Student> listStudentsOfCourse(Course course);

    List<Course> findAll();

    List<CourseDTO> getStudentCourses(Long studentId);

    List<CourseWithStudentCountDTO> getCoursesWithStudentCount();

    Page<CourseDTO> findByNameContaining(String name, int page);

    List<CourseDTO> getAllCoursesPratica();

}
