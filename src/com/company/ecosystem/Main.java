package com.company.ecosystem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel{
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int MS_TO_WAIT = 1000 / FRAMES_PER_SECOND;
    private static final int STARTING_FOOD_COUNT = 20;
    private static final int STARTING_PREY_COUNT = 8;
    private static final int FOOD_RADIUS = 5;
    private static final double FOOD_SPAWN_CHANCE_PER_FRAME = 0.05;
    private static final Color PREY_COLOR = Color.BLUE;
    private static final Color FOOD_COLOR = Color.GREEN;

    private Timer animationTimer;
    private TimerTask animationTask;
    private ArrayList<Prey> preys;
    private ArrayList<Food> foodList;
    private Random ran;

    public Main() {
        ran = new Random();
        preys = new ArrayList<>();
        foodList = new ArrayList<>();
        initializeFood();

        for (int i = 0; i < STARTING_PREY_COUNT; i++) {
            DNA dna = new DNA();
            Double x = WORLD_WIDTH * 0.25 + ran.nextDouble() * WORLD_WIDTH / 2;
            Double y = WORLD_HEIGHT * 0.25 + ran.nextDouble() * WORLD_HEIGHT / 2;
            preys.add(new Prey(dna, x, y));
        }

        animationTimer = new Timer();
        animationTask = new AnimationUpdater();
    }

    private void initializeFood() {
        for (int i = 0; i < STARTING_FOOD_COUNT; i++) {
            Food newFood = new Food(ran.nextInt(WORLD_WIDTH), ran.nextInt(WORLD_HEIGHT), FOOD_RADIUS);
            foodList.add(newFood);
        }
    }

    private void start() {
        animationTimer.schedule(animationTask, 0, MS_TO_WAIT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(PREY_COLOR);
        for (Prey prey : preys) {
            int upperLeftX = (int) (prey.getLocation()[0] - prey.getRadius());
            int upperLeftY = (int) (prey.getLocation()[1] - prey.getRadius());
            g.drawOval(upperLeftX, upperLeftY, prey.getRadius().intValue() * 2, prey.getRadius().intValue() * 2);
        }

        g.setColor(FOOD_COLOR);
        for (Food food : foodList) {
            int upperLeftX = (int) (food.getLocation()[0] - FOOD_RADIUS);
            int upperLeftY = (int) (food.getLocation()[1] - FOOD_RADIUS);
            g.fillOval(upperLeftX, upperLeftY, FOOD_RADIUS * 2, FOOD_RADIUS * 2);
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
            ArrayList<Prey> bornPreys = new ArrayList<>();
            for (Iterator<Prey> iterator = preys.iterator() ; iterator.hasNext(); ) {
                Prey prey = iterator.next();
                prey.update(WORLD_WIDTH, WORLD_HEIGHT);

                prey.eat(foodList);
                if (prey.isDead()) {
                    iterator.remove();
                }

                Prey child = prey.reproduce();
                if (child != null) {
                    bornPreys.add(child);
                }
            }
            preys.addAll(bornPreys);

            if (ran.nextDouble() < FOOD_SPAWN_CHANCE_PER_FRAME) {
                foodList.add(new Food(WORLD_WIDTH, WORLD_HEIGHT, FOOD_RADIUS));
            }

            repaint();
        }
    }
}
