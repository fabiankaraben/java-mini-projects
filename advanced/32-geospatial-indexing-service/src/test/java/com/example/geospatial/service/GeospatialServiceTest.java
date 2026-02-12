package com.example.geospatial.service;

import com.example.geospatial.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeospatialServiceTest {

    private GeospatialService geospatialService;

    @BeforeEach
    void setUp() {
        geospatialService = new GeospatialService();
    }

    @Test
    void testAddAndFindPointsWithinRadius() {
        // Point in New York City (Times Square roughly)
        Point nyc = new Point(40.7580, -73.9855, "nyc", "Times Square");
        geospatialService.addPoint(nyc);

        // Point in London
        Point london = new Point(51.5074, -0.1278, "london", "London Eye");
        geospatialService.addPoint(london);

        // Point in Central Park (close to Times Square)
        Point centralPark = new Point(40.7829, -73.9654, "central_park", "Central Park");
        geospatialService.addPoint(centralPark);

        // Search near Times Square with 5km radius
        // Central Park is about 3-4 km away. London is far.
        List<Point> results = geospatialService.findPointsWithinRadius(40.7580, -73.9855, 5.0);

        assertEquals(2, results.size(), "Should find NYC and Central Park");
        assertTrue(results.stream().anyMatch(p -> p.getId().equals("nyc")));
        assertTrue(results.stream().anyMatch(p -> p.getId().equals("central_park")));
        assertFalse(results.stream().anyMatch(p -> p.getId().equals("london")));
    }

    @Test
    void testBoundaryConditions() {
        // Test edge cases
        Point p1 = new Point(0, 0, "origin", "Origin");
        geospatialService.addPoint(p1);

        List<Point> results = geospatialService.findPointsWithinRadius(0, 0, 1.0);
        assertEquals(1, results.size());
        assertEquals("origin", results.get(0).getId());
    }
    
    @Test
    void testEmptyResult() {
         Point nyc = new Point(40.7580, -73.9855, "nyc", "Times Square");
         geospatialService.addPoint(nyc);
         
         // Search in Antarctica
         List<Point> results = geospatialService.findPointsWithinRadius(-82.8628, 135.0000, 10.0);
         assertTrue(results.isEmpty());
    }
}
