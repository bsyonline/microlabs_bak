/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.controller;

import com.rolex.microlabs.model.Product;
import com.rolex.microlabs.service.DatabaseRulesService;
import com.rolex.microlabs.service.ExcelRulesService;
import com.rolex.microlabs.service.FileRulesService;
import com.rolex.microlabs.service.RuleTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * @author rolex
 * @since 2018
 */
@RestController
public class DroolsController {

    @Autowired
    FileRulesService fileRulesService;
    @Autowired
    DatabaseRulesService databaseRulesService;
    @Autowired
    RuleTemplateService ruleTemplateService;
    @Autowired
    ExcelRulesService excelRulesService;


    @RequestMapping(value = "/file/getDiscount", method = RequestMethod.GET, produces = "application/json")
    public Product readRulesFromFile(@RequestParam(required = true) String type) {
        Product product = new Product();
        product.setType(type);
        return fileRulesService.getProductDiscount(product);
    }

    @RequestMapping(value = "/db/getDiscount", method = RequestMethod.GET, produces = "application/json")
    public Product readRulesFromDB(@RequestParam(required = true) String type) {
        Product product = new Product();
        product.setType(type);
        return databaseRulesService.readRulesFromDB(product);
    }

    @RequestMapping(value = "/template/getDiscount", method = RequestMethod.GET, produces = "application/json")
    public Product ruleTemplate(@RequestParam(required = true) String type) throws FileNotFoundException {
        Product product = new Product();
        product.setType(type);
        return ruleTemplateService.ruleTemplate(product);
    }
    
    @RequestMapping(value = "/excel/getDiscount", method = RequestMethod.GET, produces = "application/json")
    public Product readRuleFromExcel(@RequestParam(required = true) String type) throws FileNotFoundException {
        Product product = new Product();
        product.setType(type);
        return ruleTemplateService.ruleTemplate(product);
    }
}
