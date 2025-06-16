package com.iny.side.course.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.web.dto.EnrolledCoursesDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;

    private Account professor;
    private Account student;

    @BeforeEach
    void setUp() {

        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeCourseRepository fakeCourseRepository = new FakeCourseRepository();
        FakeEnrollmentRepository fakeEnrollmentRepository = new FakeEnrollmentRepository();

        courseService = CourseServiceImpl.builder()
                .courseRepository(fakeCourseRepository)
                .enrollmentRepository(fakeEnrollmentRepository)
                .build();

        student = Account.builder()
                .id(1L)
                .username("test@test.com")
                .password("123123123")
                .name("김학생")
                .role(Role.STUDENT)
                .build();
        professor = Account.builder()
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
        fakeUserRepository.save(student);
        fakeUserRepository.save(professor);
        fakeUserRepository.save(왕선생);
        fakeUserRepository.save(박관리);

        Course 신경학 = Course.builder()
                .name("신경학")
                .semester("2024-02")
                .account(professor)
                .build();
        Course 한의학입문 = Course.builder()
                .name("한의학 입문")
                .semester("2025-01")
                .account(professor)
                .build();
        Course 인체학입문 = Course.builder()
                .name("인체학 입문")
                .semester("2025-01")
                .account(professor)
                .build();
        Course 소프트웨어공학입문 = Course.builder()
                .name("소프트웨어공학 입문")
                .semester("2025-01")
                .account(왕선생)
                .build();

        fakeCourseRepository.save(신경학);
        fakeCourseRepository.save(한의학입문);
        fakeCourseRepository.save(인체학입문);
        fakeCourseRepository.save(소프트웨어공학입문);

        Enrollment testEnrollment = Enrollment.builder()
                .id(1L)
                .account(student)
                .course(인체학입문)
                .build();

        fakeEnrollmentRepository.save(testEnrollment);
    }

    @Test
    void 교수는_해당학기에_본인이_가르치는_과목을_조회_가능() {
        // when
        List<ProfessorCoursesDto> result = courseService.getAll(professor.getId(), "2025-01");

        // then
        // 1. 조회된 과목 수
        assertThat(result).hasSize(2);
        // 2. 모든 과목이 해당 교수의 것인지
        assertThat(result)
                .allMatch(course -> course.professorName().equals("이교수"));
        // 3. 모든 과목이 해당 학기인지
        assertThat(result)
                .allMatch(course -> course.semester().equals("2025-01"));
        // 4. 과목명이 기대값에 포함되는지
        assertThat(result)
                .extracting(ProfessorCoursesDto::name)
                .containsExactlyInAnyOrder("한의학 입문", "인체학 입문");
    }

    @Test
    void 학생은_해당학기에_본인이_수강하는_과목_조회_가능() {
        // when
        List<EnrolledCoursesDto> result = courseService.getAllEnrolled(student.getId(), "2025-01");

        // then
        assertThat(result).hasSize(1);
        assertThat(result).allMatch(enrolledCourse -> enrolledCourse.professorName().equals("이교수"));
        assertThat(result).allMatch(enrolledCourse -> enrolledCourse.semester().equals("2025-01"));
        assertThat(result)
                .extracting(EnrolledCoursesDto::name)
                .containsExactlyInAnyOrder("인체학 입문");
    }
}