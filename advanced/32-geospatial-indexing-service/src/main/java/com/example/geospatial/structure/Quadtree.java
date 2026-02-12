package com.example.geospatial.structure;

import com.example.geospatial.model.Boundary;
import com.example.geospatial.model.Point;

import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    private static final int CAPACITY = 4;
    private Boundary boundary;
    private List<Point> points;
    private Quadtree northWest;
    private Quadtree northEast;
    private Quadtree southWest;
    private Quadtree southEast;
    private boolean divided;

    public Quadtree(Boundary boundary) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    public boolean insert(Point point) {
        if (!boundary.contains(point)) {
            return false;
        }

        if (points.size() < CAPACITY) {
            points.add(point);
            return true;
        }

        if (!divided) {
            subdivide();
        }

        if (northWest.insert(point)) return true;
        if (northEast.insert(point)) return true;
        if (southWest.insert(point)) return true;
        if (southEast.insert(point)) return true;

        return false; // Should not reach here if logic is correct
    }

    private void subdivide() {
        double xMin = boundary.getXMin();
        double yMin = boundary.getYMin();
        double xMax = boundary.getXMax();
        double yMax = boundary.getYMax();
        
        double xMid = (xMin + xMax) / 2;
        double yMid = (yMin + yMax) / 2;

        northWest = new Quadtree(new Boundary(xMin, yMid, xMid, yMax));
        northEast = new Quadtree(new Boundary(xMid, yMid, xMax, yMax));
        southWest = new Quadtree(new Boundary(xMin, yMin, xMid, yMid));
        southEast = new Quadtree(new Boundary(xMid, yMin, xMax, yMid));

        divided = true;
    }

    public List<Point> query(Boundary range) {
        List<Point> found = new ArrayList<>();
        if (!boundary.intersects(range)) {
            return found;
        }

        for (Point p : points) {
            if (range.contains(p)) {
                found.add(p);
            }
        }

        if (divided) {
            found.addAll(northWest.query(range));
            found.addAll(northEast.query(range));
            found.addAll(southWest.query(range));
            found.addAll(southEast.query(range));
        }

        return found;
    }
}
