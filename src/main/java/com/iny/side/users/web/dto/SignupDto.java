package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.web.validation.ValidSignup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidSignup
public record SignupDto(
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String username,

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "이름이 입력되지 않았습니다.")
        @Size(min = 2, message = "이름이 올바르지 않습니다.")
        String name,

        @NotNull(message = "역할이 선택되지 않았습니다.")
        Role role,

        @NotBlank(message = "학교가 입력되지 않았습니다.")
        String school,

        @NotBlank(message = "전공이 입력되지 않았습니다.")
        String major,

        // 학생용 필드
        Integer grade,
        String studentId,

        // 교수용 필드
        String employeeId,

        @NotBlank(message = "인증번호가 입력되지 않았습니다.")
        String verificationCode
) {
    public static SignupDto from(Account account) {
        return new SignupDto(
                account.getUsername(),
                account.getPassword(),
                account.getName(),
                account.getRole(),
                account.getSchool(),
                account.getMajor(),
                account.getGrade(),
                account.getStudentId(),
                account.getEmployeeId(),
                null // verificationCode는 응답에 포함하지 않음
        );
    }

    public Account toAccount(String cryptedPassword) {
        return Account.builder()
                .username(this.username())
                .password(cryptedPassword)
                .name(this.name())
                .role(this.role())
                .school(this.school())
                .major(this.major())
                .grade(this.grade())
                .studentId(this.studentId())
                .employeeId(this.employeeId())
                .emailVerified(false)
                .build();
    }
}
