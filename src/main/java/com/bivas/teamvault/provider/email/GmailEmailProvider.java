package com.bivas.teamvault.provider.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("Gmail")
public class GmailEmailProvider implements EmailProvider {
    private final JavaMailSender mailSender;

    @Override
    public void SendInviteEmail(String inviteLink, String recipientEmail, String teamName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("TeamVault - You are invited to join team " + teamName);
        message.setText("Hello,\n\n"
                + teamName + " has invited you to join their team!" + ".\n\n"
                + "Click here to accept: " + inviteLink + "\n\n"
                + "Regards, TeamVault");

        mailSender.send(message);
    }
}
