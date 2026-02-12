package com.example.geospatial.model;

import java.util.Objects;

public class Boundary {
    private double xMin; // longitude min
    private double yMin; // latitude min
    private double xMax; // longitude max
    private double yMax; // latitude max

    public Boundary() {
    }

    public Boundary(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public double getXMin() {
        return xMin;
    }

    public void setXMin(double xMin) {
        this.xMin = xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(double yMin) {
        this.yMin = yMin;
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(double xMax) {
        this.xMax = xMax;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(double yMax) {
        this.yMax = yMax;
    }

    public boolean contains(Point p) {
        return p.getLongitude() >= xMin &&
               p.getLongitude() <= xMax &&
               p.getLatitude() >= yMin &&
               p.getLatitude() <= yMax;
    }

    public boolean intersects(Boundary other) {
        return !(other.xMin > xMax ||
                 other.xMax < xMin ||
                 other.yMin > yMax ||
                 other.yMax < yMin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boundary boundary = (Boundary) o;
        return Double.compare(boundary.xMin, xMin) == 0 &&
                Double.compare(boundary.yMin, yMin) == 0 &&
                Double.compare(boundary.xMax, xMax) == 0 &&
                Double.compare(boundary.yMax, yMax) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xMin, yMin, xMax, yMax);
    }

    @Override
    public String toString() {
        return "Boundary{" +
                "xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                '}';
    }
}
