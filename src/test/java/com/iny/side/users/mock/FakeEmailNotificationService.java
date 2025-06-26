package com.iny.side.users.mock;

import com.iny.side.users.application.service.EmailNotificationService;
import com.iny.side.users.domain.event.EmailVerificationRequestedEvent;

import java.util.ArrayList;
import java.util.List;

public class FakeEmailNotificationService implements EmailNotificationService {
    
    private final List<EmailVerificationRequestedEvent> handledEvents = new ArrayList<>();
    private final FakeEmailSender fakeEmailSender;

    public FakeEmailNotificationService(FakeEmailSender fakeEmailSender) {
        this.fakeEmailSender = fakeEmailSender;
    }

    @Override
    public void handleVerificationRequest(EmailVerificationRequestedEvent event) {
        handledEvents.add(event);
        fakeEmailSender.sendVerificationEmail(event.email(), event.verificationCode());
    }

    public List<EmailVerificationRequestedEvent> getHandledEvents() {
        return new ArrayList<>(handledEvents);
    }

    public void clear() {
        handledEvents.clear();
        fakeEmailSender.clear();
    }

    public boolean wasEventHandled(String email) {
        return handledEvents.stream()
                .anyMatch(event -> event.email().equals(email));
    }

    public int getEventCount() {
        return handledEvents.size();
    }
}
