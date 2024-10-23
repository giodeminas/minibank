package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  public Account findByAccountNumber(String accountNumber);
}
