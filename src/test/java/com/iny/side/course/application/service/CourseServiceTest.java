package com.iny.side.course.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.course.web.dto.EnrolledCoursesDetailDto;
import com.iny.side.course.web.dto.EnrolledCoursesSimpleDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseServiceTest {

    private CourseService courseService;
    private FakeEnrollmentRepository fakeEnrollmentRepository;
    private FakeCourseRepository fakeCourseRepository;

    private Account professor;
    private Account student;
    private Course 인체학입문;

    @BeforeEach
    void setUp() {

        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        fakeCourseRepository = new FakeCourseRepository();
        fakeEnrollmentRepository = new FakeEnrollmentRepository();
        FakeEnrollmentValidationService fakeEnrollmentValidationService = new FakeEnrollmentValidationService(fakeEnrollmentRepository, fakeCourseRepository);

        courseService = CourseServiceImpl.builder()
                .courseRepository(fakeCourseRepository)
                .enrollmentRepository(fakeEnrollmentRepository)
                .enrollmentValidationService(fakeEnrollmentValidationService)
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
        인체학입문 = Course.builder()
                .name("인체학 입문")
                .semester("2025-01")
                .description("인체의 구조와 기능에 대해 학습합니다.")
                .account(professor)
                .build();
        Course 소프트웨어공학입문 = Course.builder()
                .name("소프트웨어공학 입문")
                .semester("2025-01")
                .account(왕선생)
                .build();

        fakeCourseRepository.save(신경학);
        fakeCourseRepository.save(한의학입문);
        인체학입문 = fakeCourseRepository.save(인체학입문);
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
        List<ProfessorCoursesDto> result = courseService.getAll(professor.getId(), "2025-01", 0).getContent();

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
        List<EnrolledCoursesSimpleDto> result = courseService.getAllEnrolled(student.getId(), "2025-01");

        // then
        assertThat(result).hasSize(1);
        assertThat(result).allMatch(enrolledCourse -> enrolledCourse.professorName().equals("이교수"));
        assertThat(result).allMatch(enrolledCourse -> enrolledCourse.semester().equals("2025-01"));
        assertThat(result)
                .extracting(EnrolledCoursesSimpleDto::name)
                .containsExactlyInAnyOrder("인체학 입문");
    }

    @Test
    void 학생은_해당학기에_본인이_수강하는_과목을_페이징하여_조회_가능() {
        // when
        SliceResponse<EnrolledCoursesSimpleDto> result = courseService.getAllEnrolled(student.getId(), "2025-01", 0);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(12);
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isTrue();
        assertThat(result.getContent()).allMatch(enrolledCourse -> enrolledCourse.professorName().equals("이교수"));
        assertThat(result.getContent()).allMatch(enrolledCourse -> enrolledCourse.semester().equals("2025-01"));
        assertThat(result.getContent())
                .extracting(EnrolledCoursesSimpleDto::name)
                .containsExactlyInAnyOrder("인체학 입문");
    }

    @Test
    void 학생은_수강중인_과목의_상세정보를_조회할_수_있다() {
        // when
        EnrolledCoursesDetailDto result = courseService.getEnrolled(student.getId(), 인체학입문.getId());

        // then
        assertThat(result.id()).isEqualTo(인체학입문.getId());
        assertThat(result.name()).isEqualTo("인체학 입문");
        assertThat(result.semester()).isEqualTo("2025-01");
        assertThat(result.description()).isEqualTo("인체의 구조와 기능에 대해 학습합니다.");
        assertThat(result.professorName()).isEqualTo("이교수");
    }

    @Test
    void 수강하지_않은_과목의_상세정보_조회시_예외_발생() {
        // given
        Course 다른과목 = Course.builder()
                .name("다른 과목")
                .semester("2025-01")
                .account(professor)
                .build();
        Course savedCourse = fakeCourseRepository.save(다른과목);

        // when & then
        assertThatThrownBy(() -> courseService.getEnrolled(student.getId(), savedCourse.getId()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 존재하지_않는_과목의_상세정보_조회시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> courseService.getEnrolled(student.getId(), 999L))
                .isInstanceOf(NotFoundException.class);
    }
}