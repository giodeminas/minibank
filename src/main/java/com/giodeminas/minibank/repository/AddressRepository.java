package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
