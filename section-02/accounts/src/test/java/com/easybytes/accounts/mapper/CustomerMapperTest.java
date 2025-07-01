package com.easybytes.accounts.mapper;

import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {
    @Test
    void mapToCustomerDto_shouldMapFieldsCorrectly() {
        Customer customer = new Customer();
        customer.setName("Alice");
        customer.setEmail("alice@example.com");
        customer.setMobileNumber("9876543210");

        CustomerDto dto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());

        assertEquals("Alice", dto.getName());
        assertEquals("alice@example.com", dto.getEmail());
        assertEquals("9876543210", dto.getMobileNumber());
    }

    @Test
    void mapToCustomer_shouldMapFieldsCorrectly() {
        CustomerDto dto = new CustomerDto();
        dto.setName("Bob");
        dto.setEmail("bob@example.com");
        dto.setMobileNumber("1234567890");

        Customer customer = CustomerMapper.mapToCustomer(dto, new Customer());

        assertEquals("Bob", customer.getName());
        assertEquals("bob@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getMobileNumber());
    }
}
