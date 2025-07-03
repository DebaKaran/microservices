package com.easybytes.accounts.utils;

import com.easybytes.accounts.dtos.AccountsDto;
import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.entity.Accounts;
import com.easybytes.accounts.entity.Customer;

public class TestDataUtil {

    public static Accounts getAccounts() {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(1L);
        accounts.setAccountType("SAVINGS");
        accounts.setAccountNumber(12345L);
        accounts.setBranchAddress("Bangalore, India");

        return accounts;
    }

    public static Customer getCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("ABC");
        customer.setEmail("xyz@example.com");
        customer.setMobileNumber("1234567890");
        return  customer;
    }

    public static AccountsDto getAccountsDto() {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(12345L);
        accountsDto.setAccountType("SAVINGS");
        accountsDto.setBranchAddress("Bangalore, India");
        return accountsDto;
    }

    public static CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("xyz@example.com");
        customerDto.setMobileNumber("1234567890");
        return customerDto;
    }

    public static CustomerAccountResponseDto getCustomerAccountResponseDto() {
        return new CustomerAccountResponseDto(getCustomerDto(), getAccountsDto());
    }

    public static CustomerDto getInvalidCustomerDto() {
        CustomerDto invalidCustomer = new CustomerDto();
        invalidCustomer.setName("Ab"); // too short
        invalidCustomer.setEmail("invalid-email"); // invalid
        invalidCustomer.setMobileNumber("123"); // invalid
        return  invalidCustomer;
    }
}
