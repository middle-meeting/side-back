package com.iny.side.users.domain.event;

public record EmailVerificationRequestedEvent(
        String email,
        String verificationCode
) {
}
