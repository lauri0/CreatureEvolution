package com.company.ecosystem;

import java.util.Random;

public abstract class Food {
    Double[] location;
    Double radius;
    Random ran;

    Food () {
        ran = new Random();
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Random getRan() {
        return ran;
    }

    public void setRan(Random ran) {
        this.ran = ran;
    }
}
