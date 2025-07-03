package com.easybytes.accounts.audits;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // In real application, fetch the logged-in username (e.g., from Spring Security)
        return Optional.of("ACCOUNTS_MS");
    }
}
