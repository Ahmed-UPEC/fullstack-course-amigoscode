package com.asxms.customer;

import com.asxms.exception.DuplicateResourceException;
import com.asxms.exception.RequestValidationException;
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
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return customerDao.getAllCustomer();
    }

    public Customer getCustomer(Integer customerId) {
        return customerDao.selectCustomerById(customerId).orElseThrow(() -> new RessourceNotFoundException("customer with id [%s] not found".formatted(customerId)));
    }

    public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        String email = customerRegistrationRequest.email();

        if (customerDao.existsPersonWithEmail(email)) {
            // check if email exists
            throw new DuplicateResourceException("email already taken");
        } else {
            // add new customer
            Customer customerToAdd = new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age());
            customerDao.insertCustomer(customerToAdd);
        }
    }

    public void deleteCustomerById(Integer id) {
        if (!customerDao.existsPersonWithId(id)) {
            // Customer search by Id does not exists
            throw new RessourceNotFoundException("customer with the id %s not found".formatted(id));
        } else {
            // Customer search by Id exists
            customerDao.deleteCustomerById(id);
        }
    }

    public void updateCustomer(Integer id, CustomerRegistrationRequest updateRequest) {
        if (!customerDao.existsPersonWithId(id)) {
            // Customer search by Id does not exists
            throw new RessourceNotFoundException("customer with the id %s not found".formatted(id));
        } else {
            // get a Customer for a specific id
            Customer customer = getCustomer(id);

            boolean changes = false;

            // if in the json body the field name is different than null
            // AND
            // the field name is NOT equal to the current customer Name
            // we make some changes
            if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
                customer.setName(updateRequest.name());
                changes = true;
            }

            if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
                customer.setAge(updateRequest.age());
                changes = true;
            }

            if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
                // check if email is already taken or not
                if (customerDao.existsPersonWithEmail(updateRequest.email())) {
                    // check if email exists
                    throw new DuplicateResourceException("email already taken");
                }
                // email is not taken
                else {
                    customer.setEmail(updateRequest.email());
                    changes = true;
                }
            }

            // if no changes we throw error
            if (!changes) {
                throw new RequestValidationException("no data changes found");
            }
            // if changes we update
            else {
                customerDao.updateCustomer(customer);
            }
        }
    }

}
