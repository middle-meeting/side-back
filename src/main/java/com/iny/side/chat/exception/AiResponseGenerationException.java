package com.iny.side.chat.exception;

import com.iny.side.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AiResponseGenerationException extends BusinessException {

    public AiResponseGenerationException() {
        super("error.ai_response_generation_failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public AiResponseGenerationException(String detailMessage) {
        super("error.ai_response_generation_failed", HttpStatus.INTERNAL_SERVER_ERROR, detailMessage);
    }
}
