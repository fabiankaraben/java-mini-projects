package com.example.geospatial.service;

import com.example.geospatial.model.Boundary;
import com.example.geospatial.model.Point;
import com.example.geospatial.structure.Quadtree;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeospatialService {

    private final Quadtree quadtree;

    public GeospatialService() {
        // Initialize with a world-wide boundary
        // Latitude: -90 to 90
        // Longitude: -180 to 180
        this.quadtree = new Quadtree(new Boundary(-180, -90, 180, 90));
    }

    public void addPoint(Point point) {
        quadtree.insert(point);
    }

    public List<Point> findPointsWithinRadius(double lat, double lon, double radiusKm) {
        // 1 degree of latitude is approximately 111 km
        // 1 degree of longitude varies, but max is ~111 km at equator
        
        double latChange = radiusKm / 111.0;
        double lonChange = Math.abs(radiusKm / (111.0 * Math.cos(Math.toRadians(lat))));

        Boundary searchBoundary = new Boundary(
                lon - lonChange,
                lat - latChange,
                lon + lonChange,
                lat + latChange
        );

        List<Point> candidates = quadtree.query(searchBoundary);

        return candidates.stream()
                .filter(p -> calculateDistance(lat, lon, p.getLatitude(), p.getLongitude()) <= radiusKm)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
