package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;

public record SignupResponseDto(
    Long id,
    String username,
    String name,
    Role role,
    String school,
    String major,
    Integer grade,
    String studentId,
    String employeeId,
    String message
) {
    
    public static SignupResponseDto from(Account account) {
        return new SignupResponseDto(
            account.getId(),
            account.getUsername(),
            account.getName(),
            account.getRole(),
            account.getSchool(),
            account.getMajor(),
            account.getGrade(),
            account.getStudentId(),
            account.getEmployeeId(),
            "회원가입이 완료되었습니다."
        );
    }
}
