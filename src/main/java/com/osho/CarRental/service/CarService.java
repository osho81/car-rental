package com.osho.CarRental.service;

import com.osho.CarRental.model.Car;
import com.osho.CarRental.model.Order;
import com.osho.CarRental.repository.CarRepository;
import com.osho.CarRental.repository.OrderRepository;
import com.osho.CarRental.service.repository.CarServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarService implements CarServiceRepository {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car addCar(Car car) {
        Optional<Car> foundByRegNr = carRepository.findByRegNr(car.getRegNr());
        if (foundByRegNr.isPresent()) { // Check if regNr is occupied
            throw new RuntimeException(car.getRegNr() + " already in our fleet.");
        } else {
            System.out.println(car.getModel() + " with reg. nr " + car.getRegNr() + " is added.");
            return carRepository.save(car);
        }
    }

    public Car updateCar(Car car) {

        // Get car to update with id if exists, else get with reg.nr if that exists
        Optional<Car> foundById = carRepository.findById(car.getId());
        Optional<Car> foundByRegNr = carRepository.findByRegNr(car.getRegNr());
        if (foundById.isEmpty() && foundByRegNr.isEmpty()) {
            throw new RuntimeException("Car with id " + car.getId()
                    + " or reg. nr " + car.getRegNr() + " not found");
        }

        // Then, if either id or reg.nr exists, get car by one of them
        Car carToUpdate = foundById.isPresent() ? carRepository.findById(car.getId()).get() :
                carRepository.findByRegNr(car.getRegNr()).get();

        // Add new value to each column, but keep old value if new is null/empty
        carToUpdate.setRegNr(car.getRegNr() == null ? carToUpdate.getRegNr() : car.getRegNr());
        carToUpdate.setModel(car.getModel() == null ? carToUpdate.getModel() : car.getModel());
        carToUpdate.setType(car.getType() == null ? carToUpdate.getType() : car.getType());

        // Convert local-date year to (sub)string, capture year, then to parse to int: model year is max next year
        carToUpdate.setModelYear(car.getModelYear() > Integer.parseInt(LocalDate.now().toString().substring(0,4))+1 ||
                car.getModelYear() == 0 ? carToUpdate.getModelYear() : car.getModelYear());
        carToUpdate.setDailySek(car.getDailySek() == 0 ? carToUpdate.getDailySek() : car.getDailySek());

        // If car is not part of any order, set old value
        if (car.getOrdersOfCar().isEmpty() || car.getOrdersOfCar() == null) {
            carToUpdate.setOrdersOfCar(carToUpdate.getOrdersOfCar());
        } else {
            carToUpdate.setOrdersOfCar(car.getOrdersOfCar());
        }

        // Save, i.e. update existing car, with new (and eventual old) passed in values
        return carRepository.save(carToUpdate);
    }

    @Override
    public void deleteCar(Car car) {
        // Get car to delete with id if exists, else get with reg.nr if that exists
        Optional<Car> foundById = carRepository.findById(car.getId());
        Optional<Car> foundByRegNr = carRepository.findByRegNr(car.getRegNr());
        if (foundById.isEmpty() && foundByRegNr.isEmpty()) {
            throw new RuntimeException("Car with id " + car.getId()
                    + " or reg. nr " + car.getRegNr() + " not found");
        }

        // Then, if either id or reg.nr exists, get car by one of them
        Car carToDelete = foundById.isPresent() ? carRepository.findById(car.getId()).get() :
                carRepository.findByRegNr(car.getRegNr()).get();

        // Update car before delete; remove all relations to any "order-children"
        Car disassociatedCar = disassociateOrders(carToDelete.getId());
        carRepository.delete(disassociatedCar);
    }

    // Manually removing relation the car's related orders before removing the car
    // Otherwise a false reference to a removed car will remain in related order(s)
    private Car disassociateOrders(int id) {
        Car carToSaveAnReturn = carRepository.findById(id).get();
        List<Order> associatedOrders = carToSaveAnReturn.getOrdersOfCar();
        for (Order order : associatedOrders) {
            Order orderToDisassociate = orderRepository.findById(order.getId()).get();
            orderToDisassociate.setCarId(0); // 0 to show order has no car anymore
            orderRepository.save(orderToDisassociate);
        }
        return carRepository.save(carToSaveAnReturn); // Save update, & return
    }

}
