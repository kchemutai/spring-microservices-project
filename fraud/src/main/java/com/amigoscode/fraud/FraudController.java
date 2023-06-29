package com.amigoscode.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {
    private final FraudCheckService fraudCheckService;

    @GetMapping("/{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable Integer customerId){
        Boolean isFraudulent = fraudCheckService.isFraudulentCustomer(customerId);
        log.info("Fraud check for customer "+customerId);
        return new FraudCheckResponse(isFraudulent);
    }
}
