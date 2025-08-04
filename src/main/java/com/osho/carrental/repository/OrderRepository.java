package com.osho.carrental.repository;

import com.osho.carrental.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Customized method
    Optional<Order> findByOrderNr(String orderNr);
}
