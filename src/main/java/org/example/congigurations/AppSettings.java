package org.example.congigurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppSettings {

    @Value("${transaction.timeout}")
    public static int transactionTimeout;
}

