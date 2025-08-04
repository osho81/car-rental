package com.osho.CarRental.service.repository;

import com.osho.CarRental.model.Customer;
import com.osho.CarRental.model.Order;
import java.util.List;

public interface OrderServiceRepository {

    List<Order> getMyOrders(Customer customer);
    Order orderCar(Order order);
    Order updateOrder(Order order);
    Order cancelOrder(Order order);

}
