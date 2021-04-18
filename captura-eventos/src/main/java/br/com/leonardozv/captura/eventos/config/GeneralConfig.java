package br.com.leonardozv.captura.eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class GeneralConfig {
	
	@Bean("amazonS3")
    AmazonS3 amazonS3Client() {
		
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new AnonymousAWSCredentials());
        
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration("https://s3.amazonaws.com/", "");
                
        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder
        		.standard()        		
        		.withEndpointConfiguration(endpointConfiguration)
        		.withCredentials(credentialsProvider);
        
        return clientBuilder.build();
    }

}
