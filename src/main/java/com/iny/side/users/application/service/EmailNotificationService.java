package com.iny.side.users.application.service;

import com.iny.side.users.domain.event.EmailVerificationRequestedEvent;

public interface EmailNotificationService {
    void handleVerificationRequest(EmailVerificationRequestedEvent event);
}
