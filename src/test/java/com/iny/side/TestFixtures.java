package com.iny.side;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;

import java.time.LocalDateTime;

public class TestFixtures {
    public static Account professor(Long id) {
        return Account.builder()
                .id(id)
                .username("prof" + id + "@test.com")
                .password("pw" + id)
                .name("교수" + id)
                .role(Role.PROFESSOR)
                .school("테스트대학교")
                .major("컴퓨터공학과")
                .employeeId("P" + String.format("%04d", id))
                .emailVerified(true)
                .build();
    }

    public static Account student(Long id) {
        return Account.builder()
                .id(id)
                .username("student" + id + "@test.com")
                .password("pw" + id)
                .name("학생" + id)
                .role(Role.STUDENT)
                .school("테스트대학교")
                .major("컴퓨터공학과")
                .grade(3)
                .studentId("2021" + String.format("%04d", id))
                .emailVerified(true)
                .build();
    }

    public static Course course(Long id, Account professor) {
        return Course.builder()
                .id(id)
                .name("course" + id)
                .semester("2025-01")
                .account(professor)
                .build();
    }

    public static Account createStudent() {
        return student(1L);
    }

    public static Account createProfessor() {
        return professor(1L);
    }

    public static Course createCourse() {
        Account professor = createProfessor();
        return course(1L, professor);
    }

    public static Assignment createAssignment(Course course) {
        return Assignment.builder()
                .id(1L)
                .title("테스트 과제")
                .personaName("홍길동")
                .personaAge(25)
                .personaGender(GenderType.MALE)
                .personaSymptom("기침")
                .personaHistory("특이사항 없음")
                .personaPersonality("외향적")
                .personaDisease("감기")
                .objective("감기 진단 연습")
                .maxTurns(10)
                .dueDate(LocalDateTime.of(2025, 7, 15, 14, 0))
                .course(course)
                .build();
    }

    public static Enrollment createEnrollment(Course course, Account student) {
        return Enrollment.builder()
                .id(1L)
                .course(course)
                .account(student)
                .build();
    }
}
