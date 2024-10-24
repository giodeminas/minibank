package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.model.Customer;
import com.giodeminas.minibank.repository.AddressRepository;
import com.giodeminas.minibank.service.util.DTOConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  /*public List<AddressDTO> getAddresses(Customer customer) {
    return addressRepository.findAllByCustomer(customer).stream()
        .map(DTOConverter::convertToDTO)
        .collect(Collectors.toList());
  }*/


}
