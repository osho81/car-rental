package com.osho.CarRental.service.repository;

import com.osho.CarRental.model.Car;
import java.util.List;

public interface CarServiceRepository {

    List<Car> getAllCars();
    Car addCar(Car car);
    Car updateCar(Car car);
    void deleteCar(Car car);

}
