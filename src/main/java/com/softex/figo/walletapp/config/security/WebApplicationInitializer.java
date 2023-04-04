package com.softex.figo.walletapp.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class WebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}
