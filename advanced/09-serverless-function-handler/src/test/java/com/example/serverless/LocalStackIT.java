package com.example.serverless;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.LAMBDA;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse;

@SpringBootTest
@Testcontainers
class LocalStackIT {

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.4.0"))
            .withServices(LAMBDA);

    @Test
    void testLocalStackLambdaService() throws IOException {
        // This test verifies that we can interact with the AWS Lambda service provided by LocalStack.
        // Deploying the actual Java function into LocalStack during the build is complex because
        // it requires the JAR to be built first.
        // Here we demonstrate the integration infrastructure is working.

        LambdaClient lambdaClient = LambdaClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LAMBDA))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                ))
                .region(Region.of(localStack.getRegion()))
                .build();

        ListFunctionsResponse response = lambdaClient.listFunctions();
        assertThat(response).isNotNull();
        assertThat(response.functions()).isEmpty();
    }
}
