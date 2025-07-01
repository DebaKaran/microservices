package com.easybytes.accounts.controllers;

import com.easybytes.accounts.constants.AccountsConstants;
import com.easybytes.accounts.dtos.CustomerDto;
import com.easybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.easybytes.accounts.services.IAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        CustomerDto customerDto = getCustomerDto();

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
        CustomerDto customerDto = getCustomerDto();
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

    private static CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setMobileNumber("9876543210");
        return customerDto;
    }
}
