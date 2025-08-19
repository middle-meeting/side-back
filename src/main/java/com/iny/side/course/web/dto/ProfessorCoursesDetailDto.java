package com.iny.side.course.web.dto;

import com.iny.side.course.domain.entity.Course;

public record ProfessorCoursesDetailDto(Long id, String name, String semester, String description, String professorName, Integer totalStudents, String schedule, String classroom, String department) {
    public static ProfessorCoursesDetailDto from(Course course){
        return new ProfessorCoursesDetailDto(
                course.getId(),
                course.getName(),
                course.getSemester(),
                course.getDescription(),
                course.getAccount().getName(),
                course.getTotalStudents(),
                course.getSchedule(),
                course.getClassroom(),
                course.getDepartment()
        );
    }
}
