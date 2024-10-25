package com.giodeminas.minibank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
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

  private String type;

  private String accountNumber;

  private String firstName;

  private String lastName;

  private String email;

  @Digits(integer = 15, fraction = 0, message = "Phone number must not exceed 15 digits!")
  private String phone;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private List<AddressDTO> addresses;

  public CustomerDTO(String accountNumber, String firstName, String lastName, String email,
      String phone) {
    this.accountNumber = accountNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public String customToString() {
    return firstName + ", " + lastName + ", " + email + ", " + phone;
  }
}
