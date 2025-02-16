package com.snowflake.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snowflake.entity.Customer;
import com.snowflake.exception.CustomerNotFoundException;
import com.snowflake.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	// Get all customers
	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		logger.info("Fetching all customers");
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	// Get customer by ID
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
		logger.info("Fetching customer with ID: {}", id);
		Customer customer = customerService.getCustomerById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
		return ResponseEntity.ok(customer);
	}

	// Create new customer
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		logger.info("Creating new customer: {}", customer.getCustomerId());
		return ResponseEntity.ok(customerService.saveCustomer(customer));
	}

	// Update existing customer
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer updatedCustomer) {
		logger.info("Updating customer with ID: {}", id);
		if (!customerService.getCustomerById(id).isPresent()) {
			throw new CustomerNotFoundException("Customer not found with ID: " + id);
		}
		updatedCustomer.setCustomerId(id);
		return ResponseEntity.ok(customerService.saveCustomer(updatedCustomer));
	}

	// Soft delete customer (update status to INACTIVE)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> softDeleteCustomer(@PathVariable String id) {
		logger.info("Soft deleting customer with ID: {}", id);
		if (!customerService.getCustomerById(id).isPresent()) {
			throw new CustomerNotFoundException("Customer not found with ID: " + id);
		}
		customerService.softDeleteCustomer(id);
		return ResponseEntity.noContent().build();
	}
}