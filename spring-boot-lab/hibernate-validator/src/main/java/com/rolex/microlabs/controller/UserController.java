/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.controller;

import com.rolex.microlabs.exception.BusinessException;
import com.rolex.microlabs.model.User;
import com.rolex.microlabs.validator.BeanValidator;
import com.rolex.microlabs.validator.BeanValidatorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rolex
 * @since 2018
 */
@RestController
public class UserController {

    @Autowired
    BeanValidator validator;

    @PostMapping("/users")
    public User add(@RequestBody User user) throws BusinessException {
        BeanValidatorResult validatorResult = validator.validate(user);
        if (validatorResult.isHasErrors()) {
            throw new BusinessException(validatorResult.getErrorMsg());
        }
        return user;
    }

    @ExceptionHandler(Exception.class)
    public Object handler(Exception e) {
        return e.getMessage();
    }
}
