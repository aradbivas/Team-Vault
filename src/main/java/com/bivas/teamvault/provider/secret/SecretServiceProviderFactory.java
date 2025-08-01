package com.bivas.teamvault.provider.secret;

import com.bivas.teamvault.properties.SecretServiceProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SecretServiceProviderFactory {

    private final ApplicationContext context;

    private final SecretServiceProperties secretServiceProperties;

    public SecretServiceProvider getProvider() {
        return (SecretServiceProvider) context.getBean(secretServiceProperties.getProvider());
    }
}
