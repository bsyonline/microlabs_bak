/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.future;

import lombok.Data;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class SubDO {

    String entId;
    String userId;

    public SubDO(String entId, String userId) {
        this.entId = entId;
        this.userId = userId;
    }
}
