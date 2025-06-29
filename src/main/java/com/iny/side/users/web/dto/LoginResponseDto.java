package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;

public record LoginResponseDto(
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
    
    public static LoginResponseDto from(Account account) {
        return new LoginResponseDto(
                account.getId(),
                account.getUsername(),
                account.getName(),
                account.getRole(),
                account.getSchool(),
                account.getMajor(),
                account.getGrade(),
                account.getStudentId(),
                account.getEmployeeId(),
                "로그인이 완료되었습니다."
        );
    }
    
    public static LoginResponseDto from(AccountDto accountDto) {
        return new LoginResponseDto(
                accountDto.id(),
                accountDto.username(),
                accountDto.name(),
                accountDto.role(),
                accountDto.school(),
                accountDto.major(),
                accountDto.grade(),
                accountDto.studentId(),
                accountDto.employeeId(),
                "로그인이 완료되었습니다."
        );
    }

    public static LoginResponseDto from(AccountResponseDto accountResponseDto) {
        return new LoginResponseDto(
                accountResponseDto.id(),
                accountResponseDto.username(),
                accountResponseDto.name(),
                accountResponseDto.role(),
                accountResponseDto.school(),
                accountResponseDto.major(),
                accountResponseDto.grade(),
                accountResponseDto.studentId(),
                accountResponseDto.employeeId(),
                "로그인이 완료되었습니다."
        );
    }


}
