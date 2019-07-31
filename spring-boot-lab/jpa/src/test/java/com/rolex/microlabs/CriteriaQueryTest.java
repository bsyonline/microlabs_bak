/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.model.FileSubDO;
import com.rolex.microlabs.model.FileSubDO_;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CriteriaQueryTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findAll() {
        String uid = "11";
        Date start = Date.from(LocalDate.of(2017, 2, 6).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDate.of(2019, 5, 31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileSubDO> query = criteriaBuilder.createQuery(FileSubDO.class);
        Root<FileSubDO> root = query.from(FileSubDO.class);
        List<Predicate> list = Lists.newArrayList();
        if (uid != null) {
//            list.add(criteriaBuilder.equal(root.get(FileSubDO_.uid), uid));
            list.add(criteriaBuilder.equal(root.get("uid"), uid));
        }
        if (start != null) {
            list.add(criteriaBuilder.greaterThanOrEqualTo(root.get(FileSubDO_.idt), start));
        }
        if (end != null) {
            list.add(criteriaBuilder.lessThanOrEqualTo(root.get(FileSubDO_.idt), end));
        }
        query.where(list.toArray(new Predicate[list.size()]));
        TypedQuery<FileSubDO> typedQuery = entityManager.createQuery(query);
        List<FileSubDO> orders = typedQuery.getResultList();
        System.out.println(orders);
    }

}
