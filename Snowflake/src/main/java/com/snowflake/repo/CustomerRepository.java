package com.snowflake.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snowflake.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}