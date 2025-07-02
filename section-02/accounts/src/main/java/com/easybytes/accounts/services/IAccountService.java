package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.dtos.CustomerDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerAccountResponseDto fetchAccount(final String mobileNum);
}
