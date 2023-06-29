package com.amigoscode.customer;

import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    public void registerCustomer(CustomerRegistrationRequest registrationRequest) {
        Customer customer=Customer.builder()
                .firstName(registrationRequest.firstName())
                .lastname(registrationRequest.lastName())
                .email(registrationRequest.email())
                .build();

        //todo: check if email is valid
        //todo: check if email is not taken
        //todo: check if customer in db

        customerRepository.save(customer);
    }
}
