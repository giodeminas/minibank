package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
