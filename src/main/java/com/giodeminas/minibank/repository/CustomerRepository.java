package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
    JpaSpecificationExecutor<Customer>, PagingAndSortingRepository<Customer, Long> {

  @Query("SELECT c FROM Customer c JOIN FETCH c.addresses WHERE c.id = :id")
  Optional<Customer> findByIdWithAddresses(@Param("id") Long id);
}
