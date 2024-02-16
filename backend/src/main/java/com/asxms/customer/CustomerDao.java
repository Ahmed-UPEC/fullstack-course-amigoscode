package com.asxms.customer;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;


/**
 * DAO LAYER - Database connections CRUD operations, defining
 * the schema for the CRUD operation
 */
public interface CustomerDao {
    // GET HTTP
    List<Customer> getAllCustomer();

    // GET HTTP
    Optional<Customer> selectCustomerById(Integer customerId);

    // POST HTTP
    void insertCustomer(Customer customer);

    boolean existsPersonWithEmail(String email);

    void deleteCustomerById(Integer id);
    boolean existsPersonWithId(Integer id);

    void updateCustomer(Customer update);

}
