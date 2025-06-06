package com.iny.side.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDetail {
    private final String code;
    private final String field; // validationError 시 입력
    private final String message;
}
