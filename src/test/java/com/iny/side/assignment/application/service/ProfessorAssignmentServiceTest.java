package com.iny.side.assignment.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.users.mock.FakeUserRepository;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.ProfessorAssignmentDetailResponseDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfessorAssignmentServiceTest {

    private ProfessorAssignmentService professorAssignmentService;
    private FakeAssignmentRepository fakeAssignmentRepository;
    private FakeCourseRepository fakeCourseRepository;
    private FakeUserRepository fakeUserRepository;

    private Account professor;
    private Account otherProfessor;
    private Course testCourse;
    private Assignment testAssignment;

    @BeforeEach
    void setUp() {
        fakeAssignmentRepository = new FakeAssignmentRepository();
        fakeCourseRepository = new FakeCourseRepository();
        fakeUserRepository = new FakeUserRepository();

        FakeEnrollmentValidationService fakeEnrollmentValidationService = new FakeEnrollmentValidationService(null, fakeCourseRepository);

        professorAssignmentService = ProfessorAssignmentServiceImpl.builder()
                .assignmentRepository(fakeAssignmentRepository)
                .enrollmentValidationService(fakeEnrollmentValidationService)
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
                .course(testCourse)
                .build();

        testAssignment = Assignment.builder()
                .title("상세보기용 과제")
                .personaName("김환자")
                .personaAge(28)
                .personaGender(GenderType.FEMALE)
                .personaSymptom("두통")
                .personaHistory("특이사항 없음")
                .personaPersonality("내성적")
                .personaDisease("긴장성 두통")
                .objective("두통 감별법 학습")
                .maxTurns(8)
                .dueDate(LocalDateTime.of(2025, 7, 25, 15, 0))
                .course(testCourse)
                .build();

        fakeAssignmentRepository.save(assignment1);
        fakeAssignmentRepository.save(assignment2);
        testAssignment = fakeAssignmentRepository.save(testAssignment);
    }

    @Test
    void 교수는_본인_강의의_과제_여러개_정상조회() {
        // when
        SliceResponse<AssignmentSimpleResponseDto> result = professorAssignmentService.getAll(testCourse.getId(), professor.getId(), 0);

        // then
        assertThat(result.getContent())
                .hasSize(3)
                .extracting(AssignmentSimpleResponseDto::title)
                .containsExactlyInAnyOrder("심장질환 케이스", "소화기 질환", "상세보기용 과제");
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
        AssignmentSimpleResponseDto result = professorAssignmentService.create(testCourse.getId(), professor.getId(), createDto);

        // then
        assertThat(result.title()).isEqualTo("심부전 케이스");
        assertThat(fakeAssignmentRepository.findAllByCourseId(testCourse.getId()))
                .extracting(Assignment::getTitle)
                .contains("심부전 케이스");
    }

    @Test
    void 교수는_타교수강의에는_과제_생성_불가() {
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
                professorAssignmentService.create(testCourse.getId(), otherProfessor.getId(), createDto)
        ).isInstanceOf(ForbiddenException.class)
        ;
    }

    @Test
    void 존재하지_않는_강의에는_과제_생성_불가() {
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
                professorAssignmentService.create(99999L, professor.getId(), createDto)
        ).isInstanceOf(NotFoundException.class)
        ;
    }

    @Test
    void 교수_본인_강의의_과제_상세조회_정상_조회() {
        // given
        // when
        ProfessorAssignmentDetailResponseDto detail = professorAssignmentService.get(
                testCourse.getId(), professor.getId(), testAssignment.getId());

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.title()).isEqualTo("상세보기용 과제");
        assertThat(detail.personaName()).isEqualTo("김환자");
        assertThat(detail.dueDate()).isEqualTo(LocalDateTime.of(2025, 7, 25, 15, 0));
    }

    @Test
    void 교수는_타교수_강의의_과제_상세조회_불가() {
        // when & then
        assertThatThrownBy(() ->
                professorAssignmentService.get(testCourse.getId(), otherProfessor.getId(), testAssignment.getId())
        ).isInstanceOf(ForbiddenException.class)
        ;
    }

    @Test
    void 존재하지_않는_강의에는_상세조회_불가() {
        assertThatThrownBy(() ->
                professorAssignmentService.get(99999L, professor.getId(), testAssignment.getId())
        ).isInstanceOf(NotFoundException.class)
        ;
    }

    @Test
    void 존재하지_않는_과제는_상세조회_불가() {
        assertThatThrownBy(() ->
                professorAssignmentService.get(testCourse.getId(), professor.getId(), 88888L)
        ).isInstanceOf(NotFoundException.class)
        ;
    }

    @Test
    void 강의에_속하지_않는_과제는_상세조회_불가() {
        Course otherCourse = Course.builder()
                .name("다른 강의")
                .semester("2025-01")
                .account(professor)
                .build();
        Course savedCourse = fakeCourseRepository.save(otherCourse);

        assertThatThrownBy(() ->
                professorAssignmentService.get(savedCourse.getId(), professor.getId(), testAssignment.getId())
        ).isInstanceOf(ForbiddenException.class)
        ;
    }

    @Test
    void 과제_삭제_정상() {
        // given
        // when
        professorAssignmentService.delete(testCourse.getId(), professor.getId(), testAssignment.getId());
        // then
        assertThat(fakeAssignmentRepository.findByAssignmentId(testAssignment.getId())).isEmpty();
    }

    @Test
    void 존재하지_않는_강의는_삭제_불가() {
        assertThatThrownBy(() ->
                professorAssignmentService.delete(99999L, professor.getId(), testAssignment.getId())
        ).isInstanceOf(NotFoundException.class)
        ;
    }

    @Test
    void 존재하지_않는_과제는_삭제_불가() {
        assertThatThrownBy(() ->
                professorAssignmentService.delete(testCourse.getId(), professor.getId(), 88888L)
        ).isInstanceOf(NotFoundException.class)
        ;
    }

    @Test
    void 교수는_타교수_강의의_과제_삭제_불가() {
        assertThatThrownBy(() ->
                professorAssignmentService.delete(testCourse.getId(), otherProfessor.getId(), testAssignment.getId())
        ).isInstanceOf(ForbiddenException.class)
        ;
    }

    @Test
    void 강의에_속하지_않는_과제는_삭제_불가() {
        Course otherCourse = Course.builder()
                .name("다른 강의")
                .semester("2025-01")
                .account(professor)
                .build();
        Course savedCourse = fakeCourseRepository.save(otherCourse);

        assertThatThrownBy(() ->
                professorAssignmentService.delete(savedCourse.getId(), professor.getId(), testAssignment.getId())
        ).isInstanceOf(ForbiddenException.class)
        ;
    }
}