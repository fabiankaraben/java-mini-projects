package com.example.discovery;

import java.time.Instant;
import java.util.Objects;

public class ServiceInstance {
    private String serviceId;
    private String host;
    private int port;
    private Instant lastHeartbeat;
    private String status; // UP, DOWN

    public ServiceInstance() {}

    public ServiceInstance(String serviceId, String host, int port) {
        this.serviceId = serviceId;
        this.host = host;
        this.port = port;
        this.lastHeartbeat = Instant.now();
        this.status = "UP";
    }

    public void renew() {
        this.lastHeartbeat = Instant.now();
        this.status = "UP";
    }

    public void renew(Instant timestamp) {
        this.lastHeartbeat = timestamp;
        this.status = "UP";
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Instant getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Instant lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstance that = (ServiceInstance) o;
        return port == that.port && Objects.equals(serviceId, that.serviceId) && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, host, port);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", lastHeartbeat=" + lastHeartbeat +
                ", status='" + status + '\'' +
                '}';
    }
}
