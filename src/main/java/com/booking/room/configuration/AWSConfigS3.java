package com.booking.room.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfigS3 {
    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3Client = AmazonS3Client.builder().withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
        return s3Client;
    }
}
