package com.giodeminas.minibank.controller;

import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.dto.CustomerDTOPaginated;
import com.giodeminas.minibank.dto.PaginatedResponse;
import com.giodeminas.minibank.service.CustomerService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping
  public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
    try {
      CustomerDTO newCustomerDTO = customerService.createCustomer(customerDTO);
      if (newCustomerDTO.getAccountNumber() != null) {
        return new ResponseEntity<>(
            "New Customer named '" + newCustomerDTO.getFirstName() + " "
                + newCustomerDTO.getLastName()
                + "' for Account '" + newCustomerDTO.getAccountNumber()
                + "' created successfully!",
            HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>(
            "New Customer named '" + newCustomerDTO.getFirstName() + " "
                + newCustomerDTO.getLastName() + " created successfully!", HttpStatus.CREATED);
      }
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<Object> getCustomer(@PathVariable Long customerId) {
    try {
      return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<PaginatedResponse<CustomerDTOPaginated>> getCustomers(
      @RequestBody(required = false) CustomerDTO customerDTO,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {
    return new ResponseEntity<>(customerService.getCustomers(customerDTO, page, size),
        HttpStatus.OK);
  }

  @PutMapping("/{customerId}")
  public ResponseEntity<String> updateCustomer(@PathVariable Long customerId,
      @Valid @RequestBody CustomerDTO customerDTO) {
    try {
      if (customerId != null) {
        CustomerDTO oldCustomerDTO = customerService.getCustomer(customerId);
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(customerId, customerDTO);
        List<String> changes = new ArrayList<>();
        if (!oldCustomerDTO.getType().equals(updatedCustomerDTO.getType())) {
          changes.add("Type from '" + oldCustomerDTO.getType() + "' to '"
              + updatedCustomerDTO.getType() + "'. ");
        }
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

        if (updatedCustomerDTO.getAddresses() != null && !updatedCustomerDTO.getAddresses()
            .isEmpty()) {
          for (AddressDTO addressDTO : updatedCustomerDTO.getAddresses()) {
            if (oldCustomerDTO.getAddresses() == null) {
              changes.add(
                  "Inserted new Address '" + addressDTO.customToString() + "' with Address Id '"
                      + addressDTO.getAddressId() + "'.");
            } else if (oldCustomerDTO.getAddresses().size() != updatedCustomerDTO.getAddresses()
                .size()) {
              changes.add(
                  "Inserted new Address '" + addressDTO.customToString() + "' with Address Id '"
                      + addressDTO.getAddressId() + "'.");
            } else {
              for (AddressDTO oldAddress : oldCustomerDTO.getAddresses()) {
                if (addressDTO.getAddressId().equals(oldAddress.getAddressId())) {
                  if (!oldAddress.getHouse().equals(addressDTO.getHouse())) {
                    changes.add("Updated House from '" + oldAddress.getHouse() +
                        "' to '" + addressDTO.getHouse()
                        + "' for Address Id '" + addressDTO.getAddressId() + "'."
                    );
                  }
                  if (!oldAddress.getStreet().equals(addressDTO.getStreet())) {
                    changes.add("Updated Street from '" + oldAddress.getStreet() + "' to '"
                        + addressDTO.getStreet() + " for Address Id '" + oldAddress.getAddressId()
                        + "'."
                    );
                  }
                  if (!oldAddress.getCity().equals(addressDTO.getCity())) {
                    changes.add(
                        "Updated City from '" + oldAddress.getCity() + "' to '"
                            + addressDTO.getCity()
                            + "' for Address Id '" + addressDTO.getAddressId() + "'."
                    );
                  }
                  if (!oldAddress.getState().equals(addressDTO.getState())) {
                    changes.add(
                        "Updated State from '" + oldAddress.getState() + "' to '"
                            + addressDTO.getState()
                            + "' for Address Id '" + addressDTO.getAddressId() + "'."
                    );
                  }
                  if (!oldAddress.getZip().equals(addressDTO.getZip())) {
                    changes.add(
                        "Updated Zip from '" + oldAddress.getZip() + "' to '" + addressDTO.getZip()
                            + "' for Address Id '" + addressDTO.getAddressId() + "'."
                    );
                  }
                  if (!oldAddress.getCountry().equals(addressDTO.getCountry())) {
                    changes.add(
                        "Updated Country from '" + oldAddress.getCountry() + "' to '"
                            + addressDTO.getCountry()
                            + "' for Address Id '" + addressDTO.getAddressId() + "'."
                    );
                  }
                }
              }
            }
          }
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
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
