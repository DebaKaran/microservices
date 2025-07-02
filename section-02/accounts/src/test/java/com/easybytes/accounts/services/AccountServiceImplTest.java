package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.entity.Accounts;
import com.easybytes.accounts.entity.Customer;
import com.easybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.easybytes.accounts.exceptions.ResourceNotFoundException;
import com.easybytes.accounts.repository.AccountsRepository;
import com.easybytes.accounts.repository.CustomerRepository;
import com.easybytes.accounts.services.impl.AccountServiceImpl;
import com.easybytes.accounts.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AccountsRepository accountsRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    public void createAccount_whenCustomerDoesNotExist_shouldCreateAccount() {
        Customer customer = new Customer();

        //Given
        when(customerRepository.findByMobileNumber(any(String.class)))
                .thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        //below is not required as it is not returnig anything
//        when(accountsRepository.save(any(Accounts.class)))
//                .thenReturn(...);

        //when
        accountService.createAccount(TestDataUtil.getCustomerDto());

        //then
        verify(customerRepository, times(1)).findByMobileNumber(anyString());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountsRepository, times(1)).save(any(Accounts.class));

    }

    @Test
    public void createAccount_shouldThrowException_whenCustomerAlreadyExists() {
        Customer existingCustomer  = new Customer();

        //Given
        when(customerRepository.findByMobileNumber(any(String.class)))
                .thenReturn(Optional.of(existingCustomer ));

        //when + given
        assertThrows(CustomerAlreadyExistsException.class, () -> {
                accountService.createAccount(TestDataUtil.getCustomerDto());
        });

        //then
        verify(customerRepository, times(1)).findByMobileNumber(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
        verify(accountsRepository, never()).save(any(Accounts.class));
    }

    @Test
    void fetchAccount_shouldReturnCustomerAccountResponse_whenCustomerAndAccountExist() {
        Customer customer = TestDataUtil.getCustomer();
        Accounts accounts = TestDataUtil.getAccounts();

        //given
        when(customerRepository.findByMobileNumber(anyString()))
                .thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(anyLong()))
                .thenReturn(Optional.of(accounts));

        //when
        CustomerAccountResponseDto responseDto = accountService.fetchAccount("1234567890");

        // Assert (Then)
        assertNotNull(responseDto);
        assertNotNull(responseDto.getCustomerDto());
        assertNotNull(responseDto.getAccountsDto());

        assertEquals("ABC", responseDto.getCustomerDto().getName());
        assertEquals("xyz@example.com", responseDto.getCustomerDto().getEmail());
        assertEquals("1234567890", responseDto.getCustomerDto().getMobileNumber());

        assertEquals("SAVINGS", responseDto.getAccountsDto().getAccountType());
        assertEquals(12345L, responseDto.getAccountsDto().getAccountNumber());
        assertEquals("Bangalore, India", responseDto.getAccountsDto().getBranchAddress());

        //verification
        verify(customerRepository, times(1)).findByMobileNumber(anyString());
        verify(accountsRepository, times(1)).findByCustomerId(anyLong());


    }

    @Test
    void fetchAccount_shouldThrowResourceNotFoundException_whenCustomerNotFound() {
        //given
        String mobileNumber = "1234567890";
        when(customerRepository.findByMobileNumber(anyString()))
                .thenReturn(Optional.empty());

        //when + given
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.fetchAccount(mobileNumber);
        });

        //Then
        assertEquals("Customer not found with the given input data mobileNumber : '1234567890'", exception.getMessage());
        verify(customerRepository, times(1)).findByMobileNumber(mobileNumber);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(accountsRepository); // since customer is not found
    }

    @Test
    void fetchAccount_shouldThrowResourceNotFoundException_whenAccountNotFoundForCustomer() {
        Customer customer = TestDataUtil.getCustomer();
        String mobileNumber = "1234567890";

        //given
        when(customerRepository.findByMobileNumber(anyString()))
                .thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(anyLong()))
                .thenReturn(Optional.empty());

        //when + given
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.fetchAccount(mobileNumber);
        });

        //Then
        assertAll(
                () -> assertEquals("Account not found with the given input data customerId : '1'", exception.getMessage()),
                () -> verify(customerRepository, times(1)).findByMobileNumber(mobileNumber),
                () -> verify(accountsRepository, times(1)).findByCustomerId(customer.getCustomerId())
        );

        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(accountsRepository);

    }

    @Test
    public void updateAccount_shouldUpdateCustomerAndAccount_whenValidDataProvided() {
        CustomerAccountResponseDto responseDto = TestDataUtil.getCustomerAccountResponseDto();
        Accounts accounts = TestDataUtil.getAccounts();
        Customer customer = TestDataUtil.getCustomer();

        //given
        when(accountsRepository.findById(anyLong()))
                .thenReturn(Optional.of(accounts));

        when(accountsRepository.save(any(Accounts.class)))
                .thenReturn(accounts);

        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        //when
        boolean isUpdated = accountService.updateAccount(responseDto);

        //then
        assertTrue(isUpdated);

        //verifacation
        verify(accountsRepository, times(1)).findById(anyLong());
        verify(accountsRepository, times(1)).save(any(Accounts.class));
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void updateAccount_shouldThrowResourceNotFoundException_whenAccountNotFound() {
        CustomerAccountResponseDto responseDto = TestDataUtil.getCustomerAccountResponseDto();

        //given
        when(accountsRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when + then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateAccount(responseDto);
        });

        String accountNumber = responseDto.getAccountsDto().getAccountNumber().toString();
        String expectedMessage = "Account not found with the given input data AccountNumber : '" + accountNumber + "'";
        //Then
        assertAll(
                () -> assertEquals("Account not found with the given input data AccountNumber : '12345'", exception.getMessage()),
                () -> verify(accountsRepository, times(1)).findById(responseDto.getAccountsDto().getAccountNumber())
        );
        verifyNoMoreInteractions(accountsRepository);
        verifyNoInteractions(customerRepository);

    }

    @Test
    public void updateAccount_shouldThrowResourceNotFoundException_whenCustomerNotFound() {
        CustomerAccountResponseDto responseDto = TestDataUtil.getCustomerAccountResponseDto();
        Accounts accounts = TestDataUtil.getAccounts();

        //given
        when(accountsRepository.findById(anyLong())).thenReturn(Optional.of(accounts));
        when(accountsRepository.save(any(Accounts.class))).thenReturn(accounts);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when + then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateAccount(responseDto);
        });

        Long customerId = accounts.getCustomerId();
        String expectedMessage = "Customer not found with the given input data CustomerID : '" + customerId + "'";
        //Then
        assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage()),
                () -> verify(accountsRepository, times(1)).findById(responseDto.getAccountsDto().getAccountNumber()),
                () -> verify(accountsRepository, times(1)).save(accounts),
                () -> verify(customerRepository, times(1)).findById(accounts.getCustomerId())
        );
        verifyNoMoreInteractions(accountsRepository);
        verifyNoMoreInteractions(customerRepository);

    }

    @Test
    public void updateAccount_shouldReturnFalse_whenAccountsDtoIsNull() {
        CustomerAccountResponseDto customerAccountResponseDto = new CustomerAccountResponseDto();

        boolean isUpdated = accountService.updateAccount(customerAccountResponseDto);

        assertFalse(isUpdated);
        verifyNoInteractions(customerRepository);
        verifyNoInteractions(accountsRepository);
    }

    @Test
    void deleteAccount_shouldDeleteCustomerAndAccount_whenValidMobileNumberProvided() {
        // Arrange
        String mobileNumber = "1234567890";
        Customer customer = TestDataUtil.getCustomer(); // with customerId set

        when(customerRepository.findByMobileNumber(mobileNumber))
                .thenReturn(Optional.of(customer));

        // Act
        boolean result = accountService.deleteAccount(mobileNumber);

        // Assert
        assertTrue(result);
        verify(customerRepository).findByMobileNumber(mobileNumber);
        verify(accountsRepository).deleteByCustomerId(customer.getCustomerId());
        verify(customerRepository).deleteById(customer.getCustomerId());
    }

    @Test
    void deleteAccount_shouldThrowResourceNotFoundException_whenCustomerNotFound() {
        String mobileNumber = "1234567890";

        when(customerRepository.findByMobileNumber(mobileNumber))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteAccount(mobileNumber);
        });

        assertEquals(
                "Customer not found with the given input data mobileNumber : '1234567890'",
                exception.getMessage()
        );

        verify(customerRepository).findByMobileNumber(mobileNumber);
        verifyNoInteractions(accountsRepository);
    }


}
