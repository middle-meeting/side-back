package com.iny.side.course.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.web.dto.ProfessorCoursesDto;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.web.dto.AccountResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;

    @BeforeEach
    void setUp() {

        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeCourseRepository fakeCourseRepository = new FakeCourseRepository();

        courseService = CourseServiceImpl.builder()
                .courseRepository(fakeCourseRepository)
                .build();

        Account 김학생 = Account.builder()
                .id(1L)
                .username("test@test.com")
                .password("123123123")
                .name("김학생")
                .role(Role.STUDENT)
                .build();
        Account 이교수 = Account.builder()
                .id(2L)
                .username("test1@test.com")
                .password("123123123")
                .name("이교수")
                .role(Role.PROFESSOR)
                .build();
        Account 왕선생 = Account.builder()
                .id(3L)
                .username("test2@test.com")
                .password("123123123")
                .name("왕선생")
                .role(Role.PROFESSOR)
                .build();
        Account 박관리 = Account.builder()
                .id(4L)
                .username("test3@test.com")
                .password("123123123")
                .name("박관리")
                .role(Role.ADMIN)
                .build();
        fakeUserRepository.save(김학생);
        fakeUserRepository.save(이교수);
        fakeUserRepository.save(왕선생);
        fakeUserRepository.save(박관리);

        Course 신경학 = Course.builder()
                .name("신경학")
                .semester("202402")
                .account(이교수)
                .build();
        Course 한의학입문 = Course.builder()
                .name("한의학 입문")
                .semester("202501")
                .account(이교수)
                .build();
        Course 인체학입문 = Course.builder()
                .name("인체학 입문")
                .semester("202501")
                .account(이교수)
                .build();
        Course 소프트웨어공학입문 = Course.builder()
                .name("소프트웨어공학 입문")
                .semester("202501")
                .account(왕선생)
                .build();

        fakeCourseRepository.save(신경학);
        fakeCourseRepository.save(한의학입문);
        fakeCourseRepository.save(인체학입문);
        fakeCourseRepository.save(소프트웨어공학입문);
    }

    @Test
    void 교수는_해당학기에_본인이_가르치는_과목을_조회_가능() {
        // given
        AccountResponseDto dto = new AccountResponseDto(2L, "test1@test.com", "이교수", Role.PROFESSOR);
        String semester = "202501";

        // when
        List<ProfessorCoursesDto> result = courseService.findProfessorCourses(dto, semester);

        // then
        // 1. 조회된 과목 수
        assertThat(result).hasSize(2);
        // 2. 모든 과목이 해당 교수의 것인지
        assertThat(result)
                .allMatch(course -> course.professorName().equals(dto.name()));
        // 3. 모든 과목이 해당 학기인지
        assertThat(result)
                .allMatch(course -> course.semester().equals(semester));
        // 4. 과목명이 기대값에 포함되는지
        assertThat(result)
                .extracting(ProfessorCoursesDto::name)
                .containsExactlyInAnyOrder("한의학 입문", "인체학 입문");
    }
}