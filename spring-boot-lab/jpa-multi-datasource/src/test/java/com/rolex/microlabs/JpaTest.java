package com.rolex.microlabs;

import com.rolex.microlabs.model.Book;
import com.rolex.microlabs.model.Customer;
import com.rolex.microlabs.repository.CustomerRepository;
import com.rolex.microlabs.repository2.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rolex
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTest {
    
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BookRepository bookRepository;
    
    @Test
    public void init() {
        // save a couple of customers
        customerRepository.save(new Customer("Jack", "Bauer"));
        customerRepository.save(new Customer("Chloe", "O'Brian"));
        customerRepository.save(new Customer("Kim", "Bauer"));
        customerRepository.save(new Customer("David", "Palmer"));
        customerRepository.save(new Customer("Michelle", "Dessler"));
        
        // fetch all customers
        logger.info("Customers found with findAll():");
        logger.info("-------------------------------");
        for (Customer customer : customerRepository.findAll()) {
            logger.info(customer.toString());
        }
        logger.info("");
        
        // fetch an individual customer by ID
        customerRepository.findById(1)
            .ifPresent(customer -> {
                logger.info("Customer found with findById(1L):");
                logger.info("--------------------------------");
                logger.info(customer.toString());
                logger.info("");
            });
        
        // fetch customers by last name
        logger.info("Customer found with findByLastName('Bauer'):");
        logger.info("--------------------------------------------");
        customerRepository.findByLastName("Bauer").forEach(bauer -> {
            logger.info(bauer.toString());
        });
        // for (Customer bauer : repository.findByLastName("Bauer")) {
        // 	log.info(bauer.toString());
        // }
        logger.info("");
        
        bookRepository.save(new Book("English", 10.4, 1));
        bookRepository.save(new Book("Math", 20.1,1));
        bookRepository.save(new Book("Chinese", 14.5, 2));
        logger.info("Books found with findAll():");
        logger.info("-------------------------------");
        for (Book book : bookRepository.findAll()) {
            logger.info(book.toString());
        }
    }
    
}
