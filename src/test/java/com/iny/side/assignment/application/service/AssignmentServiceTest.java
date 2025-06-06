package com.iny.side.assignment.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentResponseDto;
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

    private Account professorAccount1;
    private Course testCourse1;

    @BeforeEach
    void setUp() {

        fakeAssignmentRepository = new FakeAssignmentRepository();
        fakeCourseRepository = new FakeCourseRepository();
        fakeUserRepository = new FakeUserRepository();

        assignmentService = AssignmentServiceImpl.builder()
                .assignmentRepository(fakeAssignmentRepository)
                .courseRepository(fakeCourseRepository)
                .build();

        professorAccount1 = Account.builder()
                .username("test1@test.com")
                .password("123123123")
                .name("이교수")
                .role(Role.PROFESSOR)
                .build();
        professorAccount1 = fakeUserRepository.save(professorAccount1);

        testCourse1 = Course.builder()
                .name("이교수의 케이스스터디")
                .semester("202501")
                .account(professorAccount1)
                .build();
        testCourse1 = fakeCourseRepository.save(testCourse1);

        Assignment testAssignment1 = Assignment.builder()
                .title("첫번째과제")
                .personaName("김환자")
                .personaAge(18)
                .personaGender(GenderType.MALE)
                .personaSymptom("가슴 통증")
                .personaHistory("과거 심장질환 병력 있음")
                .personaPersonality("침착함")
                .personaDisease("고혈압")
                .objective("심장 질환 감별 및 대처법 교육")
                .maxTurns(10)
                .dueDate(LocalDateTime.of(2025, 6, 30, 23, 0))
                .account(professorAccount1)
                .course(testCourse1)
                .build();

        Assignment testAssignment2 = Assignment.builder()
                .title("두번째과제")
                .personaName("박환자")
                .personaAge(35)
                .personaGender(GenderType.FEMALE)
                .personaSymptom("복부 통증과 메스꺼움")
                .personaHistory("과거 위염 진단 경험")
                .personaPersonality("걱정이 많음")
                .personaDisease("위염")
                .objective("소화기 질환 초기진단 및 생활습관 개선 교육")
                .maxTurns(20)
                .dueDate(LocalDateTime.of(2025, 6, 29, 18, 0))
                .account(professorAccount1)
                .course(testCourse1)
                .build();

        fakeAssignmentRepository.save(testAssignment1);
        fakeAssignmentRepository.save(testAssignment2);
    }

    @Test
    void 내_강의에_과제가_여러개_있으면_모두_정상_조회() {
        // given
        // when
        List<AssignmentResponseDto> assignments = assignmentService.findAssignmentsByCourseAndProfessor(testCourse1.getId(), professorAccount1.getId());

        // then
        assertThat(assignments)
                .hasSize(2)
                .extracting(
                        AssignmentResponseDto::title,
                        AssignmentResponseDto::dueDate,
                        AssignmentResponseDto::creatorName
                )
                .containsExactlyInAnyOrder(
                        tuple("첫번째과제", LocalDateTime.of(2025, 6, 30, 23, 0), "이교수"),
                        tuple("두번째과제", LocalDateTime.of(2025, 6, 29, 18, 0), "이교수")
                );
    }
}