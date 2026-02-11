package com.fabiankaraben.p2p;

import com.fabiankaraben.p2p.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class P2PFileSharingIntegrationTest {

    private ConfigurableApplicationContext peer1;
    private ConfigurableApplicationContext peer2;

    private static final String PEER1_STORAGE = "build/peer1-storage";
    private static final String PEER2_STORAGE = "build/peer2-storage";

    @AfterEach
    void tearDown() throws IOException {
        if (peer1 != null) peer1.close();
        if (peer2 != null) peer2.close();
        
        FileSystemUtils.deleteRecursively(Path.of(PEER1_STORAGE));
        FileSystemUtils.deleteRecursively(Path.of(PEER2_STORAGE));
    }

    @Test
    void testFileTransferBetweenPeers() throws IOException {
        // Start Peer 1
        peer1 = new SpringApplicationBuilder(P2PFileSharingApplication.class)
                .properties(
                        "server.port=8081",
                        "p2p.server.port=9091",
                        "p2p.storage.path=" + PEER1_STORAGE
                )
                .run();

        // Start Peer 2
        peer2 = new SpringApplicationBuilder(P2PFileSharingApplication.class)
                .properties(
                        "server.port=8082",
                        "p2p.server.port=9092",
                        "p2p.storage.path=" + PEER2_STORAGE
                )
                .run();

        // Create a file in Peer 1's storage
        FileService peer1FileService = peer1.getBean(FileService.class);
        String fileName = "test-file.txt";
        String content = "Hello P2P World!";
        Path peer1FilePath = Path.of(PEER1_STORAGE, fileName);
        Files.createDirectories(peer1FilePath.getParent());
        Files.writeString(peer1FilePath, content, StandardCharsets.UTF_8);

        // Verify Peer 1 has the file
        assertThat(peer1FileService.listFiles()).contains(fileName);

        // Trigger Peer 2 to download from Peer 1 (using Peer 2's Client)
        // We can invoke the controller or the service directly. Let's use the service bean for simplicity in test.
        // Or we can use TestRestTemplate if we want to test the controller.
        // Let's use TestRestTemplate to verify the full flow including the controller.
        
        // Actually, since I have the context, let's use the Controller bean to simulate the HTTP request action
        // or just use the PeerClient bean directly to test the P2P logic.
        // The requirement says "transferring a file between them".
        // Let's use the PeerClient from Peer 2.
        
        com.fabiankaraben.p2p.service.PeerClient peer2Client = peer2.getBean(com.fabiankaraben.p2p.service.PeerClient.class);
        peer2Client.downloadFile("localhost", 9091, fileName);

        // Verify Peer 2 has the file
        FileService peer2FileService = peer2.getBean(FileService.class);
        
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
             assertThat(peer2FileService.listFiles()).contains(fileName);
        });

        Path peer2FilePath = Path.of(PEER2_STORAGE, fileName);
        assertThat(Files.readString(peer2FilePath)).isEqualTo(content);
    }
}
