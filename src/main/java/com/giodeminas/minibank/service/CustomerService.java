package com.giodeminas.minibank.service;

import com.giodeminas.minibank.constants.CustomerType;
import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.dto.CustomerDTOPaginated;
import com.giodeminas.minibank.dto.PaginatedResponse;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.model.Address;
import com.giodeminas.minibank.model.Customer;
import com.giodeminas.minibank.repository.AccountRepository;
import com.giodeminas.minibank.repository.AddressRepository;
import com.giodeminas.minibank.repository.CustomerRepository;
import com.giodeminas.minibank.service.util.DTOConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private AddressRepository addressRepository;

  public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    Account account = accountRepository.findByAccountNumber(customerDTO.getAccountNumber())
        .orElseThrow(
            () -> new RuntimeException(
                "Couldn't find Account with the specified Account Number '"
                    + customerDTO.getAccountNumber() + "'."));
    Customer newCustomer = new Customer();
    try {
      if (customerDTO.getType() != null && !customerDTO.getType().isEmpty()) {
        newCustomer.setType(CustomerType.valueOf(customerDTO.getType()));
      }
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Couldn't find Customer Type '" + customerDTO.getType() + "'.");
    }
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
    try {
      accountRepository.save(account);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException(
          "Couldn't insert duplicate of Customer values '" + customerDTO.customToString() + "'.");
    }
    return DTOConverter.convertToDTO(newCustomer);
  }

  public CustomerDTO getCustomer(Long customerId) {
    return DTOConverter.convertToDTO(customerRepository.findById(customerId).orElseThrow(
        () -> new RuntimeException(
            "Couldn't find Customer with Customer Id '" + customerId + "'.")));
  }

  public PaginatedResponse<CustomerDTOPaginated> getCustomers(CustomerDTO customerDTO, int page,
      int size) {
    if (customerDTO != null) {
      PaginatedResponse<CustomerDTOPaginated> response = new PaginatedResponse<>();
      response.setQuery(formQueryForPage(customerDTO));
      Pageable pageable = PageRequest.of(page, size);

      Page<Customer> customerPage = customerRepository
          .searchCustomers(customerDTO.getType() != null && !customerDTO.getType().isEmpty()
                  ? CustomerType.valueOf(customerDTO.getType()) : null,
              customerDTO.getFirstName(),
              customerDTO.getLastName(),
              customerDTO.getPhone(),
              customerDTO.getEmail(),
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getHouse()
                  : null,
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getStreet()
                  : null,
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getCity()
                  : null,
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getState()
                  : null,
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getZip()
                  : null,
              customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()
                  ? customerDTO.getAddresses().get(0).getCountry()
                  : null,
              pageable);

      // Initialize the row number offset for the current page
      long rowNumberOffset = (long) customerPage.getNumber() * size;

      // Use AtomicLong to maintain a sequential row number
      AtomicLong rowCounter = new AtomicLong(rowNumberOffset);

      List<CustomerDTOPaginated> customerDTOResponses = customerPage.getContent().stream()
          .map(customer -> DTOConverter.convertToDTOResponse(
              rowCounter.incrementAndGet(), customer))
          .collect(Collectors.toList());
      response.setContent(customerDTOResponses);

      response.setTotalPages(customerPage.getTotalPages());
      response.setTotalElements(customerPage.getTotalElements());
      return response;
    } else {
      PaginatedResponse<CustomerDTOPaginated> response = new PaginatedResponse<>();
      Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));

      // Initialize the row number offset for the current page
      long rowNumberOffset = (long) customerPage.getNumber() * size;

      // Use AtomicLong to maintain a sequential row number
      AtomicLong rowCounter = new AtomicLong(rowNumberOffset);

      List<CustomerDTOPaginated> customerDTOResponses = customerPage.getContent().stream()
          .map(customer -> DTOConverter.convertToDTOResponse(
              rowCounter.incrementAndGet(), customer))
          .collect(Collectors.toList());
      response.setContent(customerDTOResponses);

      response.setTotalPages(customerPage.getTotalPages());
      response.setTotalElements(customerPage.getTotalElements());
      return response;
    }
  }

  private String formQueryForPage(CustomerDTO customerDTO) {
    if (customerDTO != null) {
      List<String> queryStrings = new ArrayList<>();
      if (customerDTO.getType() != null) {
        queryStrings.add("type=" + customerDTO.getType());
      }
      if (customerDTO.getFirstName() != null) {
        queryStrings.add("firstName=" + customerDTO.getFirstName());
      }
      if (customerDTO.getLastName() != null) {
        queryStrings.add("lastName=" + customerDTO.getLastName());
      }
      if (customerDTO.getPhone() != null) {
        queryStrings.add("phone=" + customerDTO.getPhone());
      }
      if (customerDTO.getEmail() != null) {
        queryStrings.add("email=" + customerDTO.getEmail());
      }
      if (customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()) {
        if (customerDTO.getAddresses().get(0).getHouse() != null) {
          queryStrings.add("house=" + customerDTO.getAddresses().get(0).getHouse());
        }
        if (customerDTO.getAddresses().get(0).getStreet() != null) {
          queryStrings.add("street=" + customerDTO.getAddresses().get(0).getStreet());
        }
        if (customerDTO.getAddresses().get(0).getCity() != null) {
          queryStrings.add("city=" + customerDTO.getAddresses().get(0).getCity());
        }
        if (customerDTO.getAddresses().get(0).getState() != null) {
          queryStrings.add("state=" + customerDTO.getAddresses().get(0).getState());
        }
        if (customerDTO.getAddresses().get(0).getZip() != null) {
          queryStrings.add("zip=" + customerDTO.getAddresses().get(0).getZip());
        }
        if (customerDTO.getAddresses().get(0).getCountry() != null) {
          queryStrings.add("country=" + customerDTO.getAddresses().get(0).getCountry());
        }
      }
      return String.join(", ", queryStrings);
    } else {
      return null;
    }
  }

  public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(
            () -> new RuntimeException(
                "Couldn't find Customer with specified Customer Id '" + customerId + "'."));
    CustomerDTO oldCustomerDTO = DTOConverter.convertToDTO(customer);
    if (customerDTO.getAccountNumber() != null) {
      Account oldAccount = accountRepository.findByAccountNumber(
              oldCustomerDTO.getAccountNumber())
          .orElseThrow(() -> new RuntimeException(
              "Couldn't find Account with the specified Account Number '"
                  + oldCustomerDTO.getAccountNumber() + "'."));
      Account newAccount = accountRepository.findByAccountNumber(customerDTO.getAccountNumber())
          .orElseThrow(() -> new RuntimeException(
              "Couldn't find Account with the specified Account Number '"
                  + customerDTO.getAccountNumber() + "'."));
      oldAccount.removeOwner(customer);
      newAccount.addOwner(customer);
      accountRepository.save(oldAccount);
      accountRepository.save(newAccount);
    }
    if (customerDTO.getType() != null) {
      customer.setType(CustomerType.valueOf(customerDTO.getType()));
    }
    if (customerDTO.getFirstName() != null) {
      customer.setFirstName(customerDTO.getFirstName());
    }
    if (customerDTO.getLastName() != null) {
      customer.setLastName(customerDTO.getLastName());
    }
    if (customerDTO.getPhone() != null) {
      customer.setPhone(customerDTO.getPhone());
    }
    if (customerDTO.getEmail() != null) {
      customer.setEmail(customerDTO.getEmail());
    }
    if (customerDTO.getAddresses() != null && !customerDTO.getAddresses().isEmpty()) {
      for (AddressDTO addressDTO : customerDTO.getAddresses()) {
        if (addressDTO.getAddressId() != null) {
          Address oldAddress = addressRepository.findById(addressDTO.getAddressId())
              .orElseThrow(() ->
                  new RuntimeException(
                      "Couldn't find address with Address Id '" + addressDTO.getAddressId()
                          + "'."));
          if (addressDTO.getHouse() != null) {
            oldAddress.setHouse(addressDTO.getHouse());
          }
          if (addressDTO.getStreet() != null) {
            oldAddress.setStreet(addressDTO.getStreet());
          }
          if (addressDTO.getCity() != null) {
            oldAddress.setCity(addressDTO.getCity());
          }
          if (addressDTO.getState() != null) {
            oldAddress.setState(addressDTO.getState());
          }
          if (addressDTO.getZip() != null) {
            oldAddress.setZip(addressDTO.getZip());
          }
          if (addressDTO.getCountry() != null) {
            oldAddress.setCountry(addressDTO.getCountry());
          }
        } else {
          Address newAddress = new Address();
          newAddress.setCustomer(customer);
          newAddress.setHouse(addressDTO.getHouse());
          newAddress.setStreet(addressDTO.getStreet());
          newAddress.setCity(addressDTO.getCity());
          newAddress.setState(addressDTO.getState());
          newAddress.setZip(addressDTO.getZip());
          newAddress.setCountry(addressDTO.getCountry());
          customer.getAddresses().add(newAddress);
        }
      }
    }
    customerRepository.save(customer);
    return DTOConverter.convertToDTO(customer);
  }
}
