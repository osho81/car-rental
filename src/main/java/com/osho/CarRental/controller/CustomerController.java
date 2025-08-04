package com.osho.CarRental.controller;

import com.osho.CarRental.model.Customer;
import com.osho.CarRental.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //------------------------- PROJECT REQUIREMENTS ------------------------//

    // Requirement: "Lista kunder GET /api/v1/customers"
    @GetMapping("/customers") // USER ROLE REQUIRED
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<List<Customer>>(customerService.getAllCustomers(), HttpStatus.OK);
    }


    //---------------------- NOT PROJECT REQUIREMENTS -----------------------//

    @PostMapping("/addcustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<Customer>(customerService.addCustomer(customer), HttpStatus.CREATED);
    }

    @DeleteMapping("/deletecustomer")
    public ResponseEntity<String> deleteOrder(@RequestBody Customer customer) {
        customerService.deleteCustomer(customer);
        return new ResponseEntity<String>("Customer with " +
                (customer.getId() == 0 ? "email " : "with id " + customer.getId() + " and/or email ") +
                customer.getEmail() + " deleted.", HttpStatus.OK);
    }


}
