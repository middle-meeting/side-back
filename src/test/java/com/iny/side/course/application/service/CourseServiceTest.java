package com.iny.side.course.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.web.dto.MyCoursesDto;
import com.iny.side.common.TestDataFactory;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.web.dto.AccountResponseDto;
import org.assertj.core.api.Assertions;
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

        Account 김학생 = TestDataFactory.createAccount(1L, "test@test.com", Role.STUDENT);
        Account 이교수 = TestDataFactory.createAccount(2L, "test1@test.com", Role.PROFESSOR);
        Account 왕선생 = TestDataFactory.createAccount(3L, "test2@test.com", Role.PROFESSOR);
        Account 박관리 = TestDataFactory.createAccount(4L, "test3@test.com", Role.ADMIN);
        fakeUserRepository.save(김학생);
        fakeUserRepository.save(이교수);
        fakeUserRepository.save(왕선생);
        fakeUserRepository.save(박관리);

        Course 신경학 = TestDataFactory.createCourse("신경학", "202402", 이교수);
        Course 한의학입문 = TestDataFactory.createCourse("한의학 입문", "202501", 이교수);
        Course 인체학입문 = TestDataFactory.createCourse("인체학 입문", "202501", 이교수);
        Course 소프트웨어공학입문 = TestDataFactory.createCourse("소프트웨어공학 입문", "202501", 왕선생);

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
        List<MyCoursesDto> result = courseService.findMyCourse(dto, semester);

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
                .extracting(MyCoursesDto::name)
                .containsExactlyInAnyOrder("한의학 입문", "인체학 입문");
    }
}