package com.homfo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@RequiredArgsConstructor
@Configuration
public class WebConfig {
    private final Environment env;

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();

        loggingFilter.setIncludeHeaders(false);

        if (isDevProfile()) {
            loggingFilter.setIncludeQueryString(true);
            loggingFilter.setIncludePayload(true);
            loggingFilter.setMaxPayloadLength(10000);
        } else {
            loggingFilter.setIncludeQueryString(false);
            loggingFilter.setIncludePayload(false);
        }

        return loggingFilter;
    }

    private boolean isDevProfile() {
        return env.acceptsProfiles(Profiles.of("dev", "local"));
    }
}
