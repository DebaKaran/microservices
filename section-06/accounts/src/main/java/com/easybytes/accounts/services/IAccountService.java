package com.easybytes.accounts.services;

import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.dtos.CustomerDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerAccountResponseDto fetchAccount(final String mobileNum);

    boolean updateAccount(CustomerAccountResponseDto customerAccountResponseDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);
}
