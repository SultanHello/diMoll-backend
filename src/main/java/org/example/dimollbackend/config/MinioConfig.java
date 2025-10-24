//package org.example.dimollbackend.config;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MinioConfig {
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        BasicAWSCredentials creds = new BasicAWSCredentials("minio", "minio123");
//
//        return AmazonS3ClientBuilder.standard()
//
////                .withEndpointConfiguration(
////                        new AwsClientBuilder.EndpointConfiguration("http://localhost:9001", "us-east-1")
////                )
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration("http://minio:9000", "us-east-1")
//                )
//
//                .withPathStyleAccessEnabled(true) // обязательно для MinIO
//                .withCredentials(new AWSStaticCredentialsProvider(creds))
//                .build();
//    }
//}