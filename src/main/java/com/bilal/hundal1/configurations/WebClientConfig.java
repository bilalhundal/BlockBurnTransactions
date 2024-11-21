package com.bilal.hundal1.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class WebClientConfig {
	@Bean
     RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of("ap-southeast-2")) 
                .build();
    }
}
