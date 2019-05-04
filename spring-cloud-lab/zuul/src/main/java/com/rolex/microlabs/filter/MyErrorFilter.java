/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2018
 */
@Component
@Slf4j
public class MyErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        log.info(String.format("take exception %s", throwable.getCause().getMessage()));
//        ctx.set("error.status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        ctx.set("error.exception", throwable.getCause());
        return null;
    }
}
