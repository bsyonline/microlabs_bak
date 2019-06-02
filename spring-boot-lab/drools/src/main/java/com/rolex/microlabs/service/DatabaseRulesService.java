/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.model.Product;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


/**
 * @author rolex
 * @since 2018
 */
@Service
public class DatabaseRulesService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Product readRulesFromDB(Product product) {
        String ruleString = jdbcTemplate.queryForObject("select rule from t_rules where id=1", String.class);
        System.out.println("ruleString = " + ruleString);
        KieSession kieSession = kieContainer(ruleString);
        kieSession.insert(product);
        kieSession.fireAllRules();
        kieSession.dispose();
        return product;
    }
    
    public static KieSession kieContainer(String drlString) {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newByteArrayResource(drlString.getBytes()), ResourceType.DRL);
        InternalKnowledgeBase base = KnowledgeBaseFactory.newKnowledgeBase();
        base.addPackages(kbuilder.getKnowledgePackages());
        KieSession kSession = base.newKieSession();
        return kSession;
    }
    
}
