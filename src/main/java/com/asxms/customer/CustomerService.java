package com.asxms.customer;

import com.asxms.exception.DuplicateResourceException;
import com.asxms.exception.RessourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * BUSINESS LAYER - Logic
 *
 * @Service -> Required for this layer otherwise no bean error
 * in the Controller
 */
@Service
public class CustomerService {

    // init the customerDAO (CRUD operation layer -> interface)
    private final CustomerDao customerDao;

    // Constructor
    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return customerDao.getAllCustomer();
    }

    public Customer getCustomer(Integer customerId) {
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(
                        () -> new IllegalArgumentException("customer with id [%s]".formatted(customerId))
                );
    }

    public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        String email = customerRegistrationRequest.email();

        if (customerDao.existsPersonWithEmail(email)) {
            // check if email exists
            throw new DuplicateResourceException(
                    "email already taken"
            );
        } else {
            // add new customer
            Customer customerToAdd = new Customer(
                    customerRegistrationRequest.name(),
                    customerRegistrationRequest.email(),
                    customerRegistrationRequest.age()
            );
            customerDao.insertCustomer(
                    customerToAdd
            );
        }
    }

    public void deleteCustomerById(Integer id) {
        if (!customerDao.existsPersonWithId(id)) {
            // Customer search by Id does not exists
            throw new RessourceNotFoundException(
                    "customer with the id %s not found".formatted(id)
            );
        }
        else {
            // Customer search by Id exists
            customerDao.deleteCustomerById(id);
        }
    }

}
