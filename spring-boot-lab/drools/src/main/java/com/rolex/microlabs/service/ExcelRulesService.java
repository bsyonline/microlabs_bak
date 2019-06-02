/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.model.Product;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

/**
 * @author rolex
 * @since 2018
 */
@Service
public class ExcelRulesService {
    
    
    public Product getProductDiscount(Product product) {
        KieSession kieSession = kieSession();
        kieSession.insert(product);
        kieSession.fireAllRules();
        kieSession.dispose();
        return product;
    }
    
    public KieSession kieSession() {
        String drlFile = "src/main/resources/rules/product.xls";
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newFileResource(drlFile));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId()).newKieSession();
    }
}
