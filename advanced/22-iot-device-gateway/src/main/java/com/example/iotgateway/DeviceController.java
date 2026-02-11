package com.example.iotgateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final MqttGatewayService mqttGatewayService;

    public DeviceController(MqttGatewayService mqttGatewayService) {
        this.mqttGatewayService = mqttGatewayService;
    }

    @PostMapping("/{deviceId}/command")
    public CompletableFuture<ResponseEntity<String>> sendCommand(@PathVariable String deviceId, @RequestBody String command) {
        return mqttGatewayService.sendCommand(deviceId, command)
                .thenApply(v -> ResponseEntity.ok("Command sent to " + deviceId))
                .exceptionally(ex -> ResponseEntity.internalServerError().body("Failed to send command: " + ex.getMessage()));
    }

    @GetMapping("/{deviceId}/data")
    public ResponseEntity<String> getLastData(@PathVariable String deviceId) {
        String data = mqttGatewayService.getLastMessage(deviceId);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getAllDevices() {
        return ResponseEntity.ok(mqttGatewayService.getAllDeviceMessages());
    }
}
