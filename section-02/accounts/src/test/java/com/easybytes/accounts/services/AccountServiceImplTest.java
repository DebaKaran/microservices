package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.entity.Accounts;
import com.easybytes.accounts.entity.Customer;
import com.easybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.easybytes.accounts.repository.AccountsRepository;
import com.easybytes.accounts.repository.CustomerRepository;
import com.easybytes.accounts.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private static CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setMobileNumber("9876543210");
        return customerDto;
    }
}
