package com.osho.CarRental.valueobject;

import com.osho.CarRental.model.Order;
import org.springframework.stereotype.Service;

// Template class for handling full/combined response from exchange service

@Service
public class ResponseTemplateVO {

    private Order order;
    private Exchange exchange;

    public ResponseTemplateVO() {
    }

    public ResponseTemplateVO(Order order, Exchange exchange) {
        this.order = order;
        this.exchange = exchange;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
