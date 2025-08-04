package com.osho.CarRental.controller;

import com.osho.CarRental.model.Order;
import com.osho.CarRental.service.ExchangeService;
import com.osho.CarRental.valueobject.ResponseTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    // Requirement: En microservice ska implementeras* GET /api/v1/exchange
    @GetMapping("/exchange") // USER ROLE REQUIRED
    public ResponseTemplateVO getExchanged(@RequestBody Order order) {
        // calling service class method, to further reach the exchange microservice
        return exchangeService.getExchangeService(order.getId());
    }

}
