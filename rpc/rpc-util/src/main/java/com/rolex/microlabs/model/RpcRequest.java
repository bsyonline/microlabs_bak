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
public class RpcRequest implements Serializable {
    private String requestId;
    private String interfaceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
