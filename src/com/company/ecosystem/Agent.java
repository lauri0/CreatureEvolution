package com.company.ecosystem;

import java.util.Random;

public class Agent {
    Double[] location;
    Double[] speed;
    Double health;
    DNA dna;
    Double radius;
    Double maxSpeed;
    Random ran;

    boolean isDead() {
        return health < 0.0;
    }

    Double distance(Double[] point1, Double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

    Double map(Double x, Double in_min, Double in_max, Double out_min, Double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
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
