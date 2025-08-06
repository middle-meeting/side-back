package com.iny.side.submission.web.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;

import java.util.Objects;

public enum SubmissionFilter {
    ALL,
    GRADED,
    NEEDS_GRADING,
    NOT_SUBMITTED;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    public static SubmissionFilter from(String submissionFilter) {
        try {
            return SubmissionFilter.valueOf(submissionFilter.toUpperCase());
        } catch (Exception e) {
            return ALL;
        }
    }

    public static SubmissionFilter from(SubmissionDetailVo submissionDetailVo) {
        if (!Objects.isNull(submissionDetailVo.score())) {
            return GRADED;
        } else if (!Objects.isNull(submissionDetailVo.submissionId())) {
            return NEEDS_GRADING;
        } else {
            return NOT_SUBMITTED;
        }
    }
}
