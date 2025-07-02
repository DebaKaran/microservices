package com.easybytes.accounts.controllers;

import com.easybytes.accounts.constants.AccountsConstants;
import com.easybytes.accounts.dtos.AccountsDto;
import com.easybytes.accounts.dtos.CustomerAccountResponseDto;
import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.easybytes.accounts.exceptions.ResourceNotFoundException;
import com.easybytes.accounts.services.IAccountService;
import com.easybytes.accounts.utils.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AccountsController.class)
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAccountService accountService;

    @Test
    public void createAccount_Successful() throws Exception {
        CustomerDto customerDto = TestDataUtil.getCustomerDto();

        String custometDtoJsonString = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(custometDtoJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(AccountsConstants.MESSAGE_201));

        verify(accountService).createAccount(any(CustomerDto.class));
    }

    @Test
    void createAccount_customerAlreadyExists_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = TestDataUtil.getCustomerDto();
        String custometDtoJsonString = objectMapper.writeValueAsString(customerDto);

        doThrow(new CustomerAlreadyExistsException("Customer already registered"))
                .when(accountService).createAccount(any(CustomerDto.class));

        mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(custometDtoJsonString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Customer already registered"));

        verify(accountService).createAccount(any(CustomerDto.class));
    }

    @Test
    public void fetchAccountDetails_Successful() throws Exception {
        String mobileNumber = "1234567890";
        CustomerAccountResponseDto responseDto = TestDataUtil.getCustomerAccountResponseDto();

        //given
        when(accountService.fetchAccount(mobileNumber)).thenReturn(responseDto);

        mockMvc.perform(get("/api/fetch")
                        .param("mobileNum", mobileNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDto.name").value("ABC"))
                .andExpect(jsonPath("$.accountsDto.accountNumber").value(12345L));

        verify(accountService, times(1)).fetchAccount(mobileNumber);

    }

    @Test
    void fetchAccountDetails_shouldReturnNotFound_whenCustomerNotFound() throws Exception {
        String mobileNumber = "1234567890";

        // Given: Service layer throws exception
        when(accountService.fetchAccount(mobileNumber))
                .thenThrow(new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        //when + then
        mockMvc.perform(get("/api/fetch")
                .param("mobileNum", mobileNumber)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Customer not found with the given input data mobileNumber : '1234567890'"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void fetchAccountDetails_shouldReturnNotFound_whenAccountNotFoundForCustomer() throws Exception {
        String mobileNumber = "1234567890";
        String customerId = "1";

        // Given: Service layer throws exception

        when(accountService.fetchAccount(mobileNumber))
                .thenThrow(new ResourceNotFoundException("Account", "customerId", customerId));

        //when + then
        mockMvc.perform(get("/api/fetch")
                        .param("mobileNum", mobileNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Account not found with the given input data customerId : '1'"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }


}
