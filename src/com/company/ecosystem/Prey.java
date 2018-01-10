package com.company.ecosystem;

import java.util.ArrayList;
import java.util.Random;

public class Prey extends Agent {
    private static final double MAX_SPEED = 10.0;
    private static final double MAX_RADIUS = 50.0;
    private static final int PREDATOR_ALERT_RANGE = 100;
    private static final double MAX_ACCELERATION = 1.5;
    //private static final double REPRODUCTION_RATE = 0.004;
    //private static final double MUTATION_RATE = 0.15;
    private static final int REPRODUCTION_HEALTH_THRESHOLD = 50;
    private static final int REPRODUCTION_HEALTH_PENALTY = 25;
    private static final int BASIC_FOOD_NUTRITIONAL_VALUE = 100;
    private static final int ALTERNATE_FOOD_NUTRITIONAL_VALUE = 300;
    private static final double ALTERNATE_FOOD_SIZE_MULTIPLIER_THRESHOLD = 4;
    private static final double MAX_HEALTH = 500;
    //private static final double fearOfPredators = 0.5;

    private Double attractionToFood;
    private Double fearOfPredators;

    Prey(DNA dna_, Double x, Double y) {
        ran = new Random();
        location = new Double[2];
        location[0] = x;
        location[1] = y;
        speed = new Double[2];
        speed[0] = -2.5 + 5 * ran.nextDouble();
        speed[1] = -2.5 + 5 * ran.nextDouble();
        health = 200d;
        dna = dna_;
        maxSpeed = map(dna.genes[0], 0.0, 1.0, MAX_SPEED, 0.0);
        maxAcceleration = MAX_ACCELERATION;
        radius = map(dna.genes[0], 0.0, 1.0, 0.0, MAX_RADIUS);
        attractionToFood = map(dna.genes[1], 0.0, 1.0, -1.0, 1.0);
        fearOfPredators = map(dna.genes[2], 0.0, 1.0, -1.0, 1.0);
        System.out.println(fearOfPredators);
    }

    void eatBasic(ArrayList<BasicFood> basicFoodList) {
        for (int i = basicFoodList.size()-1; i >= 0; i--) {
            Double[] foodLocation = basicFoodList.get(i).getLocation();
            Double d = distance(location, foodLocation);
            if (d < radius + basicFoodList.get(i).getRadius()) {
                health += BASIC_FOOD_NUTRITIONAL_VALUE;
                if (health > MAX_HEALTH) {
                    health = MAX_HEALTH;
                }
                basicFoodList.remove(i);
            }
        }
    }

    void eatAlternate(ArrayList<AlternateFood> alternateFoodList) {
        for (int i = alternateFoodList.size()-1; i >= 0; i--) {
            Double[] foodLocation = alternateFoodList.get(i).getLocation();
            Double d = distance(location, foodLocation);
            if (d < radius + alternateFoodList.get(i).getRadius()
                    && radius < alternateFoodList.get(i).getRadius() * ALTERNATE_FOOD_SIZE_MULTIPLIER_THRESHOLD) {
                health += ALTERNATE_FOOD_NUTRITIONAL_VALUE;
                alternateFoodList.remove(i);
            }
        }
    }

    Prey reproduce(Double reproductionRate, Double mutationRate) {
        if (ran.nextDouble() < reproductionRate && health > REPRODUCTION_HEALTH_THRESHOLD) {
            DNA childDNA = dna.copy();
            health -= REPRODUCTION_HEALTH_PENALTY;

            // There is a probability for a mutation to occur
            if (ran.nextDouble() < mutationRate) {
                childDNA = new DNA();
            }
            return new Prey(childDNA, location[0], location[1]);
        } else {
            return null;
        }
    }

    Double[] normalizedVectorBetweenPoints(Double[] point1, Double[] point2){
        Double dist = distance(point1, point2);
        Double vx = (point2[0] - point1[0])/dist;
        Double vy = (point2[1] - point1[1])/dist;

        return new Double[]{vx, vy};
    }

    void moveAccordingToSituation(ArrayList<BasicFood> foodList, ArrayList<Predator> predatorList, int worldWidth, int worldHeight) {
        ArrayList<Predator> nearPredators = new ArrayList<>();
        for(Predator p : predatorList){
            if(distance(p.getLocation(), location) < PREDATOR_ALERT_RANGE && radius < p.getRadius() ){
                nearPredators.add(p);
            }
        }

        if(!nearPredators.isEmpty()){
            //System.out.println("Predators near: " + nearPredators.size());
            Double[][] predatorVectors = new Double[nearPredators.size()][2];
            for(int i = 0; i<nearPredators.size(); i++){
                predatorVectors[i] = normalizedVectorBetweenPoints(location, nearPredators.get(i).getLocation());
            }

            Double[] vectorSum = new Double[]{0.0, 0.0};
            for(Double[] v : predatorVectors){
                vectorSum[0] += v[0];
                vectorSum[1] += v[1];
            }

            Double[] finalVector = normalizedVectorBetweenPoints(new Double[]{0.0, 0.0}, vectorSum);
            Double[] targetLocation = new Double[]{location[0] + -1*finalVector[0]*100, location[1] + -1*finalVector[1]*100};
            accelerate(targetLocation, fearOfPredators);
            move(worldWidth, worldHeight);
        }
        else {
            Double closestDistance = 1000000.0;
            Double closestFoodXCoord = 1000000.0;
            Double closestFoodYCoord = 1000000.0;
            for (Food food : foodList) {
                Double[] otherLocation = food.getLocation();
                Double dist = distance(location, otherLocation);
                if (dist < closestDistance && food.getRadius() < radius) {
                    closestDistance = dist;
                    closestFoodXCoord = food.getLocation()[0];
                    closestFoodYCoord = food.getLocation()[1];
                }
            }
            if (closestDistance < 1000000.0) {
                // Choose a target location opposite to where the closest Predator is
                Double[] targetLocation = {closestFoodXCoord * 1, closestFoodYCoord * 1};
                Double modifier = attractionToFood;
                accelerate(targetLocation, modifier);
                move(worldWidth, worldHeight);
            }
            else {
                moveRandomly(worldWidth, worldHeight, MAX_ACCELERATION);
            }

        }

    }

    void update(ArrayList<BasicFood> foodList, ArrayList<Predator> predatorList, int worldWidth, int worldHeight) {
        moveAccordingToSituation(foodList, predatorList, worldWidth, worldHeight);

        health -= 1;
    }
}
