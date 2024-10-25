package com.giodeminas.minibank.controller;

import com.giodeminas.minibank.dto.AccountDTO;
import com.giodeminas.minibank.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @PostMapping
  public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO) {
    try {
      AccountDTO newAccountDTO = accountService.createAccount(accountDTO);
      return new ResponseEntity<>("New account with number '" + newAccountDTO.getAccountNumber()
          + "' created successfully!", HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping
  public ResponseEntity<List<AccountDTO>> getAllAccounts() {
    return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
  }
}
