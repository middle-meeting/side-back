package com.iny.side.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorPayload {
    private final List<ErrorDetail> errors;
}

