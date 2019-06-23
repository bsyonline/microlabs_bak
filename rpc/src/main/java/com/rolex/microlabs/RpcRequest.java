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
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -4364536436151723421L;

    private String requestId;
    private long createMillisTime;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
