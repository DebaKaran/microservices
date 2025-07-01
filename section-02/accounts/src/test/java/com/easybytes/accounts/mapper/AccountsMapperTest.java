package com.easybytes.accounts.mapper;

import com.easybytes.accounts.dtos.AccountsDto;
import com.easybytes.accounts.entity.Accounts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsMapperTest {

    @Test
    void mapToAccountsDto_shouldMapFieldsCorrectly() {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(1234567890L);
        accounts.setAccountType("Savings");
        accounts.setBranchAddress("Bangalore");

        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

        assertEquals(1234567890L, accountsDto.getAccountNumber());
        assertEquals("Savings", accountsDto.getAccountType());
        assertEquals("Bangalore", accountsDto.getBranchAddress());
    }

    @Test
    void mapToAccounts_shouldMapFieldsCorrectly() {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(9876543210L);
        accountsDto.setAccountType("Current");
        accountsDto.setBranchAddress("Bangalore");

        Accounts accounts = AccountsMapper.mapToAccounts(accountsDto, new Accounts());

        assertEquals(9876543210L, accounts.getAccountNumber());
        assertEquals("Current", accounts.getAccountType());
        assertEquals("Bangalore", accounts.getBranchAddress());
    }
}

