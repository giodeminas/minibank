package com.giodeminas.minibank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address", uniqueConstraints =
    {@UniqueConstraint(columnNames = {"customer_id", "house", "street", "city", "state", "zip",
        "country"})})
public class Address extends MiniBankRecord {

  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @Column
  private String house;

  @Column
  private String street;

  @Column
  private String city;

  @Column
  private String state;

  @Column
  private String zip;

  @Column
  private String country;
}
