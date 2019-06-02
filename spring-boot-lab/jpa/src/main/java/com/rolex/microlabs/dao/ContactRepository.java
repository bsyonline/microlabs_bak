package com.rolex.microlabs.dao;

import com.rolex.microlabs.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rolex
 * @since 2019
 */
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
