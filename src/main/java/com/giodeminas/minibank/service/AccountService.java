package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.AccountDTO;
import com.giodeminas.minibank.entities.Account;
import com.giodeminas.minibank.repository.AccountRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CustomerService customerService;

  public AccountDTO createAccount(AccountDTO accountDTO) {
    Account newAccount = new Account(accountDTO.getAccountNumber(), accountDTO.getBalance());
    return convertToDTO(accountRepository.save(newAccount));
  }

  public List<AccountDTO> getAllAccounts() {
    return accountRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public Account getAccount(String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  public AccountDTO convertToDTO(Account account) {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setAccountNumber(account.getAccountNumber());
    accountDTO.setBalance(account.getBalance());
    accountDTO.setOwners(account.getOwners().stream()
        .map(customer -> customerService.convertToDTO(customer))
        .collect(Collectors.toList()));
    return accountDTO;
  }
}
