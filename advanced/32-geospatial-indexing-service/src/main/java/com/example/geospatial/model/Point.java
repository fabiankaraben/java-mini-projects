package com.example.geospatial.model;

import java.util.Objects;

public class Point {
    private double latitude;
    private double longitude;
    private String id;
    private Object data;

    public Point() {
    }

    public Point(double latitude, double longitude, String id, Object data) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.data = data;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.latitude, latitude) == 0 &&
                Double.compare(point.longitude, longitude) == 0 &&
                Objects.equals(id, point.id) &&
                Objects.equals(data, point.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, id, data);
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                ", data=" + data +
                '}';
    }
}
