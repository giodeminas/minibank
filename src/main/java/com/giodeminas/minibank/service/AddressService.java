package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.entities.Address;
import com.giodeminas.minibank.entities.Customer;
import com.giodeminas.minibank.repository.AddressRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  public List<AddressDTO> getAddresses(Customer customer) {
    return addressRepository.findByCustomer(customer).stream()
        .map(address -> convertToDTO(address))
        .collect(Collectors.toList());
  }

  public AddressDTO convertToDTO(Address address) {
    AddressDTO addressDTO = new AddressDTO();
    addressDTO.setHouse(address.getHouse());
    addressDTO.setStreet(address.getStreet());
    addressDTO.setCity(address.getCity());
    addressDTO.setState(address.getState());
    addressDTO.setZip(address.getZip());
    addressDTO.setCountry(address.getCountry());
    return addressDTO;
  }
}
