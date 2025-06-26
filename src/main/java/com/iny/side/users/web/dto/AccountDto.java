package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import lombok.Builder;

@Builder
public record AccountDto(Long id, String username, String password, String name, Role role,
                        String school, String major, Integer grade, String studentId,
                        String employeeId, Boolean emailVerified) {
    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .name(account.getName())
                .role(account.getRole())
                .school(account.getSchool())
                .major(account.getMajor())
                .grade(account.getGrade())
                .studentId(account.getStudentId())
                .employeeId(account.getEmployeeId())
                .emailVerified(account.getEmailVerified())
                .build();
    }
}
