/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.repository2;

import com.rolex.microlabs.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author rolex
 * @since 2018
 */
public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByCustomerId(Integer customerId);
    
}