package com.giodeminas.minibank.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address extends MiniBankRecord {

  @ManyToOne
  private Customer customer;

  private String house;

  private String street;

  private String city;

  private String state;

  private String zip;

  private String country;
}
