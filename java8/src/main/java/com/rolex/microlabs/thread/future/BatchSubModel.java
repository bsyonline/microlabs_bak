/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.thread.future;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@Data
public class BatchSubModel {

    List<SubDO> order = Lists.newArrayList();
    List<SubDO> exists = Lists.newArrayList();
    List<String> notFound = Lists.newArrayList();
}
