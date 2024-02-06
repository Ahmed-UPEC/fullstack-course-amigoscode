package com.asxms.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API LAYER - Exposing all the routes
 *
 * @RestController -> Required for the Spring Framework
 */
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    // init the customerService (Business logic layer)
    private final CustomerService customerService;

    // Constructor
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomer() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.insertCustomer(request);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
        customerService.deleteCustomerById(customerId);
    }

}
