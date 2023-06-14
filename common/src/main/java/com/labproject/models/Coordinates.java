package com.labproject.models;

public class Coordinates implements Model, Comparable<Coordinates> {
    private int x; //must be greater than -261
    private double y; //must be greater than -162
    private final int X_LOWER_BOUND = -261;
    private final double Y_LOWER_BOUND = -162;

    public Coordinates() {}
    public Coordinates(int x, double y) {
        if (x <= X_LOWER_BOUND || y <= Y_LOWER_BOUND)
            throw new RuntimeException("Invalid coordinates.");
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(x=%d, y=%f)", x, y);
    }

    @Override
    public int compareTo(Coordinates coordinates) {
        return Integer.compare(x, coordinates.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        if (x != that.x) return false;
        return Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = x;
        long temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
