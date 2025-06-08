package com.iny.side.course.application.service;

import com.iny.side.course.web.dto.ProfessorCoursesDto;
import com.iny.side.users.web.dto.AccountResponseDto;

import java.util.List;

public interface CourseService {
    List<ProfessorCoursesDto> findProfessorCourses(AccountResponseDto accountResponseDto, String semester);
}
