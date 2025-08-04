package com.osho.CarRental.repository;

import com.osho.CarRental.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Customized method
    Optional<Order> findByOrderNr(String orderNr);
}
