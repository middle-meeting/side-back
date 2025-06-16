package com.iny.side.common;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;

public class TestDataFactory {
    public static Account createAccount(long id, String username, Role role) {
        return Account.builder()
                .id(id)
                .username(username)
                .password("123123123")
                .name(role == Role.PROFESSOR ? "교수" + id : "사용자" + id)
                .role(role)
                .build();
    }

    public static Course createCourse(String name, String semester, Account professor) {
        return Course.builder()
                .name(name)
                .semester(semester)
                .account(professor)
                .build();
    }

    public static com.iny.side.assignment.domain.entity.Assignment createAssignment(String title, Account professor, Course course, java.time.LocalDateTime dueDate) {
        return com.iny.side.assignment.domain.entity.Assignment.builder()
                .title(title)
                .personaName("테스트환자")
                .personaAge(20)
                .personaGender(com.iny.side.common.domain.GenderType.MALE)
                .personaSymptom("증상")
                .personaHistory("병력")
                .personaPersonality("성격")
                .personaDisease("질병")
                .objective("목표")
                .maxTurns(10)
                .dueDate(dueDate)
                .account(professor)
                .course(course)
                .build();
    }
}
