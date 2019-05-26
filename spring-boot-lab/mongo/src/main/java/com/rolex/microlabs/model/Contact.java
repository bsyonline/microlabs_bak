/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rolex
 * @since 2019
 */
@Data
@AllArgsConstructor
public class Contact {

    String phone;
    String email;
    Location location;

}
