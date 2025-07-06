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

    @Builder
    public Course(Long id, String name, String semester, String description, Account account) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.description = description;
        this.account = account;
    }

    public void validateOwner(Long professorId) {
        if (!this.account.getId().equals(professorId)) {
            throw new ForbiddenException("forbidden.not_your_course");
        }
    }
}
