package com.giodeminas.minibank.dto;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

  private Long addressId;

  private String house;

  private String street;

  private String city;

  private String state;

  @Digits(integer = 5, fraction = 0, message = "Zip must not exceed 5 digits!")
  private String zip;

  private String country;

  public String customToString() {
    return house + ", " + street + ", " + city + ", " + state + ", " + zip + ", " + country;
  }
}
