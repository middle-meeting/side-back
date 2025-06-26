package com.iny.side.users.domain.entity;

import com.iny.side.users.domain.Role;
import com.iny.side.users.web.dto.SignupDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name = "major", nullable = false)
    private String major;

    // 학생용 필드
    @Column(name = "grade")
    private Integer grade;

    @Column(name = "student_id")
    private String studentId;

    // 교수용 필드
    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Builder
    public Account(Long id, String username, String password, String name, Role role,
                   String school, String major, Integer grade, String studentId,
                   String employeeId, Boolean emailVerified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.studentId = studentId;
        this.employeeId = employeeId;
        this.emailVerified = emailVerified != null ? emailVerified : false;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
