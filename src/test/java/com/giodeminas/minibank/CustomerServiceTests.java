package com.giodeminas.minibank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.giodeminas.minibank.dto.AddressDTO;
import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.dto.CustomerDTOPaginated;
import com.giodeminas.minibank.dto.PaginatedResponse;
import com.giodeminas.minibank.model.Account;
import com.giodeminas.minibank.model.Address;
import com.giodeminas.minibank.model.Customer;
import com.giodeminas.minibank.repository.AccountRepository;
import com.giodeminas.minibank.repository.AddressRepository;
import com.giodeminas.minibank.repository.CustomerRepository;
import com.giodeminas.minibank.service.CustomerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class CustomerServiceTests {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private CustomerService customerService;

  @Test
  public void testCreateCustomer() {
    // Arrange
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAccountNumber("123");
    customerDTO.setFirstName("John");
    customerDTO.setLastName("Doe");

    Customer mockCustomer = new Customer();
    mockCustomer.setFirstName("John");
    mockCustomer.setLastName("Doe");

    Account mockAccount = new Account("123");

    // Mock
    when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(mockAccount));
    when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

    // Act
    CustomerDTO result = customerService.createCustomer(customerDTO);

    // Assert
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123", result.getAccountNumber());

    // Verify
    verify(accountRepository, times(1)).save(any(Account.class));
  }

  @Test
  public void testCreateCustomerThrowsExceptionDuplicate() {
    // Arrange
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAccountNumber("123");
    customerDTO.setFirstName("John");
    customerDTO.setLastName("Doe");
    customerDTO.setEmail("john.doe@gmail.com");
    customerDTO.setPhone("37066655555");

    Customer mockCustomer = new Customer();
    mockCustomer.setFirstName("John");
    mockCustomer.setLastName("Doe");
    mockCustomer.setEmail("john.doe@gmail.com");
    mockCustomer.setPhone("37066655555");

    Account mockAccount = new Account("123", 1000.99);

    // Mock
    when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(mockAccount));
    when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

    // Act 1
    CustomerDTO result = customerService.createCustomer(customerDTO);

    // Assert 1
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123", result.getAccountNumber());

    // Act 2
    when(accountRepository.save(any(Account.class))).thenThrow(
        DataIntegrityViolationException.class);
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> customerService.createCustomer(customerDTO));

    // Assert 2
    assertEquals(
        "Couldn't insert duplicate of Customer values 'John, Doe, john.doe@gmail.com, 37066655555'.",
        exception.getMessage(), "Expected createCustomer to throw, but it didn't.");

    // Verify
    verify(accountRepository, times(2)).save(any(Account.class));
  }

  @Test
  public void testGetCustomer() {
    // Arrange
    Long customerId = 1L;

    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAccountNumber("123");
    customerDTO.setFirstName("John");
    customerDTO.setLastName("Doe");

    Customer mockCustomer = new Customer();
    mockCustomer.setFirstName("John");
    mockCustomer.setLastName("Doe");

    Account mockAccount = new Account("123", 1000.99);
    mockCustomer.setAccount(mockAccount);

    // Mock
    when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

    // Act
    CustomerDTO result = customerService.getCustomer(customerId);

    // Assert
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123", result.getAccountNumber());

    // Verify
    verify(customerRepository, times(1)).findById(customerId);
  }

  @Test
  public void testGetCustomers() {
    // Arrange
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setFirstName("John");

    Customer mockCustomer = new Customer();
    mockCustomer.setFirstName("John");
    mockCustomer.setLastName("Doe");

    Account mockAccount = new Account("123", 1000.99);
    mockCustomer.setAccount(mockAccount);

    List<Customer> mockCustomers = Arrays.asList(mockCustomer);
    Page<Customer> customerPage = new PageImpl<>(mockCustomers);

    Pageable pageable = PageRequest.of(0, 5);

    // Mock
    when(customerRepository.searchCustomers(null, "John", null, null, null, null, null, null, null,
        null, null, pageable)).thenReturn(customerPage);

    // Act
    PaginatedResponse<CustomerDTOPaginated> result = customerService.getCustomers(customerDTO, 0,
        5);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getContent().size());
    assertEquals("firstName=John", result.getQuery());
    assertEquals(1, result.getContent().get(0).getRowNumber());
    assertEquals("John", result.getContent().get(0).getCustomer().getFirstName());

    // Verify
    verify(customerRepository, times(1))
        .searchCustomers(null, "John", null, null, null, null, null,
            null, null,
            null, null, pageable);
  }

  @Test
  public void testUpdateCustomer() {
    // Arrange
    Long customerId = 1L;

    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAccountNumber("456");
    customerDTO.setFirstName("UpdatedName");

    ArrayList<AddressDTO> addresses = new ArrayList<>();

    AddressDTO addressDTO1 = new AddressDTO();
    addressDTO1.setAddressId(1L);
    addressDTO1.setStreet("UpdatedStreet");
    addresses.add(addressDTO1);

    AddressDTO addressDTO2 = new AddressDTO();
    addressDTO2.setCountry("InsertedCountry");
    addresses.add(addressDTO2);

    customerDTO.setAddresses(addresses);

    Customer mockCustomer = new Customer();
    mockCustomer.setFirstName("John");
    mockCustomer.setLastName("Doe");

    ArrayList<Address> mockAddresses = new ArrayList<>();

    Address mockAddress1 = new Address();
    mockAddress1.setId(1L);
    mockAddress1.setStreet("Architect st.");
    mockAddresses.add(mockAddress1);

    mockCustomer.setAddresses(mockAddresses);

    Address mockAddress2 = new Address();
    mockAddress2.setId(2L);
    mockAddress2.setStreet("UpdatedStreet");

    Account mockAccount1 = new Account("123", 1000.99);
    mockCustomer.setAccount(mockAccount1);
    Account mockAccount2 = new Account("456");

    // Mock
    when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
    when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);
    when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(mockAccount1));
    when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(mockAccount2));
    when(addressRepository.findById(1L)).thenReturn(Optional.of(mockAddress1));
    when(addressRepository.save(any(Address.class))).thenReturn(mockAddress2);

    // Act
    CustomerDTO result = customerService.updateCustomer(customerId, customerDTO);

    // Assert
    assertNotNull(result);
    assertEquals("UpdatedName", result.getFirstName());
    assertEquals("UpdatedStreet", result.getAddresses().get(0).getStreet());
    assertEquals("456", result.getAccountNumber());
    assertEquals("InsertedCountry", result.getAddresses().get(1).getCountry());

    // Verify
    verify(accountRepository, times(1)).findByAccountNumber("123");
    verify(accountRepository, times(1)).findByAccountNumber("456");
    verify(accountRepository, times(2)).save(any(Account.class));
    verify(addressRepository, times(1)).findById(1L);
    verify(customerRepository, times(1)).findById(customerId);
    verify(customerRepository, times(1)).save(any(Customer.class));
  }
}
