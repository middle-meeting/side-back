package com.iny.side.submission.domain.entity;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.submission.exception.InvalidSubmissionDataException;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SubmissionTest {

    private Submission submission;
    private Account student;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        student = Account.builder().id(1L).build();
        assignment = Assignment.builder().id(1L).build();
        
        submission = Submission.builder()
                .student(student)
                .assignment(assignment)
                .build();
    }

    @Test
    @DisplayName("제출 시 주진단과 최종판단이 모두 있으면 정상 제출")
    void submit_정상_제출() {
        // given
        String primaryDiagnosis = "감기";
        String subDiagnosis = "인후염";
        String finalJudgment = "충분한 휴식과 수분 섭취를 권장합니다.";

        // when
        submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment);

        // then
        assertThat(submission.getPrimaryDiagnosis()).isEqualTo(primaryDiagnosis);
        assertThat(submission.getSubDiagnosis()).isEqualTo(subDiagnosis);
        assertThat(submission.getFinalJudgment()).isEqualTo(finalJudgment);
        assertThat(submission.getStatus()).isEqualTo(Submission.SubmissionStatus.SUBMITTED);
        assertThat(submission.getSubmittedAt()).isNotNull();
    }

    @Test
    @DisplayName("제출 시 subDiagnosis가 없어도 정상 제출")
    void submit_subDiagnosis_없어도_정상_제출() {
        // given
        String primaryDiagnosis = "감기";
        String subDiagnosis = null;
        String finalJudgment = "충분한 휴식과 수분 섭취를 권장합니다.";

        // when
        submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment);

        // then
        assertThat(submission.getPrimaryDiagnosis()).isEqualTo(primaryDiagnosis);
        assertThat(submission.getSubDiagnosis()).isNull();
        assertThat(submission.getFinalJudgment()).isEqualTo(finalJudgment);
        assertThat(submission.getStatus()).isEqualTo(Submission.SubmissionStatus.SUBMITTED);
    }

    @Test
    @DisplayName("제출 시 주진단이 null이면 예외 발생")
    void submit_주진단_null_예외() {
        // given
        String primaryDiagnosis = null;
        String subDiagnosis = "인후염";
        String finalJudgment = "충분한 휴식과 수분 섭취를 권장합니다.";

        // when & then
        assertThatThrownBy(() -> submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment))
                .isInstanceOf(InvalidSubmissionDataException.class);
    }

    @Test
    @DisplayName("제출 시 주진단이 빈 문자열이면 예외 발생")
    void submit_주진단_빈문자열_예외() {
        // given
        String primaryDiagnosis = "   ";
        String subDiagnosis = "인후염";
        String finalJudgment = "충분한 휴식과 수분 섭취를 권장합니다.";

        // when & then
        assertThatThrownBy(() -> submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment))
                .isInstanceOf(InvalidSubmissionDataException.class);
    }

    @Test
    @DisplayName("제출 시 최종판단이 null이면 예외 발생")
    void submit_최종판단_null_예외() {
        // given
        String primaryDiagnosis = "감기";
        String subDiagnosis = "인후염";
        String finalJudgment = null;

        // when & then
        assertThatThrownBy(() -> submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment))
                .isInstanceOf(InvalidSubmissionDataException.class);
    }

    @Test
    @DisplayName("제출 시 최종판단이 빈 문자열이면 예외 발생")
    void submit_최종판단_빈문자열_예외() {
        // given
        String primaryDiagnosis = "감기";
        String subDiagnosis = "인후염";
        String finalJudgment = "";

        // when & then
        assertThatThrownBy(() -> submission.submit(primaryDiagnosis, subDiagnosis, finalJudgment))
                .isInstanceOf(InvalidSubmissionDataException.class);
    }
}
