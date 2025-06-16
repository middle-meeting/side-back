package com.iny.side;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;

public class TestFixtures {
    public static Account professor(Long id) {
        return Account.builder()
                .id(id)
                .username("prof" + id + "@test.com")
                .password("pw" + id)
                .name("교수" + id)
                .role(Role.PROFESSOR)
                .build();
    }

    public static Account student(Long id) {
        return Account.builder()
                .id(id)
                .username("student" + id + "@test.com")
                .password("pw" + id)
                .name("학생" + id)
                .role(Role.STUDENT)
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
}
