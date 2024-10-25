package com.giodeminas.minibank.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Account extends MiniBankRecord {

  @Column(unique = true)
  private String accountNumber;

  @Column
  private double balance;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Customer> owners;

  @Column
  private int numberOfOwners;

  public Account(String accountNumber) {
    this.accountNumber = accountNumber;
    this.balance = 0;
    this.owners = new ArrayList<>();
    this.numberOfOwners = 0;
  }

  public Account(String accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.owners = new ArrayList<>();
    this.numberOfOwners = 0;
  }

  public Account(String accountNumber, double balance, List<Customer> owners) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.owners = owners;
    this.numberOfOwners = owners.size();
  }

  public void addOwner(Customer owner) {
    owner.setAccount(this);
    owners.add(owner);
    numberOfOwners = owners.size();
  }

  public void removeOwner(Customer owner) {
    owner.setAccount(null);
    owners.remove(owner);
    numberOfOwners = owners.size();
  }
}
