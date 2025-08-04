package com.osho.CarRental.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "ssn", nullable = false)
    private String ssn; // Social Security Number

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    private String email; // Unique email used as username

    @Column(name = "first_name")
    private String fName;

    @Column(name = "last_name")
    private String lName;

    @Column(name = "address")
    private String address;

    // A customers can make multiple orders
    // (Refers to variable name customerId in Order class, not column name)
    @OneToMany(mappedBy = "customerId")
    private List<Order> ordersByCustomer = new ArrayList<>();

    // Empty constructor
    public Customer() {
    }

    // Constructor without ID
    public Customer(String ssn, LocalDate dateOfBirth, String email, String fName,
                    String lName, String address, List<Order> ordersByCustomer) {
        this.ssn = ssn;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.ordersByCustomer = ordersByCustomer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrdersByCustomer() {
        return ordersByCustomer;
    }

    public void setOrdersByCustomer(List<Order> ordersByCustomer) {
        this.ordersByCustomer = ordersByCustomer;
    }
}
