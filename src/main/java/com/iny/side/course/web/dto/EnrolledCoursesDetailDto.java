package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Enrollment;

public record EnrolledCoursesDetailDto(Long id, String name, String semester, String description, String professorName) {
    public static EnrolledCoursesDetailDto from(Enrollment enrollment) {
        return new EnrolledCoursesDetailDto(
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getCourse().getSemester(),
                enrollment.getCourse().getDescription(),
                enrollment.getCourse().getAccount().getName()
        );
    }
}
