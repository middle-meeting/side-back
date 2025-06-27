package com.iny.side.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceResponse<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final boolean hasNext;
    private final boolean hasPrevious;
    private final boolean first;
    private final boolean last;

    public static <T> SliceResponse<T> from(Slice<T> slice) {
        return new SliceResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                slice.hasPrevious(),
                slice.isFirst(),
                slice.isLast()
        );
    }

    public static <T> SliceResponse<T> of(List<T> content, int page, int size, boolean hasNext) {
        return new SliceResponse<>(
                content,
                page,
                size,
                hasNext,
                page > 0,
                page == 0,
                !hasNext
        );
    }
}
