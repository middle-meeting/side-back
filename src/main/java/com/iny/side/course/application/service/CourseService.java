package com.iny.side.course.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.course.web.dto.EnrolledCoursesDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;

import java.util.List;

public interface CourseService {
    List<ProfessorCoursesDto> getAll(Long professorId, String semester);

    List<EnrolledCoursesDto> getAllEnrolled(Long studentId, String semester);

    SliceResponse<EnrolledCoursesDto> getAllEnrolled(Long studentId, String semester, int page);
}
