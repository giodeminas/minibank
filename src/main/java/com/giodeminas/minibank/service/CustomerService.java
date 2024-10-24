package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.dto.PageinatedResponse;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.model.Address;
import com.giodeminas.minibank.model.Customer;
import com.giodeminas.minibank.repository.AccountRepository;
import com.giodeminas.minibank.repository.CustomerRepository;
import com.giodeminas.minibank.service.util.DTOConverter;
import com.giodeminas.minibank.specification.CustomerSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CustomerRepository customerRepository;

  public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    Account account = accountRepository.findByAccountNumber(customerDTO.getAccountNumber()).orElseThrow(
        () -> new RuntimeException(
            "Couldn't find Account with the specified Account Number '"
                + customerDTO.getAccountNumber() + "'."));
    Customer newCustomer = new Customer();
    newCustomer.setFirstName(customerDTO.getFirstName());
    newCustomer.setLastName(customerDTO.getLastName());
    newCustomer.setPhone(customerDTO.getPhone());
    newCustomer.setEmail(customerDTO.getEmail());
    if (customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()) {
      List<Address> newAddresses = new ArrayList<>();
      for (AddressDTO addressDTO : customerDTO.getAddresses()) {
        Address address = new Address();
        address.setCustomer(newCustomer);
        address.setHouse(addressDTO.getHouse());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZip(addressDTO.getZip());
        address.setCountry(addressDTO.getCountry());
        newAddresses.add(address);
      }
      newCustomer.setAddresses(newAddresses);
    }
    account.addOwner(newCustomer);
    accountRepository.save(account);
    return DTOConverter.convertToDTO(newCustomer);
  }

  public CustomerDTO getCustomer(Long customerId) {
    return DTOConverter.convertToDTO(customerRepository.findByIdWithAddresses(customerId).orElseThrow(
        () -> new RuntimeException(
            "Couldn't find Customer with Customer Id '" + customerId + "'.")));
  }

  public PageinatedResponse<CustomerDTO> getCustomers(CustomerDTO customerDTO, int page, int size) {
    if (customerDTO != null) {
      PageinatedResponse<CustomerDTO> response = new PageinatedResponse<>();
      Specification<Customer> specification = CustomerSpecification.withFilters(customerDTO);
      Page<Customer> customerPage = customerRepository.findAll(specification, PageRequest.of(page, size));
      response.setContent(customerPage.stream()
          .map(customer -> DTOConverter.convertToDTO(customer))
          .collect(Collectors.toList()));
      response.setTotalPages(customerPage.getTotalPages());
      response.setTotalElements(customerPage.getTotalElements());
      return response;
    } else {
      PageinatedResponse<CustomerDTO> response = new PageinatedResponse<>();
      Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));
      response.setContent(customerPage.stream()
          .map(DTOConverter::convertToDTO)
          .collect(Collectors.toList()));
      response.setTotalPages(customerPage.getTotalPages());
      response.setTotalElements(customerPage.getTotalElements());
      return response;
    }
  }

  public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(
            () -> new RuntimeException(
                "Couldn't find Customer with specified Customer Id '" + customerId + "'."));
    CustomerDTO oldCustomerDTO = DTOConverter.convertToDTO(customer);
    if (oldCustomerDTO != customerDTO) {
      if (!oldCustomerDTO.getAccountNumber().equals(customerDTO.getAccountNumber())) {
        Account oldAccount = accountRepository.findByAccountNumber(oldCustomerDTO.getAccountNumber())
            .orElseThrow(() -> new RuntimeException(
                "Couldn't find Account with the specified Account Number '"
                    + oldCustomerDTO.getAccountNumber() + "'."));
        oldAccount.removeOwner(customer);
        accountRepository.save(oldAccount);
        Account newAccount = accountRepository.findByAccountNumber(customerDTO.getAccountNumber())
            .orElseThrow(() -> new RuntimeException(
                "Couldn't find Account with the specified Account Number '"
                    + customerDTO.getAccountNumber() + "'."));
        newAccount.addOwner(customer);
        accountRepository.save(newAccount);
      }
      if (!oldCustomerDTO.getFirstName().equals(customerDTO.getFirstName())) {
        customer.setFirstName(customerDTO.getFirstName());
      }
      if (!oldCustomerDTO.getLastName().equals(customerDTO.getLastName())) {
        customer.setLastName(customerDTO.getLastName());
      }
      if (!oldCustomerDTO.getPhone().equals(customerDTO.getPhone())) {
        customer.setPhone(customerDTO.getPhone());
      }
      if (!oldCustomerDTO.getEmail().equals(customerDTO.getEmail())) {
        customer.setEmail(customerDTO.getEmail());
      }
    }
    customerRepository.save(customer);
    return DTOConverter.convertToDTO(customer);
  }
}
