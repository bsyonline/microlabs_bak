/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.thread.future;

import lombok.Data;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class EntInfoDO {
    String entId;
    String entName;

    public EntInfoDO(String entId) {
        this.entId = entId;
        this.entName = "entName_" + entId;
    }
}
