package com.company.ecosystem;

import java.util.Random;

public class Agent {
    Double[] location;
    Double[] speed;
    Double health;
    DNA dna;
    Double radius;
    Double maxSpeed;
    Double maxAcceleration;
    Random ran;

    boolean isDead() {
        return health < 0.0;
    }

    void accelerate(Double[] targetLocation, Double modifier) {
        // Calculate the deltas in order to move at max speed in the direction of the target
        Double dist = distance(location, targetLocation);
        Double deltaX = targetLocation[0] - location[0];
        Double deltaY = targetLocation[1] - location[1];

        // Modifier depends on one or more of the genes which Agents have. Can be negative.
        Double accelerationX = maxAcceleration * modifier * deltaX / dist;
        Double accelerationY = maxAcceleration * modifier * deltaY / dist;

        // Update the speed
        speed[0] += accelerationX;
        speed[1] += accelerationY;

        // Enforce maximum speed
        Double currentSpeed = Math.sqrt(Math.pow(speed[0], 2) + Math.pow(speed[1], 2));
        if (currentSpeed > maxSpeed) {
            speed[0] = speed[0] * maxSpeed / currentSpeed;
            speed[1] = speed[1] * maxSpeed / currentSpeed;
        }
    }

    void decelerate() {
        speed[0] = speed[0] * 0.95;
        speed[1] = speed[1] * 0.95;
        if (speed[0] < 0.4 && speed[0] > 0.0) {
            speed[0] = 0.0;
            System.out.println("vx = 0");
        }
        if (speed[1] < 0.4 && speed[1] > 0.0) {
            speed[1] = 0.0;
            System.out.println("vy = 0");
        }
    }

    void move(int worldWidth, int worldHeight) {
        // Update location based on speed
        Double dx = speed[0];
        Double dy = speed[1];
        location[0] += dx;
        location[1] += dy;

        handleOutOfScreen(worldWidth, worldHeight);
    }

    void moveRandomly(int worldWidth, int worldHeight, double maxAcceleration) {
        Double dvx = map(ran.nextDouble(), 0.0, 1.0, -maxAcceleration, maxAcceleration);
        Double dvy = map(ran.nextDouble(), 0.0, 1.0, -maxAcceleration, maxAcceleration);
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

        handleOutOfScreen(worldWidth, worldHeight);
    }

    void handleOutOfScreen(int worldWidth, int worldHeight) {
        if (location[0] > worldWidth) {
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
    }

    Double distance(Double[] point1, Double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

    Double map(Double x, Double in_min, Double in_max, Double out_min, Double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public DNA getDNA() {
        return dna;
    }

    public void setDNA(DNA dna) {
        this.dna = dna;
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
