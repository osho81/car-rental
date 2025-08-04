package com.osho.carrental.repository;

import com.osho.carrental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {

    // Customized method
    Optional<Car> findByRegNr(String regNr);

}
