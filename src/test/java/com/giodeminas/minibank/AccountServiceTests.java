package com.giodeminas.minibank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.giodeminas.minibank.dto.AccountDTO;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.repository.AccountRepository;
import com.giodeminas.minibank.service.AccountService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class AccountServiceTests {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountService accountService;

  @Test
  public void testCreateAccount() {
    // Arrange
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setAccountNumber("123");

    Account mockAccount = new Account();
    mockAccount.setAccountNumber("123");

    // Mock the save behavior
    when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

    // Act
    AccountDTO result = accountService.createAccount(accountDTO);

    // Assert
    assertNotNull(result);
    assertEquals("123", result.getAccountNumber());

    // Verify
    verify(accountRepository, times(1)).save(any(Account.class));
  }

  @Test
  public void testCreateAccountThrowsExceptionOnDuplicate() {
    // Arrange
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setAccountNumber("123");

    Account mockAccount = new Account();
    mockAccount.setAccountNumber("123");

    // Mock
    when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

    // Act and Assert 1
    AccountDTO result = accountService.createAccount(accountDTO);
    assertNotNull(result);
    assertEquals("123", result.getAccountNumber());

    // Act and Assert 2
    when(accountRepository.save(any(Account.class))).thenThrow(
        DataIntegrityViolationException.class);
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> accountService.createAccount(accountDTO));
    assertEquals("Can't create duplicate Account with Account Number '123'.",
        exception.getMessage(), "Expected createAccount() to throw, but it didn't.");

    // Verify
    verify(accountRepository, times(2)).save(any(Account.class));
  }

  @Test
  public void testGetAllAccounts() {
    // Arrange
    Account account1 = new Account();
    account1.setAccountNumber("123");

    Account account2 = new Account();
    account2.setAccountNumber("456");

    // Mock
    when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

    // Act
    List<AccountDTO> result = accountService.getAllAccounts();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("123", result.get(0).getAccountNumber());
    assertEquals("456", result.get(1).getAccountNumber());

    // Verify
    verify(accountRepository, times(1)).findAll();
  }
}
