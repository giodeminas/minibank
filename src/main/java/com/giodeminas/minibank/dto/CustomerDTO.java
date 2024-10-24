package com.giodeminas.minibank.dto;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "phone"})
})
public class CustomerDTO {

  private Long customerId;

  private String accountNumber;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;

  private List<AddressDTO> addresses;

  public CustomerDTO(String accountNumber, String firstName, String lastName, String email, String phone) {
    this.accountNumber = accountNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }
}
