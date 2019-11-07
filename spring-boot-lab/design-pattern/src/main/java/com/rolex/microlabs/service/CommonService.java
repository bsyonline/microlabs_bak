/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author rolex
 * @since 2019
 */
@Component
public class CommonService {

    public Map<String, Compress> map = Maps.newHashMap();

    @Autowired
    public CommonService(List<Compress> list) {
        for (Compress compress : list) {
            map.put(compress.type(), compress);
        }
    }

    public void compress(CompressCallback callback, String path) throws Exception {
        callback.doCompress(new Compress() {
            @Override
            public String type() {
                return "zip";
            }
    
            @Override
            public String compress(String path) {
                System.out.println("compress callback" + path);
                return null;
            }
        });
    }

}
