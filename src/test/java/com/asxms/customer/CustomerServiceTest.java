package com.asxms.customer;

import com.asxms.exception.DuplicateResourceException;
import com.asxms.exception.RequestValidationException;
import com.asxms.exception.RessourceNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomer() {
        // When
        underTest.getAllCustomer();
        // Then
        Mockito.verify(customerDao).getAllCustomer();
    }

    @Test
    void getCustomer() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomer(id);
        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerNotFound() {
        // Given
        int id = 1;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found".formatted(id)
                );
    }

    @Test
    void insertCustomer() {
        // Given
        String email = "bar@gmail.com";

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "foo",
                email,
                22
        );

        // When
        underTest.insertCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer customer = customerArgumentCaptor.getValue();

        assertThat(customer.getId()).isNull();
        assertThat(customer.getName()).isEqualTo(request.name());
        assertThat(customer.getEmail()).isEqualTo(request.email());
        assertThat(customer.getAge()).isEqualTo(request.age());
    }

    @Test
    void insertCustomerEmailAlreadyTaken() {
        // Given
        String email = "bar@gmail.com";

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "foo",
                email,
                22
        );

        // When
        assertThatThrownBy(() -> underTest.insertCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(Mockito.any());
    }


    @Test
    void deleteCustomerById() {
        // Given
        int id = 10;
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        Mockito.verify(customerDao).deleteCustomerById(id);
    }
    @Test
    void deleteCustomerByIdNotFound() {
        // Given
        int id = 10;
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessage("customer with the id %s not found".formatted(id));
        // Then
        Mockito.verify(customerDao, Mockito.never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest("Alex", newEmail, 20);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);
        Mockito.when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void cannotUpdateCustomerCuzIdNotFound() {
        // Given
        int id = 1;
        String newEmail = "alex@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest("Alex", newEmail, 20);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessage("customer with the id %s not found".formatted(id));

        // Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }


    @Test
    void cannotUpdateCustomerCuzNoChanges() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(customer.getName(), customer.getEmail(), customer.getAge());
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);
        // When

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void canUpdateCustomerName() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest("Alex", null, null);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerEmail() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(null, newEmail, null);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);
        Mockito.when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void cannotUpdateCustomerEmail() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(null, newEmail, null);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);
        Mockito.when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void canUpdateCustomerAge() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "foo", "bar@gmail.com", 22
        );
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(null, null, 20);
        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }
}