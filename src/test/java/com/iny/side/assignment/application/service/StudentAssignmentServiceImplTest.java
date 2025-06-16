package com.iny.side.assignment.application.service;

import com.iny.side.account.mock.FakeUserRepository;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.TestFixtures;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentAssignmentServiceImplTest {

    private StudentAssignmentService studentAssignmentService;

    private Account student;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        FakeEnrollmentRepository fakeEnrollmentRepository = new FakeEnrollmentRepository();
        FakeAssignmentRepository fakeAssignmentRepository = new FakeAssignmentRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeCourseRepository fakeCourseRepository = new FakeCourseRepository();

        studentAssignmentService = StudentAssignmentServiceImpl.builder()
                .enrollmentRepository(fakeEnrollmentRepository)
                .assignmentRepository(fakeAssignmentRepository)
                .build();

        // 교수1, 학생1 생성
        Account professor = TestFixtures.professor(1L);
        student = TestFixtures.student(1L);
        fakeUserRepository.save(professor);
        student = fakeUserRepository.save(student);

        // testCourse 생성 (교수1 소유)
        testCourse = fakeCourseRepository.save(TestFixtures.course(1L, professor));

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
        fakeAssignmentRepository.save(assignment1);
        fakeAssignmentRepository.save(assignment2);

        // 수강신청1
        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .course(testCourse)
                .account(student)
                .build();
        fakeEnrollmentRepository.save(enrollment);
    }

    @Test
    void 학생이_수강하는_강의의_과제_정상조회() {
        // when
        List<AssignmentSimpleResponseDto> result = studentAssignmentService.getAll(testCourse.getId(), student.getId());

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(AssignmentSimpleResponseDto::title)
                .containsExactlyInAnyOrder("심장질환 케이스", "소화기 질환");
    }

    @Test
    void 학생이_수강하지_않는_강의의_과제는_조회_불가() {
        assertThatThrownBy(() ->
                studentAssignmentService.getAll(999999L, student.getId())
        ).isInstanceOf(ForbiddenException.class);
    }
}