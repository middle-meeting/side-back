package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Course;

public record MyCoursesDto(Long id, String name, String semester, String professorName) {
    public static MyCoursesDto from(Course course) {
        return new MyCoursesDto(
                course.getId(),
                course.getName(),
                course.getSemester(),
                course.getAccount().getName()
        );
    }
}
