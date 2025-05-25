package com.iny.side.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class BasicResponse<T> {
    @Builder.Default
    private HttpStatus status = HttpStatus.OK;
    @Builder.Default
    private Integer code = HttpStatus.OK.value();
    private String message;
    private Integer count;
    private List<T> data;

    public static <T> BasicResponse<T> ok(List<T> data) {
        return BasicResponse.<T>builder()
                .message("성공")
                .count(data.size())
                .data(data)
                .build();
    }
}
