package com.example.blockchain;

import com.example.blockchain.service.BlockchainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.web3j.protocol.Web3j;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BlockchainIntegrationTest {

    @Container
    static GenericContainer<?> ganache = new GenericContainer<>(DockerImageName.parse("trufflesuite/ganache:latest"))
            .withExposedPorts(8545)
            .withCommand("--wallet.totalAccounts 10 --wallet.defaultBalance 100");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Web3j web3j;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("ethereum.node.url", () -> "http://" + ganache.getHost() + ":" + ganache.getMappedPort(8545));
    }

    @Test
    void shouldReturnClientVersion() throws Exception {
        mockMvc.perform(get("/api/blockchain/version"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", containsString("EthereumJS")));
    }

    @Test
    void shouldReturnBlockNumber() throws Exception {
        mockMvc.perform(get("/api/blockchain/block-number"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blockNumber").exists());
    }

    @Test
    void shouldReturnBalance() throws Exception {
        // Get an account from Web3j directly to use for testing
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        String address = accounts.get(0);

        mockMvc.perform(get("/api/blockchain/balance/" + address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.balance_eth").value("100"));
    }
}
