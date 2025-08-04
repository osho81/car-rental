package com.osho.twCarRental.service;

import com.osho.twCarRental.model.Order;
import com.osho.twCarRental.repository.OrderRepository;
import com.osho.twCarRental.service.repository.ExchangeServiceRepository;
import com.osho.twCarRental.valueobject.Exchange;
import com.osho.twCarRental.valueobject.ResponseTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Class with method calling Exchange-microservice, via the gateway

@Service
public class ExchangeService implements ExchangeServiceRepository {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseTemplateVO getExchangeService(int orderId) {
        System.out.println("I am in car-rental exchange service method"); // Control print

        ResponseTemplateVO vo = new ResponseTemplateVO(); // Enabling full/combined response

        Order actualOrder = orderRepository.findById(orderId).get();

        double amount = actualOrder.getPrice(); // Extract amount from found order obj

        // Use app-name for microservice via gateway, instead of localhost/port; pass amount as url-var
        Exchange exchange = restTemplate.getForObject("http://EXCHANGE-SERVICE/change/" + amount, Exchange.class);

        // Optionally update & save price in euro in the order, for later reference
        actualOrder.setPriceInEuro(exchange.getAmountInEur());
        orderRepository.save(actualOrder);

        vo.setOrder(actualOrder); // Add the order to full response
        vo.setExchange(exchange); // Add the exchange details to full response

        return vo;
    }

}
