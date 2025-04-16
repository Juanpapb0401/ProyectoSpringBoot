package org.example.primerproyecto.service;

import org.example.primerproyecto.dto.CourseDTO;
import org.example.primerproyecto.entity.Course;
import org.example.primerproyecto.entity.Student;

import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> listCourseOfStudent(long id);

    CourseDTO getCourseById(long id);

    void deleteCourse(long id);

    List<Student> listStudentsOfCourse(Course course);

    List<Course> findAll();
}
