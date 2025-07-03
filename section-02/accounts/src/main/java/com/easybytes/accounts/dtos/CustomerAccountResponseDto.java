package com.easybytes.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountResponseDto {
    @Schema(
            description = "Customer details"
    )
    private CustomerDto customerDto;
    @Schema(
            description = "Account details of the Customer"
    )
    private AccountsDto accountsDto;
}
