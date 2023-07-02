package com.amigoscode.customer;

import com.amigoscode.amqp.RabbitMQMessageProducer;
import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClient;
import com.amigoscode.clients.notifications.NotificationClient;
import com.amigoscode.clients.notifications.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
        //todo:make it asyc. ie add to queue
        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s welcome to AmigosCode...", customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key");
        //todo: check if customer in db

    }
}
