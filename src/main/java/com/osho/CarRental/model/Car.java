package com.osho.CarRental.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private int id;

    @Column(name = "regnr", nullable = false)
    private String regNr; // BMW, Audi etc.

    @Column(name = "model")
    private String model; // BMW, Audi etc.

    @Column(name = "type")
    private String type;

    @Column(name = "model_year")
    private int modelYear;

    @Column(name = "daily_sek")
    private double dailySek; // SEK by default

    // A car can be in many orders, and vice versa
    // (Refers to variable name carId in Order class, not column name)
    @OneToMany(mappedBy = "carId", cascade = CascadeType.PERSIST)
    private List<Order> ordersOfCar = new ArrayList<>();

    // Empty constructor
    public Car() {
    }

    // Constructor without ID
    public Car(String regNr, String model, String type, int modelYear, double dailySek, List<Order> ordersOfCar) {
        this.regNr = regNr;
        this.model = model;
        this.type = type;
        this.modelYear = modelYear;
        this.dailySek = dailySek;
        this.ordersOfCar = ordersOfCar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegNr() {
        return regNr;
    }

    public void setRegNr(String regNr) {
        this.regNr = regNr;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int registrationNr) {
        this.modelYear = registrationNr;
    }

    public double getDailySek() {
        return dailySek;
    }

    public void setDailySek(double dailySek) {
        this.dailySek = dailySek;
    }

    public List<Order> getOrdersOfCar() {
        return ordersOfCar;
    }

    public void setOrdersOfCar(List<Order> ordersOfCar) {
        this.ordersOfCar = ordersOfCar;
    }
}
