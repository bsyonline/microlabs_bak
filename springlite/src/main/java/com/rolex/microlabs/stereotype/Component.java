/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.stereotype;

import java.lang.annotation.*;

/**
 * @author rolex
 * @since 2019
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}