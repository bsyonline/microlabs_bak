/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.service.CommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompressTest {

    @Autowired
    CommonService commonService;

    @Test
    public void business() {
        commonService.map.get("zip").compress("");
    }

}
