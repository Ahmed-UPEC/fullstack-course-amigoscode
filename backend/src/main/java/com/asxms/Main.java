package com.asxms;

import com.asxms.customer.Customer;
import com.asxms.customer.CustomerRepository;
import com.asxms.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

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
            insertRandomUser(1, customerRepository);
        };
    }

    public void insertRandomUser(int n, CustomerRepository customerRepository) {
        for (int i = 0; i < n; i++) {
            Faker fake = new Faker();

            String firstname = fake.name().firstName().toLowerCase();
            String lastname = fake.name().lastName().toLowerCase();
            Random random = new Random();
            Integer age = random.nextInt(16, 99);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            Customer customer = new Customer(
                    firstname + " " + lastname,
                    firstname + "." + lastname + "@asxms.fr",
                    age,
                    gender);
            customerRepository.save(customer);
        }
    }

}
