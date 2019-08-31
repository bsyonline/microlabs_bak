/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.future;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@Data
@Slf4j
public class SubRepository {

    public SubDO getSub(String entId, String userId) {
        if (Integer.parseInt(entId) % 1024 == 0) {
            return new SubDO(entId, userId);
        }
        return null;
    }

    public void insertAll(List<SubDO> list) throws InterruptedException {
        //log.info("insert all, size={}", list.size());
        Thread.sleep(100);
    }
}
