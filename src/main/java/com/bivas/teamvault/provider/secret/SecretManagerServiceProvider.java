package com.bivas.teamvault.provider.secret;

import com.bivas.teamvault.properties.SecretManagerProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.security.SecureRandom;

@Component("SecretManager")
public class SecretManagerServiceProvider implements SecretServiceProvider {

    private final SecretsManagerClient secretsClient;
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_+=.@-";
    private static final SecureRandom random = new SecureRandom();

    public SecretManagerServiceProvider(SecretManagerProperties secretManagerProperties) {

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(secretManagerProperties.getAccessKeyId(), secretManagerProperties.getSecretAccessKey());
        secretsClient = SecretsManagerClient
                .builder()
                .region(Region.of(secretManagerProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @Override
    public String GetSecret(String key) {

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(key).build();

        GetSecretValueResponse getSecretValueResponse = secretsClient.getSecretValue(getSecretValueRequest);

        return getSecretValueResponse.secretString();
    }

    @Override
    public String SaveSecret(String key, String value) {

        String secretName = createSecretName(key);

        CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().secretString(value).name(secretName).build();

        CreateSecretResponse createSecretResponse = secretsClient.createSecret(createSecretRequest);

        return createSecretResponse.name();
    }

    @Override
    public void DeleteSecret(String key) {

        DeleteSecretRequest deleteSecretRequest = DeleteSecretRequest.builder().secretId(key).build();

        DeleteSecretResponse deleteSecretResponse = secretsClient.deleteSecret(deleteSecretRequest);
    }


    private static String createSecretName(String name) {
        String baseName = sanitize(name);

        String randomSuffix = generateRandomString(12);

        String secretName = baseName + "-" + randomSuffix;

        if (secretName.startsWith("/") || secretName.endsWith("/")) {
            secretName = secretName.replaceAll("^/+|/+$", "");
        }

        return secretName;
    }

    private static String sanitize(String input) {
        if (input == null) return "";
        // Remove slashes and all disallowed characters, allow only ALLOWED_CHARS (except slash)
        return input.replaceAll("[^a-zA-Z0-9_+=.@-]", "");
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }
}
