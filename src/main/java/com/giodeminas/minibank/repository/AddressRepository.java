package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.entities.Address;
import com.giodeminas.minibank.entities.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

  public List<Address> findByCustomer(Customer customer);
}
