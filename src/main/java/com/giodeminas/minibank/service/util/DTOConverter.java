package com.giodeminas.minibank.service.util;

import com.giodeminas.minibank.dto.AccountDTO;
import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.model.Address;
import com.giodeminas.minibank.model.Customer;
import java.util.stream.Collectors;

public class DTOConverter {

  public static AccountDTO convertToDTO(Account account) {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setAccountNumber(account.getAccountNumber());
    accountDTO.setBalance(account.getBalance());
    accountDTO.setOwners(account.getOwners().stream()
        .map(customer -> DTOConverter.convertToDTO(customer))
        .collect(Collectors.toList()));
    accountDTO.setNumberOfOwners(account.getNumberOfOwners());
    return accountDTO;
  }

  public static CustomerDTO convertToDTO(Customer customer) {
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setCustomerId(customer.getId());
    customerDTO.setAccountNumber(customer.getAccount().getAccountNumber());
    customerDTO.setFirstName(customer.getFirstName());
    customerDTO.setLastName(customer.getLastName());
    customerDTO.setPhone(customer.getPhone());
    customerDTO.setEmail(customer.getEmail());
    if (customer.getAddresses() != null && !customer.getAddresses().isEmpty()) {
      customerDTO.setAddresses(customer.getAddresses().stream()
          .map(address -> DTOConverter.convertToDTO(address))
          .collect(Collectors.toList()));
    }
    return customerDTO;
  }

  public static AddressDTO convertToDTO(Address address) {
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
