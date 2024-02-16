package com.asxms.customer;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "customer", uniqueConstraints = {@UniqueConstraint(name = "customer_email_unique", columnNames = "email")})
public class Customer {

    @Id
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1 // incrementation size
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @Column(columnDefinition = "bigserial" // add this annotation with this line because of this
            /*
            * Caused by: org.hibernate.tool.schema.spi.SchemaManagementException:
            * Schema-validation: wrong column type encountered in column [id] in
            * table [customer]; found [bigserial (Types#BIGINT)], but
            * expecting [integer (Types#INTEGER)
            * */)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private Integer age;

    public Customer() {
    }

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer(Integer id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(age, customer.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", age=" + age + '}';
    }
}