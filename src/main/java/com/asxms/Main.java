package com.asxms;

import com.asxms.customer.Customer;
import com.asxms.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @SpringBootApplication -> Required to init the Spring Framework
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Application start method
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Customer alex = new Customer(
                    "Alex",
                    "alex@gmail.com",
                    21
            );
            Customer jamila = new Customer(
                    "Jamila",
                    "jamila@gmail.com",
                    19
            );
            List<Customer> customers = List.of(alex, jamila);
            //customerRepository.saveAll(customers);
        };
    }

}
