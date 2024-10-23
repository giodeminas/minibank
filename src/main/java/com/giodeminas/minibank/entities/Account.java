package com.giodeminas.minibank.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Account extends MiniBankRecord {

  @Column(unique = true)
  private String accountNumber;

  private double balance;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
  private List<Customer> owners = new ArrayList<Customer>();

  private int numberOfOwners;

  public Account(String accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.numberOfOwners = 0;
  }

  public Account(String accountNumber, double balance, List<Customer> owners) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.owners = owners;
    this.numberOfOwners = owners.size();
  }

  public void addOwner(Customer owner) {
    owners.add(owner);
    numberOfOwners = owners.size();
  }

  public void removeOwner(Customer owner) {
    owners.remove(owner);
    numberOfOwners = owners.size();
  }
}
