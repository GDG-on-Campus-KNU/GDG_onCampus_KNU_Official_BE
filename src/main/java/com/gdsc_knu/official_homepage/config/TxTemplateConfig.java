package com.gdsc_knu.official_homepage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TxTemplateConfig {
    public final TransactionTemplate transactionTemplate;

    public TxTemplateConfig(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

}
