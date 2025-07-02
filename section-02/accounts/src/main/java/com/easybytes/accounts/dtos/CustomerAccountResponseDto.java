package com.easybytes.accounts.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountResponseDto {
    private CustomerDto customerDto;
    private AccountsDto accountsDto;
}
