package com.iny.side.assignment.exception;

import com.iny.side.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidAssignmentDueDateException extends BusinessException {
    public InvalidAssignmentDueDateException() {
        super("마감일은 30분 단위로만 설정할 수 있습니다.", HttpStatus.BAD_REQUEST);
    }
}
