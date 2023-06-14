package com.labproject.models;

public class Location implements Model {
    private double x;
    private double y;
    private Long z; //cannot be null
    private String name; //can be null

    public Location() {}
    public Location(double x, double y, Long z) {
        if (z == null)
            throw new RuntimeException("Invalid location.");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(double x, double y, Long z, String name) {
        this(x, y, z);
        this.name = name;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Long getZ() {
        return z;
    }
    public String getName() {
        return name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s(x=%f, y=%f, z=%d)", name==null ? "" : name, x, y, z);
    }
}
