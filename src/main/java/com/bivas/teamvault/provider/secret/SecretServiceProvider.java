package com.bivas.teamvault.provider.secret;

public interface SecretServiceProvider {

    public String GetSecret(String key);

    public String SaveSecret(String key, String value);

    public void DeleteSecret(String key);
}
