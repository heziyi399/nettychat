package com.netty.config;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author hzy
 * @date 2022-04-02
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component

public @interface NettyHandler {
    String key();
}
