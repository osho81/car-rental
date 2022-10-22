package com.osho.twCarRental.service;

import com.osho.twCarRental.model.Car;
import com.osho.twCarRental.model.Customer;
import com.osho.twCarRental.model.Order;
import com.osho.twCarRental.repository.CarRepository;
import com.osho.twCarRental.repository.CustomerRepository;
import com.osho.twCarRental.repository.OrderRepository;
import com.osho.twCarRental.service.repository.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class OrderService implements OrderServiceRepository {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    CarRepository carRepository;


    ////--------------- READ (GET) ----------------////

    @Override
    public Order getOrderById(int id) {
        Optional<Order> foundOrder = orderRepository.findById(id);
        if (foundOrder.isPresent()) {
            return foundOrder.get();
        } else {
            throw new RuntimeException("Order with id " + id + " not found.");
        }
    }

    @Override
    public Order getOrderByOrderNr(String orderNr) {
        Optional<Order> foundOrder = orderRepository.findByOrderNr(orderNr);
        if (foundOrder.isPresent()) {
            return foundOrder.get();
        } else {
            throw new RuntimeException("Order with order nr " + orderNr + " not found.");
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByUser(String email) {
        Optional<Customer> foundCustomer = customerRepository.findByEmail(email);
        if (foundCustomer.isPresent()) {
            return foundCustomer.get().getOrdersByCustomer();
        } else {
            throw new RuntimeException("No user with email " + email);
        }
    }


    ////-------------- CREATE (SAVE) --------------////
    @Override
    public Order orderCar(Order order) {
        Optional<Order> foundOrder = orderRepository.findByOrderNr(order.getOrderNr());
        if (foundOrder.isPresent()) {
            throw new RuntimeException(order.getOrderNr() + " already exists.");
        } else {
            // Temp date/time field, in case request-body is missing time of order
            LocalDateTime tempDateTime;
            if (order.getOrderOrUpdateTime() == null) {
                tempDateTime = LocalDateTime.now();
            } else {
                tempDateTime = order.getOrderOrUpdateTime();
            }

            // Fetch duration between first and last rental day, include day 0 (i.e. add +1)
            int tempNumOfDays = (int) DAYS.between(order.getFirstRentalDay(), order.getLastRentalDay()) + 1;

            // Calculate price, by getting number of days * price, if price is missing in body
            double tempPrice;
            if (order.getPrice() == 0) {
                tempPrice = tempNumOfDays * carRepository.findById(order.getCarId()).orElse(null).getDailySek();
            } else {
                tempPrice = order.getPrice();
            }

            // Use fields from req-body: at least orderNr, first/last rent day, customerId, carId
            // and eventual temp-fields: order/update date, price, numOfDays
            return orderRepository.save(new Order(
                    order.getOrderNr(),
                    tempDateTime,
                    order.getFirstRentalDay(),
                    order.getLastRentalDay(),
                    order.getCustomerId(),
                    order.getCarId(),
                    tempPrice,
                    tempNumOfDays
            ));
        }
    }

    @Override
    public Order updateOrder(Order order) {

        // Get order to update with id if exists, else get with order nr if that exists
        Optional<Order> foundById = orderRepository.findById(order.getId());
        Optional<Order> foundByOrderNr = orderRepository.findByOrderNr(order.getOrderNr());
        if (foundById.isEmpty() && foundByOrderNr.isEmpty()) {
            throw new RuntimeException("Order with id " + order.getId()
                    + " or order nr " + order.getOrderNr() + " not found");
        }

        // Then, if either id or reg.nr exists, get order by one of them
        Order orderToUpdate = foundById.isPresent() ? orderRepository.findById(order.getId()).get() :
                orderRepository.findByOrderNr(order.getOrderNr()).get();

        // Add new value to each column, but keep old value if new is null/empty
        orderToUpdate.setOrderNr(order.getOrderNr() == null ? orderToUpdate.getOrderNr() : order.getOrderNr());
        orderToUpdate.setOrderOrUpdateTime(LocalDateTime.now());
        orderToUpdate.setFirstRentalDay(order.getFirstRentalDay() == null ? orderToUpdate.getFirstRentalDay() : order.getFirstRentalDay());
        orderToUpdate.setLastRentalDay(order.getLastRentalDay() == null ? orderToUpdate.getLastRentalDay() : order.getLastRentalDay());
        orderToUpdate.setCustomerId(order.getCustomerId() == 0 ? orderToUpdate.getCustomerId() : order.getCustomerId());
        orderToUpdate.setCarId(order.getCarId() == 0 ? orderToUpdate.getCarId() : order.getCarId());
        orderToUpdate.setPrice(order.getPrice() == 0 ? orderToUpdate.getPrice() : order.getPrice());
        orderToUpdate.setNumberOfDays(order.getNumberOfDays() == 0 ? orderToUpdate.getNumberOfDays() : order.getNumberOfDays());

        // Save, i.e. update existing order, with new (and eventual old) passed in values
        return orderRepository.save(orderToUpdate);
    }
}
