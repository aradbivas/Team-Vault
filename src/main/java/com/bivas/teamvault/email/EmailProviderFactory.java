package com.bivas.teamvault.email;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProviderFactory {

    private final ApplicationContext context;

    private final EmailProperties emailProperties;

    public EmailProvider getProvider() {
        return (EmailProvider) context.getBean(emailProperties.getProvider());
    }
}
