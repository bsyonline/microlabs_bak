/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import lombok.Data;

import java.io.Serializable;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 7329530374415722876L;

    private String requestId;
    private Throwable error;
    private Object result;
}
