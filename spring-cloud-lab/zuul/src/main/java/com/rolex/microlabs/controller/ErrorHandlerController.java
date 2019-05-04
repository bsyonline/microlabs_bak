/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rolex
 * @since 2018
 */
@RestController
public class ErrorHandlerController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String error() {
        return "{\"code\": 500, \"msg\":\"internal server error\"}";
    }
}
