package com.asxms.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository, will be managing the CRUD operations
 * Using JPA - @Repository is not needed
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // @Query
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
}
