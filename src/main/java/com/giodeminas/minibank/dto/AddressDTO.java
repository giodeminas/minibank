package com.giodeminas.minibank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

  private String house;

  private String street;

  private String city;

  private String state;

  private String zip;

  private String country;
}
