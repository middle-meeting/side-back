package com.iny.side.course.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.course.web.dto.EnrolledCoursesDetailDto;
import com.iny.side.course.web.dto.EnrolledCoursesSimpleDto;
import com.iny.side.course.web.dto.ProfessorCoursesDetailDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;

import java.util.List;

public interface CourseService {
    SliceResponse<ProfessorCoursesDto> getAll(Long professorId, String semester, int page);

    List<EnrolledCoursesSimpleDto> getAllEnrolled(Long studentId, String semester);

    SliceResponse<EnrolledCoursesSimpleDto> getAllEnrolled(Long studentId, String semester, int page);

    EnrolledCoursesDetailDto getEnrolled(Long studentId, Long courseId);

    ProfessorCoursesDetailDto getCourse(Long id, Long courseId);
}
