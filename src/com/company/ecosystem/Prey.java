package com.company.ecosystem;

import java.util.ArrayList;
import java.util.Random;

public class Prey {
    private static final double MAX_SPEED = 15.0;
    private static final double MAX_RADIUS = 50.0;
    private static final double MAX_ACCELERATION = 1;
    private static final double REPRODUCTION_RATE = 0.0035;
    private static final double MUTATION_RATE = 0.03;
    private static final int REPRODUCTION_HEALTH_THRESHOLD = 50;
    private static final int REPRODUCTION_HEALTH_PENALTY = 25;

    private Double[] location;
    private Double[] speed;
    private Double health;
    private DNA dna;
    private Double radius;
    private Double maxSpeed;
    private Random ran;

    Prey(DNA dna_, Double x, Double y) {
        ran = new Random();
        location = new Double[2];
        location[0] = x;
        location[1] = y;
        speed = new Double[2];
        speed[0] = 5.0;
        speed[1] = 5.0;
        health = 200d;
        dna = dna_;
        maxSpeed = map(dna.genes[0], 0.0, 1.0, MAX_SPEED, 0.0);
        radius = map(dna.genes[0], 0.0, 1.0, 0.0, MAX_RADIUS);
    }

    void eat(ArrayList<Food> foodList) {
        for (int i = foodList.size()-1; i >= 0; i--) {
            Double[] foodLocation = foodList.get(i).getLocation();
            Double d = distance(location, foodLocation);
            if (d < radius) {
                health += 100;
                foodList.remove(i);
            }
        }
    }

    Prey reproduce() {
        if (ran.nextDouble() < REPRODUCTION_RATE && health > REPRODUCTION_HEALTH_THRESHOLD) {
            DNA childDNA = dna.copy();
            health -= REPRODUCTION_HEALTH_PENALTY;

            // There is a probability for a mutation to occur
            if (ran.nextDouble() < MUTATION_RATE) {
                childDNA = new DNA();
            }
            return new Prey(childDNA, location[0], location[1]);
        } else {
            return null;
        }
    }

    boolean isDead() {
        return health < 0.0;
    }

    void update(int worldWidth, int worldHeight) {
        Double dvx = map(ran.nextDouble(), 0.0, 1.0, -MAX_ACCELERATION, MAX_ACCELERATION);
        Double dvy = map(ran.nextDouble(), 0.0, 1.0, -MAX_ACCELERATION, MAX_ACCELERATION);
        speed[0] += dvx;
        speed[1] += dvy;

        // Enforce maximum speed
        Double currentSpeed = Math.sqrt(Math.pow(speed[0], 2) + Math.pow(speed[1], 2));
        if (currentSpeed > maxSpeed) {
            speed[0] = speed[0] * maxSpeed / currentSpeed;
            speed[1] = speed[1] * maxSpeed / currentSpeed;
        }

        // Updated the Prey instance location based on the speed
        Double dx = speed[0];
        Double dy = speed[1];
        location[0] += dx;
        location[1] += dy;

        if (location[0] > worldWidth || location[0] < 0) {
            location[0] = location[0] - worldWidth;
        }
        else if (location[0] < 0) {
            location[0] = worldWidth - location[0];
        }
        else if (location[1] > worldHeight) {
            location[1] = location[1] - worldHeight;
        }
        else if (location[1] < 0) {
            location[1] = worldHeight - location[1];
        }

        health -= 1;
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
