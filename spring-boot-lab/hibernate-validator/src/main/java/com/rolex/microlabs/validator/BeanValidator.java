/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author rolex
 * @since 2018
 */
@Component
public class BeanValidator implements InitializingBean {
    
    private javax.validation.Validator validator;
    
    public BeanValidatorResult validate(Object obj) {
        BeanValidatorResult validatorResult = new BeanValidatorResult();
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        if (!set.isEmpty()) {
            validatorResult.setHasErrors(true);
            set.stream().forEach(constraintViolation -> {
                String msg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validatorResult.getErrorMsgMap().put(propertyName, msg);
            });
        }
        return validatorResult;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }
    
}
