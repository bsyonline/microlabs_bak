/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.validator;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author rolex
 * @since 2018
 */
@Data
public class BeanValidatorResult {
    
    private boolean hasErrors = false;
    private Map<String, String> errorMsgMap = Maps.newHashMap();
    
    public String getErrorMsg() {
        return StringUtils.join(errorMsgMap.values(), ",");
    }
}
