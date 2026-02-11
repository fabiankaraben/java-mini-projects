package com.example.geolocation.service;

import com.example.geolocation.model.GeolocationResponse;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeolocationService {

    private final DatabaseReader databaseReader;

    public GeolocationService(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public GeolocationResponse getLocation(String ipAddress) throws IOException, GeoIp2Exception {
        InetAddress ip = InetAddress.getByName(ipAddress);
        CityResponse response = databaseReader.city(ip);

        String country = response.getCountry().getName();
        String city = response.getCity().getName();
        Double latitude = response.getLocation().getLatitude();
        Double longitude = response.getLocation().getLongitude();

        return new GeolocationResponse(ipAddress, country, city, latitude, longitude);
    }
}
