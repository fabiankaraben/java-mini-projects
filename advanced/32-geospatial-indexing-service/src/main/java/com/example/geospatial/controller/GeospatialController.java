package com.example.geospatial.controller;

import com.example.geospatial.model.Point;
import com.example.geospatial.service.GeospatialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geospatial")
public class GeospatialController {

    private final GeospatialService geospatialService;

    public GeospatialController(GeospatialService geospatialService) {
        this.geospatialService = geospatialService;
    }

    @PostMapping("/points")
    public ResponseEntity<String> addPoint(@RequestBody Point point) {
        geospatialService.addPoint(point);
        return ResponseEntity.ok("Point added successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Point>> searchPoints(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double radius) {
        List<Point> points = geospatialService.findPointsWithinRadius(lat, lon, radius);
        return ResponseEntity.ok(points);
    }
}
