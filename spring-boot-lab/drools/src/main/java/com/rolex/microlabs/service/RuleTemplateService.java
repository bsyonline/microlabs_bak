/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.model.Product;
import com.rolex.microlabs.provider.ProductProvider;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.DataProviderCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author rolex
 * @since 2018
 */
@Service
public class RuleTemplateService {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public Product ruleTemplate(Product product) throws FileNotFoundException {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_template_rules");
        KieSession kieSession = kieContainer(list);
        kieSession.insert(product);
        kieSession.fireAllRules();
        kieSession.dispose();
        return product;
    }
    
    public static KieSession kieContainer(List<Map<String, Object>> list) throws FileNotFoundException {
        String drtFile = "src/main/resources/rules/template.drt";
        ProductProvider productProvider = new ProductProvider(list);
        DataProviderCompiler compiler = new DataProviderCompiler();
        String drlString = compiler.compile(productProvider, getRulesStream(drtFile));
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newByteArrayResource(drlString.getBytes()), ResourceType.DRL);
        InternalKnowledgeBase base = KnowledgeBaseFactory.newKnowledgeBase();
        base.addPackages(kbuilder.getKnowledgePackages());
        KieSession kSession = base.newKieSession();
        return kSession;
    }
    
    public static InputStream getRulesStream(String file) throws FileNotFoundException {
        return new FileInputStream(file);
    }
    
}
