package com.giodeminas.minibank.controller;

import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.dto.PageinatedResponse;
import com.giodeminas.minibank.service.CustomerService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping
  public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerDTO) {
    // TODO use CustomerType
    CustomerDTO newCustomerDTO = customerService.createCustomer(customerDTO);
    if (newCustomerDTO != null) {
      if (newCustomerDTO.getAccountNumber() != null) {
        return new ResponseEntity<>(
            "New customer named '" + newCustomerDTO.getFirstName() + " "
                + newCustomerDTO.getLastName()
                + "' for account '" + newCustomerDTO.getAccountNumber() + "' created successfully!",
            HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>(
            "New customer named '" + newCustomerDTO.getFirstName() + " "
                + newCustomerDTO.getLastName() + " created successfully!", HttpStatus.CREATED);
      }
    } else {
      return new ResponseEntity<>("Failed to create a new account.", HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long customerId) {
    return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<PageinatedResponse<CustomerDTO>> getCustomers(
      @RequestBody(required = false) CustomerDTO customerDTO,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return new ResponseEntity<>(customerService.getCustomers(customerDTO, page, size),
        HttpStatus.OK);
  }

  @PutMapping("/{customerId}")
  public ResponseEntity<String> updateCustomer(@PathVariable Long customerId,
      @RequestBody CustomerDTO customerDTO) {
    if (customerId != null) {
      CustomerDTO oldCustomerDTO = customerService.getCustomer(customerId);
      CustomerDTO updatedCustomerDTO = customerService.updateCustomer(customerId, customerDTO);
      List<String> changes = new ArrayList<>();
      if (!oldCustomerDTO.getAccountNumber().equals(updatedCustomerDTO.getAccountNumber())) {
        changes.add("Account from '" + oldCustomerDTO.getAccountNumber() + "' to '"
            + updatedCustomerDTO.getAccountNumber() + "'. ");
      }
      if (!oldCustomerDTO.getFirstName().equals(updatedCustomerDTO.getFirstName())) {
        changes.add("First Name from '" + oldCustomerDTO.getFirstName() + "' to '"
            + updatedCustomerDTO.getFirstName() + "'. ");
      }
      if (!oldCustomerDTO.getLastName().equals(updatedCustomerDTO.getLastName())) {
        changes.add("Last Name from '" + oldCustomerDTO.getLastName() + "' to '"
            + updatedCustomerDTO.getLastName() + "'. ");
      }
      if (!oldCustomerDTO.getPhone().equals(updatedCustomerDTO.getPhone())) {
        changes.add(
            "Phone from '" + oldCustomerDTO.getPhone() + "' to '" + updatedCustomerDTO.getPhone()
                + "'. ");
      }
      if (!oldCustomerDTO.getEmail().equals(updatedCustomerDTO.getEmail())) {
        changes.add(
            "Email from '" + oldCustomerDTO.getEmail() + "' to '" + updatedCustomerDTO.getEmail()
                + "'. ");
      }

      String updateMessage;
      if (!changes.isEmpty()) {
        updateMessage = "Customer with Customer Id '" + customerId
            + "' updated successfully! Here are the changes: \n" + String.join("\n", changes);
      } else {
        updateMessage = "Customer with Customer Id '" + customerId + "' didn't need updating!";
      }

      return new ResponseEntity<>(updateMessage, HttpStatus.OK);

    } else {
      return new ResponseEntity<>
          ("Customer Id is required to update Customer info.", HttpStatus.BAD_REQUEST);
    }
  }
}
