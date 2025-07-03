package com.easybytes.accounts.audits;

import com.easybytes.accounts.entity.Accounts;
import com.easybytes.accounts.repository.AccountsRepository;
import com.easybytes.accounts.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@Import(AuditorAwareImpl.class)
public class AccountsAuditTest {

    @Autowired
    private AccountsRepository repository;

    @Test
    void whenEntityIsSaved_thenAuditFieldsAreSet() {
        Accounts entity = TestDataUtil.getAccounts();

        Accounts saved = repository.save(entity);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getCreatedBy());

        assertEquals("ACCOUNTS_MS", saved.getCreatedBy());


    }
}
