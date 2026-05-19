package com.autofix.config;

import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TwilioConfig {

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.phone-number:}")
    private String phoneNumber;

    @jakarta.annotation.PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isEmpty() && authToken != null && !authToken.isEmpty()) {
            Twilio.init(accountSid, authToken);
            log.info("✅ Twilio initialized with account: {}", accountSid.substring(0, 5) + "***");
        } else {
            log.warn("⚠️ Twilio not configured - SMS notifications will be disabled");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
