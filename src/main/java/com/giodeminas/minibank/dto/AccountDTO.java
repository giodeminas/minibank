package com.giodeminas.minibank.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

  private String accountNumber;

  private double balance;

  private List<CustomerDTO> owners;

  private int numberOfOwners;

  public AccountDTO(String accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
  }
}
