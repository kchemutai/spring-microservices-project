package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest registrationRequest) {
        Customer customer=Customer.builder()
                .firstName(registrationRequest.firstName())
                .lastname(registrationRequest.lastName())
                .email(registrationRequest.email())
                .build();

        //todo: check if email is valid
        //todo: check if email is not taken
        //todo: check if fraudster
        customerRepository.saveAndFlush(customer);
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8082/api/v1/fraud-check/{customerId}",
                    FraudCheckResponse.class,
                    customer.getId()
        );
        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
        //todo: check if customer in db

    }
}
