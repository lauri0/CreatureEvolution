package com.company.ecosystem;

import java.util.Random;

public class Food {
    private Double[] location;
    private Double radius;
    private Random ran;

    Food(int worldWidth, int worldHeight, int foodRadius) {
        ran = new Random();
        location = new Double[2];
        location[0] = ran.nextDouble() * worldWidth;
        location[1] = ran.nextDouble() * worldHeight;
        radius = foodRadius * 1.0;
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
}
