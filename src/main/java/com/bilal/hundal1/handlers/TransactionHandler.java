package com.bilal.hundal1.handlers;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bilal.hundal1.services.TransactionService;

@Component
public class TransactionHandler implements Function<Map<String, Object>, String> {

    @Autowired
    private TransactionService transactionService;

    @Override
    public String apply(Map<String, Object> input) {
        transactionService.fetchAndStoreData();
        return "Data fetched and stored successfully.";
    }
}
