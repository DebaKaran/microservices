package com.easybytes.accounts.mapper;

import com.easybytes.accounts.dtos.AccountsDto;
import com.easybytes.accounts.entity.Accounts;

public class AccountsMapper {
    public static AccountsDto mapToAccountsDto(final Accounts accounts) {
        final AccountsDto accountsDto = new AccountsDto();
        mapToAccountsDto(accounts, accountsDto);
        return accountsDto;
    }

    public static Accounts mapToAccounts(final AccountsDto accountsDto) {
        final Accounts accounts = new Accounts();
        mapToAccounts(accountsDto, accounts);
        return accounts;
    }

    public static void mapToAccountsDto(final Accounts accounts, final AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
    }

    public  static void mapToAccounts(final AccountsDto accountsDto, final Accounts accounts) {
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
    }
}
