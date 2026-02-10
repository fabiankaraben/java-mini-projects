package com.example.consul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Consul Microservice!";
    }

    @GetMapping("/services")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }
    
    @GetMapping("/instances")
    public List<ServiceInstance> getInstances() {
        return discoveryClient.getInstances("consul-microservice");
    }
}
