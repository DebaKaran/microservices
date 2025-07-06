package com.easybytes.accounts.mapper;

import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.entity.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerMapper {
    public static CustomerDto mapToCustomerDto(final Customer customer) {
        final CustomerDto customerDto = new CustomerDto();
        mapToCustomerDto(customer, customerDto);
        return customerDto;
    }

    public static Customer mapToCustomer(final CustomerDto customerDto) {
        final Customer customer = new Customer();
        mapToCustomer(customerDto, customer);
        return customer;
    }

    public static void mapToCustomerDto(final Customer customer, final CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
    }

    public static void mapToCustomer(final CustomerDto customerDto, final Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("system"); // or the actual user name
    }
}
