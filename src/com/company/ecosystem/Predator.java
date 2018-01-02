package com.company.ecosystem;

import java.util.ArrayList;
import java.util.Random;

public class Predator extends Agent {
    private static final double STARTING_HEALTH = 250d;
    private static final double MAX_SPEED = 30.0;
    private static final double MAX_RADIUS = 50.0;
    private static final double MAX_ACCELERATION = 1;
    private static final double REPRODUCTION_RATE = 0.0025;
    private static final double MUTATION_RATE = 0.15;
    private static final int REPRODUCTION_HEALTH_THRESHOLD = 50;
    private static final int REPRODUCTION_HEALTH_PENALTY = 25;

    Predator(DNA dna_, Double x, Double y) {
        ran = new Random();
        location = new Double[2];
        location[0] = x;
        location[1] = y;
        speed = new Double[2];
        speed[0] = 5.0;
        speed[1] = 5.0;
        health = STARTING_HEALTH;
        dna = dna_;
        maxSpeed = map(dna.genes[0], 0.0, 1.0, MAX_SPEED, 0.0);
        radius = map(dna.genes[0], 0.0, 1.0, 0.0, MAX_RADIUS);
    }

    void eat(ArrayList<Prey> preyList) {
        for (int i = preyList.size()-1; i >= 0; i--) {
            Double[] foodLocation = preyList.get(i).getLocation();
            Double d = distance(location, foodLocation);
            if (d < radius && preyList.get(i).getRadius() < radius) {
                health += 200;
                preyList.remove(i);
            }
        }
    }

    Predator reproduce() {
        if (ran.nextDouble() < REPRODUCTION_RATE && health > REPRODUCTION_HEALTH_THRESHOLD) {
            DNA childDNA = dna.copy();
            health -= REPRODUCTION_HEALTH_PENALTY;

            // There is a probability for a mutation to occur
            if (ran.nextDouble() < MUTATION_RATE) {
                childDNA = new DNA();
            }
            return new Predator(childDNA, location[0], location[1]);
        } else {
            return null;
        }
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

        // Updated the Predator instance location based on the speed
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
}