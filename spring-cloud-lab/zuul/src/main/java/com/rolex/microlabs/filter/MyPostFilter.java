/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.filter;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author rolex
 * @since 2018
 */
//@Component
@Slf4j
public class MyPostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
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
        log.info(String.format("filter name %s ordered %s", this.getClass().getName(), this.filterOrder()));
        RequestContext ctx = RequestContext.getCurrentContext();
        List<Pair<String, String>> headers = ctx.getZuulResponseHeaders();
        headers.add(0, new Pair<String, String>("X-RateLimit-Remaining", "30"));
//        String s = null;
//        s.toString();
        return null;
    }
}
