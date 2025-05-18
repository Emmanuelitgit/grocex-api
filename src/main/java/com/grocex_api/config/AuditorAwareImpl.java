package com.grocex_api.config;

import com.grocex_api.utils.AppUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class AuditorAwareImpl implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {
        return Optional.of(AppUtils.getAuthenticatedUsername());
    }
}
