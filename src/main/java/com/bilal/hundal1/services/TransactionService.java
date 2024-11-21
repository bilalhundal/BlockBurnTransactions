package com.bilal.hundal1.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final RestTemplate restTemplate;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    private final String apiBaseUrl = "https://api.hiro.so/extended/v2";

    public TransactionService(RestTemplate restTemplate, S3Service s3Service, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.s3Service = s3Service;
        this.objectMapper = objectMapper;
    }

    /**
     * Main method to fetch and store data in the S3 bucket.
     */
    //@Scheduled(fixedDelay = 10000)
    public void fetchAndStoreData() {
        log.info("Starting to fetch and store data...");
        try {
            String latestBlocksUrl = apiBaseUrl + "/burn-blocks/latest/blocks?limit=10";
            log.debug("Fetching latest blocks from URL: {}", latestBlocksUrl);

            // Fetch the latest blocks synchronously
            String blocksResponse = restTemplate.getForObject(latestBlocksUrl, String.class);

            if (blocksResponse != null) {
                log.debug("Successfully fetched blocks: {}", blocksResponse);
                processLatestBlocks(blocksResponse);
            } else {
                log.warn("No blocks received from the API.");
            }
        } catch (Exception e) {
            log.error("Error while fetching and storing data: {}", e.getMessage(), e);
        }
    }

    /**
     * Processes the blocks and fetches transactions for each block.
     *
     * @param blocks JSON response containing block data.
     */
    private void processLatestBlocks(String blocks) {
        log.info("Processing blocks...");
        try {
            JsonNode rootNode = objectMapper.readTree(blocks);
            
            JsonNode resultsNode = rootNode.get("results");
            System.out.println("Result Node "+ resultsNode);

            if (resultsNode == null || !resultsNode.isArray()) {
                log.error("Invalid API response: 'results' array is missing.");
                return;
            }

            for (JsonNode block : resultsNode) {
                if (block.has("height")) {
                    String burnBlockHeight = block.get("height").asText();
                    log.debug("Processing block with burn block height: {}", burnBlockHeight);

                    // Fetch transactions for the block
                    String transactionsUrl = apiBaseUrl + "/blocks/" + burnBlockHeight + "/transactions";
                    log.debug("Fetching transactions from URL: {}", transactionsUrl);

                    String transactionsResponse = restTemplate.getForObject(transactionsUrl, String.class);
                     System.out.println("Transactions: "+transactionsResponse);
                    if (transactionsResponse != null) {
                        log.debug("Transactions fetched for block {}: {}", burnBlockHeight, transactionsResponse);
                        storeTransactions(transactionsResponse, burnBlockHeight);
                    } else {
                        log.warn("No transactions found for block: {}", burnBlockHeight);
                    }
                } else {
                    log.warn("Block missing 'burn_block_height' field: {}", block);
                }
            }
        } catch (Exception e) {
            log.error("Error while processing blocks: {}", e.getMessage(), e);
        }
    }

    /**
     * Stores transactions in the S3 bucket using burn block height as the key.
     *
     * @param transactions JSON containing transactions.
     * @param burnBlockHeight Identifier for the block.
     */
    private void storeTransactions(String transactions, String burnBlockHeight) {
        log.info("Storing transactions for block: {}", burnBlockHeight);
        try {
            String key = "transactions/block-" + burnBlockHeight + ".json";
            s3Service.uploadToS3(key, transactions);
            log.info("Transactions successfully stored in S3 with key: {}", key);
        } catch (Exception e) {
            log.error("Error while storing transactions in S3: {}", e.getMessage(), e);
        }
    }
}
