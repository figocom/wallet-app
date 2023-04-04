package com.softex.figo.walletapp.config.security;

import com.softex.figo.walletapp.domain.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUser {

    boolean required() default true;

    String value() default "authUser";

    Class<? extends AuthUser> authUser() default AuthUser.class;
}
