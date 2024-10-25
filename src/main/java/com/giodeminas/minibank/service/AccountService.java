package com.giodeminas.minibank.service;

import com.giodeminas.minibank.dto.AccountDTO;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.repository.AccountRepository;
import com.giodeminas.minibank.service.util.DTOConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  public AccountDTO createAccount(AccountDTO accountDTO) {
    try {
      Account newAccount = new Account(accountDTO.getAccountNumber(), accountDTO.getBalance());
      return DTOConverter.convertToDTO(accountRepository.save(newAccount));
    } catch (Exception e) {
      throw new RuntimeException(
          "Can't create duplicate Account with Account Number '" + accountDTO.getAccountNumber()
              + "'.");
    }
  }

  public List<AccountDTO> getAllAccounts() {
    return accountRepository.findAll().stream()
        .map(DTOConverter::convertToDTO)
        .collect(Collectors.toList());
  }
}
