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
import com.iny.side.common.TestDataFactory;
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

        professorAccount1 = TestDataFactory.createAccount(1L, "test1@test.com", Role.PROFESSOR);
        professorAccount1 = fakeUserRepository.save(professorAccount1);

        testCourse1 = TestDataFactory.createCourse("이교수의 케이스스터디", "202501", professorAccount1);
        testCourse1 = fakeCourseRepository.save(testCourse1);

        Assignment testAssignment1 = TestDataFactory.createAssignment(
                "첫번째과제", professorAccount1, testCourse1, LocalDateTime.of(2025, 6, 30, 23, 0)
        );

        Assignment testAssignment2 = TestDataFactory.createAssignment(
                "두번째과제", professorAccount1, testCourse1, LocalDateTime.of(2025, 6, 29, 18, 0)
        );

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