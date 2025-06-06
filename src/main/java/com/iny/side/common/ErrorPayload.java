package com.iny.side.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorPayload {
    private final List<ErrorDetail> errors;
}

