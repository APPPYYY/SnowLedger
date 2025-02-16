package com.snowflake.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.snowflake.entity.Customer;
import com.snowflake.exception.CustomerNotFoundException;
import com.snowflake.repo.CustomerRepository;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        logger.info("Retrieving all customers");
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        logger.info("Retrieving customer with ID: {}", id);
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        logger.info("Saving customer: {}", customer.getCustomerId());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        logger.info("Deleting customer with ID: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    public void softDeleteCustomer(String id) {
        logger.info("Soft deleting customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customer.setAccountStatus("INACTIVE");
        customerRepository.save(customer);
    }
    
    public void generateDemoData(int count) {
        logger.info("Generating {} demo customer records", count);
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Customer customer = new Customer();
            customer.setCustomerId(UUID.randomUUID().toString());
            customer.setCustomerName("Demo Customer " + i);
            customer.setEmail("demo" + i + "@example.com");
            customer.setPhoneNumber("987654" + (1000 + i));
            customer.setAddress("123 Demo Street, City " + i);
            customer.setTotalOrders((int) (Math.random() * 100));
            customer.setLastOrderDate(Instant.now());
            customer.setAccountStatus("ACTIVE");
            customers.add(customer);
        }
        customerRepository.saveAll(customers);
        logger.info("Successfully inserted {} demo records", count);
    }
}