package com.osho.carrental.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // "order" singular is reserved by sql
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int id;

    @Column(name = "order_nr")
    private String orderNr;

    @Column(name = "canceled")
    private boolean canceled;

    @Column(name = "order_date")
    private LocalDateTime orderOrUpdateTime;

    @Column(name = "first_rental_day")
    private LocalDate firstRentalDay;

    @Column(name = "last_rental_day")
    private LocalDate lastRentalDay;

    @Column(name = "customer_id")
    private int customerId; // One customer per order

    @Column(name = "car_id")
    private int carId;
    @Column(name = "price")
    private double price;

    @Column(name = "num_of_days")
    private int numberOfDays;

    @Column(name = "price_in_eur")
    private double priceInEuro;

    // Empty constructor
    public Order() {
    }

    // Constructor without ID
    public Order(String orderNr, boolean canceled, LocalDateTime orderOrUpdateTime, LocalDate firstRentalDay, LocalDate lastRentalDay,
                 int customerId, int carId, double price, int numberOfDays, double priceInEuro) {
        this.orderNr = orderNr;
        this.canceled = canceled; // Alternatively set it to false here in the constructor
        this.orderOrUpdateTime = orderOrUpdateTime.withNano(0); // Remove nano seconds
        this.firstRentalDay = firstRentalDay;
        this.lastRentalDay = lastRentalDay;
        this.customerId = customerId;
        this.carId = carId;
        this.price = price;
        this.numberOfDays = numberOfDays;
        this.priceInEuro = priceInEuro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(String orderNr) {
        this.orderNr = orderNr;
    }

    public LocalDateTime getOrderOrUpdateTime() {
        return orderOrUpdateTime;
    }

    public void setOrderOrUpdateTime(LocalDateTime orderOrUpdateTime) {
        this.orderOrUpdateTime = orderOrUpdateTime.withNano(0); // Remove nano seconds
    }

    public LocalDate getFirstRentalDay() {
        return firstRentalDay;
    }

    public void setFirstRentalDay(LocalDate firstRentalDay) {
        this.firstRentalDay = firstRentalDay;
    }

    public LocalDate getLastRentalDay() {
        return lastRentalDay;
    }

    public void setLastRentalDay(LocalDate lastRentalDay) {
        this.lastRentalDay = lastRentalDay;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getPriceInEuro() {
        return priceInEuro;
    }

    public void setPriceInEuro(double priceInEuro) {
        this.priceInEuro = priceInEuro;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
