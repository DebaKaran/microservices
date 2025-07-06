package com.easybytes.accounts.repository;

import com.easybytes.accounts.audits.AuditorAwareImpl;
import com.easybytes.accounts.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(AuditorAwareImpl.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByMobileNumber_shouldReturnCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setMobileNumber("9999999999");

        // Set required base fields
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("test-run");

        customerRepository.save(customer);

        //when
        Optional<Customer> foundCustomer  =
                customerRepository.findByMobileNumber("9999999999");

        //then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getEmail()).isEqualTo("test@example.com");

    }
}
