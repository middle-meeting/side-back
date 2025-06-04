package com.iny.side.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {
    MALE("남"),
    FEMALE("여");

    private final String label;
}
