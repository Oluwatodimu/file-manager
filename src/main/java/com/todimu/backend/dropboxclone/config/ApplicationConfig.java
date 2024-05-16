package com.todimu.backend.dropboxclone.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final DoSpaceProperties doSpaceProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AmazonS3 getS3Bucket() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(doSpaceProperties.getAccessKey(), doSpaceProperties.getAccessSecret());
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(doSpaceProperties.getUrl(), doSpaceProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}
