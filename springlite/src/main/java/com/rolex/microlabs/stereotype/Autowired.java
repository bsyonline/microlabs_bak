/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.stereotype;

import java.lang.annotation.*;

/**
 * @author rolex
 * @since 2019
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

	/**
     * Declares whether the annotated dependency is required.
	 * <p>Defaults to {@code true}.
	 */
	boolean required() default true;

}