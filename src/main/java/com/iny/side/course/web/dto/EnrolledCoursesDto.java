package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Enrollment;

public record EnrolledCoursesDto(Long id, String name, String semester, String professorName) {
    public static EnrolledCoursesDto from(Enrollment enrollment) {
        return new EnrolledCoursesDto(
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getCourse().getSemester(),
                enrollment.getCourse().getAccount().getName()
        );
    }
}
