package com.example.sandbox;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Frame;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    private final DockerClient dockerClient;
    private static final String IMAGE_NAME = "python:3.9-slim";
    private static final long MEMORY_LIMIT = 50 * 1024 * 1024; // 50MB
    private static final long CPU_QUOTA = 50000; // 50% CPU
    private static final int TIMEOUT_SECONDS = 5;

    public CodeExecutionService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public CodeExecutionResponse execute(String code) {
        // Prepare the command to execute python code
        // We'll pass the code as a base64 encoded string to avoid escaping issues, 
        // and decode it inside the container.
        String encodedCode = Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8));
        String command = "echo " + encodedCode + " | base64 -d | python3";

        CreateContainerResponse container = dockerClient.createContainerCmd(IMAGE_NAME)
                .withCmd("sh", "-c", command)
                .withHostConfig(HostConfig.newHostConfig()
                        .withMemory(MEMORY_LIMIT)
                        .withCpuQuota(CPU_QUOTA)
                        .withNetworkMode("none")) // No internet access for untrusted code
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();

        String containerId = container.getId();
        dockerClient.startContainerCmd(containerId).exec();

        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        boolean timeout = false;
        int exitCode = -1;

        try {
            // Collect logs
            ResultCallback.Adapter<Frame> loggingCallback = new ResultCallback.Adapter<>() {
                @Override
                public void onNext(Frame frame) {
                    String payload = new String(frame.getPayload(), StandardCharsets.UTF_8);
                    if (frame.getStreamType().name().equals("STDOUT")) {
                        stdout.append(payload);
                    } else if (frame.getStreamType().name().equals("STDERR")) {
                        stderr.append(payload);
                    }
                }
            };

            dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(loggingCallback);

            // Wait for the container to finish with a timeout
            try {
                com.github.dockerjava.api.command.WaitContainerResultCallback waitCallback = 
                        new com.github.dockerjava.api.command.WaitContainerResultCallback();
                
                dockerClient.waitContainerCmd(containerId).exec(waitCallback);
                
                exitCode = waitCallback.awaitStatusCode(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (com.github.dockerjava.api.exception.DockerClientException e) {
                 // awaitStatusCode throws DockerClientException on timeout/interrupt usually or we can handle it
                 timeout = true;
                 dockerClient.killContainerCmd(containerId).exec();
            }


            // Give a moment for logs to flush if we didn't timeout
            if (!timeout) {
                try {
                    loggingCallback.awaitCompletion(1, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {}
            }

        } finally {
            // Cleanup
            try {
                dockerClient.removeContainerCmd(containerId).withForce(true).exec();
            } catch (Exception e) {
                // ignore if already removed
            }
        }

        return new CodeExecutionResponse(stdout.toString(), stderr.toString(), exitCode, timeout);
    }
    
    // Helper to pull image if not exists (usually done in build step, but good safety)
    public void pullImage() {
        try {
            dockerClient.pullImageCmd(IMAGE_NAME).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
