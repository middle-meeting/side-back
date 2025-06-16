package com.iny.side.assignment.exception;

import com.iny.side.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidAssignmentDueDateException extends BusinessException {
    public InvalidAssignmentDueDateException() {
        super("error.invalid.due_date", HttpStatus.BAD_REQUEST);
    }
}
