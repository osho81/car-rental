package com.osho.CarRental.service;

import com.osho.CarRental.model.Customer;
import com.osho.CarRental.model.Order;
import com.osho.CarRental.repository.CarRepository;
import com.osho.CarRental.repository.CustomerRepository;
import com.osho.CarRental.repository.OrderRepository;
import com.osho.CarRental.service.repository.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public List<Order> getMyOrders(Customer customer) {

        // Find customer, then get the associated orders list
        Optional<Customer> foundCustomerById = customerRepository.findById(customer.getId());
        Optional<Customer> foundCustomerByEmail = customerRepository.findByEmail(customer.getEmail());

        // If not found by id or email
        if (foundCustomerById.isEmpty() && foundCustomerByEmail.isEmpty()) {
            throw new RuntimeException("Customer with id " + customer.getId()
                    + " or email " + customer.getEmail() + " not found");
        }

        // If found get customer by customer id or email, then get that customer's order list
        List<Order> customerOrderList = foundCustomerById.isPresent() ? foundCustomerById.get().getOrdersByCustomer() :
        foundCustomerByEmail.get().getOrdersByCustomer();
        return customerOrderList;
    }

    @Override
    public Order orderCar(Order order) {

        Optional<Order> foundOrder = orderRepository.findByOrderNr(order.getOrderNr());

        if (foundOrder.isPresent()) {
            throw new RuntimeException("Order nr " + order.getOrderNr() + " already exists.");
        } else {
            // Else if unique order nr, prepare the Order field values:

            // Temp date/time field, in case request-body is missing time of order
            LocalDateTime tempDateTime;
            if (order.getOrderOrUpdateTime() == null) {
                tempDateTime = LocalDateTime.now();
            } else {
                tempDateTime = order.getOrderOrUpdateTime();
            }

            // Fetch duration between first and last rental day, include day 0 (i.e. add +1)
            // If dates are not chosen yet, temporary set to 1 day
            int tempNumOfDays = (order.getFirstRentalDay() != null && order.getLastRentalDay() != null) ?
                    ((int) DAYS.between(order.getFirstRentalDay(), order.getLastRentalDay()) + 1) : 1;

            // Calculate price, by getting numOfdays*price, if dates and carId are not missing in body
            double tempPrice;
            if (order.getPrice() == 0) {
                tempPrice = tempNumOfDays * carRepository.findById(order.getCarId()).orElse(null).getDailySek();
            } else {
                tempPrice = order.getPrice();
            }

            // Use "compulsory fields" from req-body: orderNr, first/last rent day, customerId, carId
            // and eventual temp-fields: order/update date, price, numOfDays
            return orderRepository.save(new Order(
                    order.getOrderNr(),
                    false,
                    tempDateTime,
                    order.getFirstRentalDay(),
                    order.getLastRentalDay(),
                    order.getCustomerId(),
                    order.getCarId(),
                    tempPrice,
                    tempNumOfDays,
                    0 // price in euro is zero at first (see exchange service)
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

        // If either id or reg.nr exists (and orderNr is unique), get order by one of them
        Order orderToUpdate = foundById.isPresent() ? orderRepository.findById(order.getId()).get() :
                orderRepository.findByOrderNr(order.getOrderNr()).get();

        //---- Add new value to each column, but keep old value if new is null/empty etc. ----//

        // Make sure not to update into orderNr that already exists
        if (foundByOrderNr.isPresent()) {
            orderToUpdate.setOrderNr(orderToUpdate.getOrderNr());
        } else {
            orderToUpdate.setOrderNr(order.getOrderNr());
        }

        LocalDateTime tempDateTime;
        if (order.getOrderOrUpdateTime() == null) {
            tempDateTime = LocalDateTime.now();
        } else {
            tempDateTime = order.getOrderOrUpdateTime();
        }
        orderToUpdate.setOrderOrUpdateTime(tempDateTime);

        // Calculate numOfDays and check carId
        int tempNumOfDays = (order.getFirstRentalDay() != null && order.getLastRentalDay() != null) ?
                ((int) DAYS.between(order.getFirstRentalDay(), order.getLastRentalDay()) + 1) : 1;
        int tempCarId = order.getCarId() == 0 ? orderToUpdate.getCarId() : order.getCarId();

        // Calculate price, by getting number of days & price, if dates are not missing in body
        double tempPrice;
        if (carRepository.findById(tempCarId).isPresent()) {
            tempPrice = tempNumOfDays * carRepository.findById(tempCarId).orElse(null).getDailySek();
        } else {
            throw new RuntimeException("Please enter car id of ordered car.");
        }

        orderToUpdate.setCanceled(order.isCanceled() == true ? orderToUpdate.isCanceled() : order.isCanceled()); // Use cancelOrder to cancel
        orderToUpdate.setFirstRentalDay(order.getFirstRentalDay() == null ? orderToUpdate.getFirstRentalDay() : order.getFirstRentalDay());
        orderToUpdate.setLastRentalDay(order.getLastRentalDay() == null ? orderToUpdate.getLastRentalDay() : order.getLastRentalDay());
        orderToUpdate.setCustomerId(order.getCustomerId() == 0 ? orderToUpdate.getCustomerId() : order.getCustomerId());
        orderToUpdate.setCarId(tempCarId);
        orderToUpdate.setPrice(tempPrice);
        orderToUpdate.setNumberOfDays(tempNumOfDays);

        // Save, i.e. update existing order, with the new (and eventual old) values
        return orderRepository.save(orderToUpdate);
    }

    @Override
    public Order cancelOrder(Order order) {

        // Get order to update with id if exists, else get with order nr if that exists
        Optional<Order> foundById = orderRepository.findById(order.getId());
        Optional<Order> foundByOrderNr = orderRepository.findByOrderNr(order.getOrderNr());

        if (foundById.isEmpty() && foundByOrderNr.isEmpty()) {
            throw new RuntimeException("Order with id " + order.getId()
                    + " or order nr " + order.getOrderNr() + " not found");
        }

        // Then, if either id or order nr exists, get order by one of them
        Order orderToCancel = foundById.isPresent() ? orderRepository.findById(order.getId()).get() :
                orderRepository.findByOrderNr(order.getOrderNr()).get();

        // Update order with "cancelled" added to order nr field

        orderToCancel.setOrderNr("CANCELLED" + orderToCancel.getOrderNr()); // Update
        orderToCancel.setCanceled(order.isCanceled()); // Update
        orderToCancel.setOrderOrUpdateTime(LocalDateTime.now()); // Update

        // Keep old values, in case new values are passed in through postman etc
        orderToCancel.setFirstRentalDay(orderToCancel.getFirstRentalDay());
        orderToCancel.setLastRentalDay(orderToCancel.getLastRentalDay());
        orderToCancel.setCustomerId(orderToCancel.getCustomerId());
        orderToCancel.setCarId(orderToCancel.getCarId());
        orderToCancel.setPrice(orderToCancel.getPrice());
        orderToCancel.setNumberOfDays(orderToCancel.getNumberOfDays());
        orderToCancel.setPriceInEuro(orderToCancel.getPriceInEuro());

        // Save updated order as a cancelled version
        return orderRepository.save(orderToCancel);
    }

}
