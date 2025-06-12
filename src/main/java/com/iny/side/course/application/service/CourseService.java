package com.iny.side.course.application.service;

import com.iny.side.course.web.dto.EnrolledCoursesDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;

import java.util.List;

public interface CourseService {
    List<ProfessorCoursesDto> findProfessorCourses(Long professorId, String semester);

    List<EnrolledCoursesDto> findEnrolledCourses(Long studentId, String semester);
}
