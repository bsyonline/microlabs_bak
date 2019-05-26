/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class Order {

    @Ignore
    String objectId;
    Integer customerId;
    Integer userId;
    String name;
    Contact contact;
    String idt;
    String udt;

}
