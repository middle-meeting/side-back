package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Course;

public record ProfessorCoursesDto(Long id, String name, String semester, String professorName) {
    public static ProfessorCoursesDto from(Course course) {
        return new ProfessorCoursesDto(
                course.getId(),
                course.getName(),
                course.getSemester(),
                course.getAccount().getName()
        );
    }
}
