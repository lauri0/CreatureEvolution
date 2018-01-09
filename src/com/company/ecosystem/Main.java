package com.company.ecosystem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel{
    private static final int WORLD_WIDTH = 1400;
    private static final int WORLD_HEIGHT = 1000;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int MS_TO_WAIT = 1000 / FRAMES_PER_SECOND;
    private static final int STARTING_BASIC_FOOD_COUNT = 60;
    private static final int STARTING_ALTERNATE_FOOD_COUNT = 0;
    private static final int STARTING_PREY_COUNT = 15;
    private static final int STARTING_PREDATOR_COUNT = 5;
    private static final int BASIC_FOOD_RADIUS = 5;
    private static final int ALTERNATE_FOOD_RADIUS = 5;
    private static final double BASIC_FOOD_SPAWN_RATE = 0.2;
    private static final double ALTERNATE_FOOD_SPAWN_RATE = 0.10;
    private static final Color BASIC_FOOD_COLOR = Color.GREEN;
    private static final Color ALTERNATE_FOOD_COLOR = Color.GRAY;
    private static final Color PREY_COLOR = Color.BLUE;
    private static final Color PREDATOR_COLOR = Color.RED;

    private Timer animationTimer;
    private TimerTask animationTask;
    private ArrayList<Prey> preyList;
    private ArrayList<BasicFood> basicFoodList;
    private ArrayList<AlternateFood> alternateFoodList;
    private ArrayList<Predator> predatorList;
    private Random ran;

    public Main() {
        ran = new Random();
        preyList = new ArrayList<>();
        basicFoodList = new ArrayList<>();
        alternateFoodList = new ArrayList<>();
        predatorList = new ArrayList<>();
        initializeFood();
        initializePrey();
        initializePredators();

        animationTimer = new Timer();
        animationTask = new AnimationUpdater();
    }

    private void initializeFood() {
        for (int i = 0; i < STARTING_BASIC_FOOD_COUNT; i++) {
            BasicFood newBasicFood = new BasicFood(WORLD_WIDTH, WORLD_HEIGHT, BASIC_FOOD_RADIUS);
            basicFoodList.add(newBasicFood);
        }
        for (int i = 0; i < STARTING_ALTERNATE_FOOD_COUNT; i++) {
            AlternateFood newAlternateFood = new AlternateFood(WORLD_WIDTH, WORLD_HEIGHT, ALTERNATE_FOOD_RADIUS);
            alternateFoodList.add(newAlternateFood);
        }
    }

    private void initializePrey() {
        for (int i = 0; i < STARTING_PREY_COUNT; i++) {
            DNA dna = new DNA();
            Double x = WORLD_WIDTH * 0.25 + ran.nextDouble() * WORLD_WIDTH / 2;
            Double y = WORLD_HEIGHT * 0.25 + ran.nextDouble() * WORLD_HEIGHT / 2;
            preyList.add(new Prey(dna, x, y));
        }
    }

    private void initializePredators() {
        for (int i = 0; i < STARTING_PREDATOR_COUNT; i++) {
            DNA dna = new DNA();
            Double x = ran.nextDouble() * WORLD_WIDTH;
            Double y = ran.nextDouble() * WORLD_HEIGHT;
            predatorList.add(new Predator(dna, x, y));
        }
    }

    private void start() {
        animationTimer.schedule(animationTask, 0, MS_TO_WAIT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(PREDATOR_COLOR);
        for (Predator predator : predatorList) {
            int upperLeftX = (int) (predator.getLocation()[0] - predator.getRadius());
            int upperLeftY = (int) (predator.getLocation()[1] - predator.getRadius());
            g.drawOval(upperLeftX, upperLeftY, predator.getRadius().intValue() * 2, predator.getRadius().intValue() * 2);
        }

        g.setColor(PREY_COLOR);
        for (Prey prey : preyList) {
            int upperLeftX = (int) (prey.getLocation()[0] - prey.getRadius());
            int upperLeftY = (int) (prey.getLocation()[1] - prey.getRadius());
            g.drawOval(upperLeftX, upperLeftY, prey.getRadius().intValue() * 2, prey.getRadius().intValue() * 2);
        }

        g.setColor(BASIC_FOOD_COLOR);
        for (BasicFood basicFood : basicFoodList) {
            int upperLeftX = (int) (basicFood.getLocation()[0] - BASIC_FOOD_RADIUS);
            int upperLeftY = (int) (basicFood.getLocation()[1] - BASIC_FOOD_RADIUS);
            g.fillOval(upperLeftX, upperLeftY, BASIC_FOOD_RADIUS * 2, BASIC_FOOD_RADIUS * 2);
        }

        g.setColor(ALTERNATE_FOOD_COLOR);
        for (AlternateFood alternateFood : alternateFoodList) {
            int upperLeftX = (int) (alternateFood.getLocation()[0] - ALTERNATE_FOOD_RADIUS);
            int upperLeftY = (int) (alternateFood.getLocation()[1] - ALTERNATE_FOOD_RADIUS);
            g.fillOval(upperLeftX, upperLeftY, ALTERNATE_FOOD_RADIUS * 2, ALTERNATE_FOOD_RADIUS * 2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WORLD_WIDTH, WORLD_HEIGHT);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Main panel = new Main();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.start();
    }

    private class AnimationUpdater extends TimerTask {
        @Override
        public void run() {
            ArrayList<Prey> bornPreyList = new ArrayList<>();
            for (Iterator<Prey> iterator = preyList.iterator() ; iterator.hasNext(); ) {
                Prey prey = iterator.next();
                prey.update(basicFoodList, WORLD_WIDTH, WORLD_HEIGHT);

                prey.eatBasic(basicFoodList);
                //prey.eatAlternate(alternateFoodList);
                if (prey.isDead()) {
                    iterator.remove();
                }

                Prey child = prey.reproduce();
                if (child != null) {
                    bornPreyList.add(child);
                }
            }
            preyList.addAll(bornPreyList);

            ArrayList<Predator> bornPredatorList = new ArrayList<>();
            for (Iterator<Predator> iterator = predatorList.iterator() ; iterator.hasNext(); ) {
                Predator predator = iterator.next();
                predator.update(preyList, WORLD_WIDTH, WORLD_HEIGHT);

                predator.eat(preyList);
                if (predator.isDead()) {
                    iterator.remove();
                }

                Predator child = predator.reproduce();
                if (child != null) {
                    bornPredatorList.add(child);
                }
            }
            predatorList.addAll(bornPredatorList);

            double randomDouble = ran.nextDouble();
            if (randomDouble < BASIC_FOOD_SPAWN_RATE) {
                basicFoodList.add(new BasicFood(WORLD_WIDTH, WORLD_HEIGHT, BASIC_FOOD_RADIUS));
            }
            /*if (randomDouble < ALTERNATE_FOOD_SPAWN_RATE) {
                alternateFoodList.add(new AlternateFood(WORLD_WIDTH, WORLD_HEIGHT, ALTERNATE_FOOD_RADIUS));
            }*/

            repaint();
        }
    }
}
