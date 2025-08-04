package com.osho.carrental.service.repository;

import com.osho.carrental.model.Customer;
import com.osho.carrental.model.Order;
import java.util.List;

public interface OrderServiceRepository {

    List<Order> getMyOrders(Customer customer);
    Order orderCar(Order order);
    Order updateOrder(Order order);
    Order cancelOrder(Order order);

}
