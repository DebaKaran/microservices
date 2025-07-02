package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.entity.Accounts;
import com.easybytes.accounts.entity.Customer;
import com.easybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.easybytes.accounts.exceptions.ResourceNotFoundException;
import com.easybytes.accounts.repository.AccountsRepository;
import com.easybytes.accounts.repository.CustomerRepository;
import com.easybytes.accounts.services.impl.AccountServiceImpl;
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
    public void createAccount_shouldCreateAccountSuccessfully() {
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
        accountService.createAccount(getCustomerDto());

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
                accountService.createAccount(getCustomerDto());
        });

        //then
        verify(customerRepository, times(1)).findByMobileNumber(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
        verify(accountsRepository, never()).save(any(Accounts.class));
    }

    @Test
    void fetchAccount_shouldReturnCustomerAccountResponse_whenCustomerAndAccountExist() {
        Customer customer = getCustomer();
        Accounts accounts = getAccounts();

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

        assertEquals("SAVING", responseDto.getAccountsDto().getAccountType());
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
        assertEquals("Customer not found with the given input data mobileNumber : '123456789'", exception.getMessage());
        verify(customerRepository, times(1)).findByMobileNumber(mobileNumber);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(accountsRepository); // since customer is not found
    }

    @Test
    void fetchAccount_shouldThrowResourceNotFoundException_whenAccountNotFoundForCustomer() {
        Customer customer = getCustomer();
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

    private static Accounts getAccounts() {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(1L);
        accounts.setAccountType("SAVING");
        accounts.setAccountNumber(12345L);
        accounts.setBranchAddress("Bangalore, India");

        return accounts;
    }
    private static Customer getCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("ABC");
        customer.setEmail("xyz@example.com");
        customer.setMobileNumber("1234567890");
        return  customer;
    }

    private static CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setMobileNumber("9876543210");
        return customerDto;
    }
}
