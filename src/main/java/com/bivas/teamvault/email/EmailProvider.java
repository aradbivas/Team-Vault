package com.bivas.teamvault.email;

public interface EmailProvider {

    void SendInviteEmail(String inviteLink, String recipientEmail, String teamName);
}
