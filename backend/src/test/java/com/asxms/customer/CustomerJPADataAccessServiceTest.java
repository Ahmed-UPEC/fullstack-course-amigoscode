package com.asxms.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable; // handler open | close mock

    @Mock
    private CustomerRepository customerRepository; // create a mock

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // opens it b4 test
        underTest = new CustomerJPADataAccessService(customerRepository); // injection
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close(); // closes it after test
    }

    /*
    * The idea for each test is to make sure that
    * The methods are calling the JPA repository methods
    * And we use Mockito.verify for this
    */

    @Test
    void getAllCustomer() {
        // When
        underTest.getAllCustomer();
        // Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.selectCustomerById(id);
        // Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                "foo",
                "bar@gmail.com",
                23,
                Gender.MALE);
        // When
        underTest.insertCustomer(customer);
        // Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "bar@gmail.com";
        // With
        underTest.existsPersonWithEmail(email);
        // Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        // With
        underTest.deleteCustomerById(id);
        // Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void existsPersonWithId() {
        // Given
        int id = 2;
        // With
        underTest.existsPersonWithId(id);
        // Then
        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                "foo",
                "bar@gmail.com",
                23,
                Gender.MALE);
        // When
        underTest.updateCustomer(customer);
        // Then
        Mockito.verify(customerRepository).save(customer);
    }
}