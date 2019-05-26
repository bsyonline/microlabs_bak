/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.rolex.microlabs.model.Book;
import com.rolex.microlabs.model.Customer;
import com.rolex.microlabs.repository.CustomerRepository;
import com.rolex.microlabs.repository2.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author rolex
 * @since 2018
 */
@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/customers")
    public List<Customer> customers() {
        List<Customer> list = ImmutableList.copyOf(customerRepository.findAll());
        for (Customer customer : list) {
            customer.setBooks(Sets.newHashSet(bookRepository.findByCustomerId(customer.getId())));
        }
        return list;
    }

    @GetMapping("/customers/{customer_id}/books")
    public List<Book> books(@PathVariable("customer_id") Integer customerId) {
        return ImmutableList.copyOf(bookRepository.findByCustomerId(customerId));
    }

    @GetMapping("/init")
    public String init() {
        customerRepository.deleteAll();
        bookRepository.deleteAll();
        StringBuffer sb = new StringBuffer();
        // save a couple of customers
        customerRepository.save(new Customer("Jack", "Bauer"));
        customerRepository.save(new Customer("Chloe", "O'Brian"));
        customerRepository.save(new Customer("Kim", "Bauer"));
        customerRepository.save(new Customer("David", "Palmer"));
        customerRepository.save(new Customer("Michelle", "Dessler"));

        // fetch all customers
        sb.append("Customers created:<br>");
        sb.append("[<br>");
        for (Customer customer : customerRepository.findAll()) {
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(customer.toString()).append("<br>");
        }
        sb.append("]<br>");

        sb.append("Books created:<br>");
        bookRepository.save(new Book("English", 10.4, customerRepository.findAll().get(0).getId()));
        bookRepository.save(new Book("Math", 20.1, customerRepository.findAll().get(0).getId()));
        bookRepository.save(new Book("Chinese", 14.5, customerRepository.findAll().get(1).getId()));
        sb.append("[<br>");
        for (Book book : bookRepository.findAll()) {
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(book.toString()).append("<br>");
        }
        sb.append("]<br>");
        sb.append("<a href='http://localhost:8083/customers'>Customer List</a>");
        return sb.toString();
    }
}
