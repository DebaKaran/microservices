package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);
}
