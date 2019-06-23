/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class RpcResponse implements Serializable {
    private String requestId;
    private Exception exception;
    private Object result;
}
