package com.iny.side.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicResponse<T> {
    private final HttpStatus status;
    private final Integer code;
    private final String message;
    private final T data;
    private final ErrorPayload error;

    public static <T> BasicResponse<T> ok(T data) {
        return ok(data, "성공");
    }

    public static <T> BasicResponse<T> ok(T data, String message) {
        return new BasicResponse<>(HttpStatus.OK, HttpStatus.OK.value(), message, data, null);
    }

    public static <T> BasicResponse<T> error(HttpStatus status, ErrorPayload error) {
        return error(status, "오류가 발생했습니다.", error);
    }

    public static <T> BasicResponse<T> error(HttpStatus status, String message, ErrorPayload error) {
        return new BasicResponse<>(status, status.value(), message, null, error);
    }
}