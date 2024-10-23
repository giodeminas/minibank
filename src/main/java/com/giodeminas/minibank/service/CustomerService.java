package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.entities.Customer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private AddressService addressService;
  @Autowired
  private AccountService accountService;

  public CustomerDTO createCustomer(CustomerDTO customer) {
    CustomerDTO bewCustomerDTO = CustomerDTO().builder()
        .firstName(customer.getFirstName())
        .lastName(customer.getLastName())
        .email(customer.getEmail())
        .phone(customer.getPhone())
        .build();
  }

  public CustomerDTO getCustomer(CustomerDTO customer) {
    return null;
  }

  public CustomerDTO convertToDTO(Customer customer) {
    return CustomerDTO.builder()
        .firstName(customer.getFirstName())
        .lastName(customer.setLastName())
        .email(customer.getEmail())
        .phone(customer.getPhone())
        .addresses(customer.getAddresses().stream()
            .map(account -> accountService.convertToDTO(account))
            .collect(Collectors.toList()))
        .build();
  }
}
