package com.iny.side.course.domain.entity;

import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.users.domain.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Account account;

    @Column(name = "total_students")
    private Integer totalStudents;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "classroom")
    private String classroom;

    @Column(name = "department")
    private String department;

    @Builder
    public Course(Long id, String name, String semester, String description, Account account, Integer totalStudents, String schedule, String classroom, String department) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.description = description;
        this.account = account;
        this.totalStudents = totalStudents;
        this.schedule = schedule;
        this.classroom = classroom;
        this.department = department;
    }

    public void validateOwner(Long professorId) {
        if (!this.account.getId().equals(professorId)) {
            throw new ForbiddenException("forbidden.not_your_course");
        }
    }
}
