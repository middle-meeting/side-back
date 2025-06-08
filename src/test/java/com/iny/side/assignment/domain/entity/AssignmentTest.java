package com.iny.side.assignment.domain.entity;

import com.iny.side.assignment.exception.InvalidAssignmentDueDateException;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AssignmentTest {

    @Test
    void 과제_생성_정상() {
        // given
        Account professor = Account.builder().id(1L).build();
        Course course = Course.builder().id(10L).account(professor).build();
        AssignmentCreateDto dto = new AssignmentCreateDto(
                "테스트 과제", "홍길동", 23, GenderType.FEMALE,
                "기침", "특이사항 없음", "외향적", "감기",
                "감기 케이스 학습", 10,
                LocalDateTime.of(2025, 7, 15, 14, 0)
        );

        // when
        Assignment assignment = Assignment.create(course, dto);

        // then
        assertThat(assignment.getTitle()).isEqualTo("테스트 과제");
        assertThat(assignment.getDueDate().getMinute()).isEqualTo(0);
        assertThat(assignment.getCourse()).isEqualTo(course);
        assertThat(assignment.getCourse().getAccount()).isEqualTo(professor);
    }

    @Test
    void 마감일이_30분_단위가_아니면_과제_생성_불가() {
        // given
        Account professor = Account.builder().id(1L).build();
        Course course = Course.builder().id(10L).account(professor).build();
        AssignmentCreateDto dto = new AssignmentCreateDto(
                "테스트 과제", "홍길동", 23, GenderType.FEMALE,
                "기침", "특이사항 없음", "외향적", "감기",
                "감기 케이스 학습", 10,
                LocalDateTime.of(2025, 7, 15, 14, 17) // 30분 단위 아님!
        );

        // when & then
        assertThatThrownBy(() -> Assignment.create(course, dto))
                .isInstanceOf(InvalidAssignmentDueDateException.class)
                .hasMessageContaining("마감일은 30분 단위로만 설정할 수 있습니다");
    }

    @Test
    void 마감일이_30분_단위라도_초_이하_단위가_0이_아니면_과제_생성_불가() {
        // given
        Account professor = Account.builder().id(1L).build();
        Course course = Course.builder().id(10L).account(professor).build();
        AssignmentCreateDto dto = new AssignmentCreateDto(
                "테스트 과제", "홍길동", 23, GenderType.FEMALE,
                "기침", "특이사항 없음", "외향적", "감기",
                "감기 케이스 학습", 10,
                LocalDateTime.of(2025, 7, 15, 14, 30, 3, 1) // 30분 단위지만 초/나노초 있음!
        );

        // when & then
        assertThatThrownBy(() -> Assignment.create(course, dto))
                .isInstanceOf(InvalidAssignmentDueDateException.class);
    }
}