package com.iny.side.assignment.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentResponseDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AssignmentServiceTest {

    private AssignmentService assignmentService;
    private FakeAssignmentRepository fakeAssignmentRepository;
    private FakeCourseRepository fakeCourseRepository;
    private FakeUserRepository fakeUserRepository;

    private Account professor;
    private Account otherProfessor;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        fakeAssignmentRepository = new FakeAssignmentRepository();
        fakeCourseRepository = new FakeCourseRepository();
        fakeUserRepository = new FakeUserRepository();

        assignmentService = AssignmentServiceImpl.builder()
                .assignmentRepository(fakeAssignmentRepository)
                .courseRepository(fakeCourseRepository)
                .build();

        // 교수 1, 2 생성
        professor = Account.builder()
                .username("prof1@test.com")
                .password("pw1")
                .name("교수1")
                .role(Role.PROFESSOR)
                .build();
        professor = fakeUserRepository.save(professor);

        otherProfessor = Account.builder()
                .username("prof2@test.com")
                .password("pw2")
                .name("교수2")
                .role(Role.PROFESSOR)
                .build();
        otherProfessor = fakeUserRepository.save(otherProfessor);

        // testCourse 생성 (교수1 소유)
        testCourse = Course.builder()
                .name("과제강의")
                .semester("2025-01")
                .account(professor)
                .build();
        testCourse = fakeCourseRepository.save(testCourse);

        // 과제 2개 생성 (testCourse 소속)
        Assignment assignment1 = Assignment.builder()
                .title("심장질환 케이스")
                .personaName("김환자")
                .personaAge(22)
                .personaGender(GenderType.FEMALE)
                .personaSymptom("흉통")
                .personaHistory("고혈압")
                .personaPersonality("침착함")
                .personaDisease("심근경색")
                .objective("심근경색 조기 진단 교육")
                .maxTurns(10)
                .dueDate(LocalDateTime.of(2025, 7, 10, 14, 0))
                .account(professor)
                .course(testCourse)
                .build();

        Assignment assignment2 = Assignment.builder()
                .title("소화기 질환")
                .personaName("박환자")
                .personaAge(35)
                .personaGender(GenderType.MALE)
                .personaSymptom("복부통증")
                .personaHistory("과거 위염")
                .personaPersonality("걱정이 많음")
                .personaDisease("위염")
                .objective("생활습관 개선 교육")
                .maxTurns(5)
                .dueDate(LocalDateTime.of(2025, 7, 12, 18, 0))
                .account(professor)
                .course(testCourse)
                .build();

        fakeAssignmentRepository.save(assignment1);
        fakeAssignmentRepository.save(assignment2);
    }

    @Test
    void 교수는_본인_강의의_과제_여러개_정상조회() {
        // when
        List<AssignmentResponseDto> result = assignmentService.findAssignmentsByCourseAndProfessor(testCourse.getId(), professor.getId());

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(AssignmentResponseDto::title)
                .containsExactlyInAnyOrder("심장질환 케이스", "소화기 질환");
    }

    @Test
    void 과제_생성_정상() {
        // given
        AssignmentCreateDto createDto = new AssignmentCreateDto(
                "심부전 케이스",
                "최환자",
                65,
                GenderType.FEMALE,
                "호흡곤란",
                "고혈압",
                "내성적",
                "심부전",
                "심부전 진단 교육",
                12,
                LocalDateTime.of(2025, 7, 15, 18, 0)
        );

        // when
        AssignmentResponseDto result = assignmentService.create(testCourse.getId(), professor.getId(), createDto);

        // then
        assertThat(result.title()).isEqualTo("심부전 케이스");
        assertThat(fakeAssignmentRepository.findByCourseId(testCourse.getId()))
                .extracting(Assignment::getTitle)
                .contains("심부전 케이스");
    }

    @Test
    void 과제_생성_권한없음() {
        AssignmentCreateDto createDto = new AssignmentCreateDto(
                "심부전 케이스",
                "최환자",
                65,
                GenderType.FEMALE,
                "호흡곤란",
                "고혈압",
                "내성적",
                "심부전",
                "심부전 진단 교육",
                12,
                LocalDateTime.of(2025, 7, 15, 18, 0)
        );
        assertThatThrownBy(() ->
                assignmentService.create(testCourse.getId(), otherProfessor.getId(), createDto)
        ).isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("본인의 강의가 아닙니다");
    }

    @Test
    void 과제_생성_강의없음() {
        AssignmentCreateDto createDto = new AssignmentCreateDto(
                "심부전 케이스",
                "최환자",
                65,
                GenderType.FEMALE,
                "호흡곤란",
                "고혈압",
                "내성적",
                "심부전",
                "심부전 진단 교육",
                12,
                LocalDateTime.of(2025, 7, 15, 18, 0)
        );
        assertThatThrownBy(() ->
                assignmentService.create(99999L, professor.getId(), createDto)
        ).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course", 99999);
    }
}