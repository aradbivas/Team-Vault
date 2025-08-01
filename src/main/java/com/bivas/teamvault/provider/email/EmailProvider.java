package com.bivas.teamvault.provider.email;

public interface EmailProvider {

    void SendInviteEmail(String inviteLink, String recipientEmail, String teamName);
}
