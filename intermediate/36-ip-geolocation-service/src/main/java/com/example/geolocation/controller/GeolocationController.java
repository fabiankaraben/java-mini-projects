package com.example.geolocation.controller;

import com.example.geolocation.model.GeolocationResponse;
import com.example.geolocation.service.GeolocationService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/geolocation")
public class GeolocationController {

    private final GeolocationService geolocationService;

    public GeolocationController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @GetMapping("/{ipAddress}")
    public ResponseEntity<GeolocationResponse> getGeolocation(@PathVariable String ipAddress) {
        try {
            GeolocationResponse response = geolocationService.getLocation(ipAddress);
            return ResponseEntity.ok(response);
        } catch (UnknownHostException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP address", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading GeoIP database", e);
        } catch (GeoIp2Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "IP address not found in database", e);
        } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP address or request", e);
        }
    }
}
