package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Enrollment;

public record EnrolledCoursesSimpleDto(Long id, String name, String semester, String professorName) {
    public static EnrolledCoursesSimpleDto from(Enrollment enrollment) {
        return new EnrolledCoursesSimpleDto(
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getCourse().getSemester(),
                enrollment.getCourse().getAccount().getName()
        );
    }
}
