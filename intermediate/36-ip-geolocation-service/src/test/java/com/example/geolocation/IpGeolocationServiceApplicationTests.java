package com.example.geolocation;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IpGeolocationServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseReader databaseReader;

    @Test
    void testGetGeolocation_Success() throws Exception {
        // Mock MaxMind CityResponse
        List<String> locales = Collections.singletonList("en");
        Map<String, String> countryNames = Collections.singletonMap("en", "United States");
        // Country(List<String> locales, Integer confidence, Long geonameId, Boolean isInEuropeanUnion, String isoCode, Map<String, String> names)
        Country country = new Country(locales, null, null, false, "US", countryNames);

        Map<String, String> cityNames = Collections.singletonMap("en", "Mountain View");
        // City(List<String> locales, Integer confidence, Long geonameId, Map<String, String> names)
        City city = new City(locales, null, null, cityNames);

        // Location(Integer averageIncome, Integer populationDensity, Double latitude, Double longitude, Integer metroCode, Integer accuracyRadius, String timeZone)
        Location location = new Location(null, null, 37.4223, -122.0848, null, null, "America/Los_Angeles");
        
        CityResponse cityResponse = new CityResponse(city, null, country, location, null, null, null, null, null, null);

        when(databaseReader.city(any(InetAddress.class))).thenReturn(cityResponse);

        mockMvc.perform(get("/api/geolocation/8.8.8.8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ipAddress").value("8.8.8.8"))
                .andExpect(jsonPath("$.country").value("United States"))
                .andExpect(jsonPath("$.city").value("Mountain View"))
                .andExpect(jsonPath("$.latitude").value(37.4223))
                .andExpect(jsonPath("$.longitude").value(-122.0848));
    }

    @Test
    void testGetGeolocation_InvalidIp() throws Exception {
        mockMvc.perform(get("/api/geolocation/invalid-ip"))
                .andExpect(status().isBadRequest());
    }
}
