package com.giodeminas.minibank.model;

import com.giodeminas.minibank.constants.CustomerType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"firstName", "lastName", "phone",
        "email"})})
public class Customer extends MiniBankRecord {

  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  @Column
  private CustomerType type;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String phone;

  @Column
  private String email;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Address> addresses;

  public Customer(Account account, CustomerType type, String firstName, String lastName,
      String phone, String email) {
    this.account = account;
    this.type = type;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
  }
}
