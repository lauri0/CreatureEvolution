package com.company.ecosystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel{
    private static final int CONTROL_PANEL_WIDTH = 250;
    private static final int CONTROL_PANEL_HEIGHT = 1000;
    private static final int WORLD_WIDTH = 1400;
    private static final int WORLD_HEIGHT = 1000;
    private static final int SCREEN_WIDTH = 1650;
    private static final int SCREEN_HEIGHT = 1000;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int MS_TO_WAIT = 1000 / FRAMES_PER_SECOND;
    private static final int STARTING_BASIC_FOOD_COUNT = 30;
    private static final int STARTING_ALTERNATE_FOOD_COUNT = 30;
    private static final int STARTING_PREY_COUNT = 15;
    private static final int STARTING_PREDATOR_COUNT = 11;
    private static final int BASIC_FOOD_RADIUS = 5;
    private static final int ALTERNATE_FOOD_RADIUS = 5;
    private static final double BASIC_FOOD_SPAWN_RATE = 0.13;
    private static final double ALTERNATE_FOOD_SPAWN_RATE = 0.10;
    private static final Color BASIC_FOOD_COLOR = Color.GREEN;
    private static final Color ALTERNATE_FOOD_COLOR = Color.GRAY;
    private static final Color PREY_COLOR = Color.BLUE;
    private static final Color PREDATOR_COLOR = Color.RED;
    private static final String BASIC_FOOD_STR = "Basic food";
    private static final String ALT_FOOD_STR = "Alternate food";
    private static final String PREDATOR_STR = "Predators";
    private static final String PREY_STR = "Prey animals";
    private static final String UPDATE_ACTION_COMMAND = "Update";
    private static final String RESET_DEFAULTS_ACTION_COMMAND = "Defaults";
    private static final double PREDATOR_REPRODUCTION_RATE = 0.0025;
    private static final double PREY_REPRODUCTION_RATE = 0.004;
    private static final double PREDATOR_MUTATION_RATE = 0.15;
    private static final double PREY_MUTATION_RATE = 0.15;
    private static final Dimension inputDimension = new Dimension(45,20);

    private Timer animationTimer;
    private TimerTask animationTask;
    private TimerTask updateTask;
    private ArrayList<Prey> preyList;
    private ArrayList<BasicFood> basicFoodList;
    private ArrayList<AlternateFood> alternateFoodList;
    private ArrayList<Predator> predatorList;
    private Random ran;

    private JPanel controlPanel;
    private JPanel countPanel;
    private JLabel basicFoodCount;
    private JLabel altFoodCount;
    private JLabel predatorCount;
    private JLabel preyCount;

    private JPanel inputPanel;
    private JPanel basicFoodSpawnPanel;
    private JPanel altFoodSpawnPanel;
    private JPanel predatorReprRatePanel;
    private JPanel preyReprRatePanel;
    private JPanel predatorMutationRatePanel;
    private JPanel preyMutationRatePanel;
    private JTextField basicFoodSpawnInput;
    private JTextField altFoodSpawnInput;
    private JTextField predatorReprRateInput;
    private JTextField preyReprRateInput;
    private JTextField predatorMutationRateInput;
    private JTextField preyMutationRateInput;
    private JLabel basicFoodSpawnInputLabel;
    private JLabel altFoodSpawnInputLabel;
    private JLabel predatorReprRateInputLabel;
    private JLabel preyReprRateInputLabel;
    private JLabel predatorMutationRateInputLabel;
    private JLabel preyMutationRateInputLabel;

    private JButton updateButton;
    private JButton defaultButton;

    private double currentBasicFoodSpawnRate;
    private double currentAltFoodSpawnRate;
    private double currentPredatorReproductionRate;
    private double currentPreyReproductionRate;
    private double currentPredatorMutationRate;
    private double currentPreyMutationRate;


    public Main() {
        ran = new Random();
        preyList = new ArrayList<>();
        basicFoodList = new ArrayList<>();
        alternateFoodList = new ArrayList<>();
        predatorList = new ArrayList<>();

        currentBasicFoodSpawnRate = BASIC_FOOD_SPAWN_RATE;
        currentAltFoodSpawnRate = ALTERNATE_FOOD_SPAWN_RATE;
        currentPredatorReproductionRate = PREDATOR_REPRODUCTION_RATE;
        currentPreyReproductionRate = PREY_REPRODUCTION_RATE;
        currentPredatorMutationRate = PREDATOR_MUTATION_RATE;
        currentPreyMutationRate = PREY_MUTATION_RATE;

        initializeFood();
        initializePrey();
        initializePredators();
        initializeControlPanel();

        animationTimer = new Timer();
        animationTask = new AnimationUpdater();
        updateTask = new InfoUpdater();
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

    private void initializeControlPanel(){
        this.setLayout(new BorderLayout());
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        countPanel = new JPanel();
        countPanel.setAlignmentX(LEFT_ALIGNMENT);
        countPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        countPanel.setMaximumSize(new Dimension(150, 90));

        basicFoodCount = new JLabel(BASIC_FOOD_STR + ": " + Integer.toString(basicFoodList.size()));
        altFoodCount = new JLabel(ALT_FOOD_STR + ": " + Integer.toString(alternateFoodList.size()));
        predatorCount = new JLabel(PREDATOR_STR + ": " + Integer.toString(predatorList.size()));
        preyCount = new JLabel(PREY_STR + ": " + Integer.toString(preyList.size()));

        countPanel.add(basicFoodCount);
        countPanel.add(altFoodCount);
        countPanel.add(predatorCount);
        countPanel.add(preyCount);

        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        inputPanel.setAlignmentX(LEFT_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(250, 300));

        basicFoodSpawnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        basicFoodSpawnInput = new JTextField(Double.toString(BASIC_FOOD_SPAWN_RATE));
        basicFoodSpawnInput.setPreferredSize(inputDimension);
        basicFoodSpawnInputLabel = new JLabel("Basic Food Spawn Rate: ");
        basicFoodSpawnPanel.add(basicFoodSpawnInputLabel);
        basicFoodSpawnPanel.add(basicFoodSpawnInput);

        altFoodSpawnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        altFoodSpawnInput = new JTextField(Double.toString(ALTERNATE_FOOD_SPAWN_RATE));
        altFoodSpawnInput.setPreferredSize(inputDimension);
        altFoodSpawnInputLabel = new JLabel("Alternate Food Spawn Rate: ");
        altFoodSpawnPanel.add(altFoodSpawnInputLabel);
        altFoodSpawnPanel.add(altFoodSpawnInput);

        predatorReprRatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        predatorReprRateInput = new JTextField(Double.toString(PREDATOR_REPRODUCTION_RATE));
        predatorReprRateInput.setPreferredSize(inputDimension);
        predatorReprRateInputLabel = new JLabel("Predator Reproduction Rate: ");
        predatorReprRatePanel.add(predatorReprRateInputLabel);
        predatorReprRatePanel.add(predatorReprRateInput);

        preyReprRatePanel = new JPanel(new FlowLayout());
        preyReprRateInput = new JTextField(Double.toString(PREY_REPRODUCTION_RATE));
        preyReprRateInput.setPreferredSize(inputDimension);
        preyReprRateInputLabel = new JLabel("Prey Reproduction Rate: ");
        preyReprRatePanel.add(preyReprRateInputLabel);
        preyReprRatePanel.add(preyReprRateInput);

        predatorMutationRatePanel = new JPanel(new FlowLayout());
        predatorMutationRateInput = new JTextField(Double.toString(PREDATOR_MUTATION_RATE));
        predatorMutationRateInput.setPreferredSize(inputDimension);
        predatorMutationRateInputLabel = new JLabel("Predator Mutation Rate: ");
        predatorMutationRatePanel.add(predatorMutationRateInputLabel);
        predatorMutationRatePanel.add(predatorMutationRateInput);

        preyMutationRatePanel = new JPanel(new FlowLayout());
        preyMutationRateInput = new JTextField(Double.toString(PREY_MUTATION_RATE));
        preyMutationRateInput.setPreferredSize(inputDimension);
        preyMutationRateInputLabel = new JLabel("Prey Mutation Rate: ");
        preyMutationRatePanel.add(preyMutationRateInputLabel);
        preyMutationRatePanel.add(preyMutationRateInput);

        ButtonClickListener listener = new ButtonClickListener();
        updateButton = new JButton("Update parameters");
        updateButton.setActionCommand(UPDATE_ACTION_COMMAND);
        updateButton.addActionListener(listener);
        defaultButton = new JButton("Reset to defaults");
        defaultButton.setActionCommand(RESET_DEFAULTS_ACTION_COMMAND);
        defaultButton.addActionListener(listener);

        inputPanel.add(basicFoodSpawnPanel);
        inputPanel.add(altFoodSpawnPanel);
        inputPanel.add(predatorReprRatePanel);
        inputPanel.add(preyReprRatePanel);
        inputPanel.add(predatorMutationRatePanel);
        inputPanel.add(preyMutationRatePanel);
        inputPanel.add(updateButton);
        inputPanel.add(defaultButton);

        controlPanel.add(countPanel);
        controlPanel.add(inputPanel);

        this.add(controlPanel, BorderLayout.LINE_END);
    }

    private void start() {
        animationTimer.schedule(animationTask, 0, MS_TO_WAIT);
        animationTimer.schedule(updateTask, 0, MS_TO_WAIT);
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
        frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

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
                prey.update(WORLD_WIDTH, WORLD_HEIGHT);

                prey.eatBasic(basicFoodList);
                prey.eatAlternate(alternateFoodList);
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
                predator.update(WORLD_WIDTH, WORLD_HEIGHT);

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
            if (randomDouble < currentBasicFoodSpawnRate) {
                basicFoodList.add(new BasicFood(WORLD_WIDTH, WORLD_HEIGHT, BASIC_FOOD_RADIUS));
            }
            if (randomDouble < currentAltFoodSpawnRate) {
                alternateFoodList.add(new AlternateFood(WORLD_WIDTH, WORLD_HEIGHT, ALTERNATE_FOOD_RADIUS));
            }

            repaint();
        }
    }

    private class InfoUpdater extends TimerTask {
        @Override
        public void run(){
            basicFoodCount.setText(BASIC_FOOD_STR + ": " + Integer.toString(basicFoodList.size()));
            altFoodCount.setText(ALT_FOOD_STR + ": " + Integer.toString(alternateFoodList.size()));
            predatorCount.setText(PREDATOR_STR + ": " + Integer.toString(predatorList.size()));
            preyCount.setText(PREY_STR + ": " + Integer.toString(preyList.size()));
        }
    }


    private class ButtonClickListener implements ActionListener{
        public Boolean validInput(){
            double basicSpawn;
            double altSpawn;

            try{
                basicSpawn = Double.parseDouble(basicFoodSpawnInput.getText());
                altSpawn = Double.parseDouble(altFoodSpawnInput.getText());
            } catch(Exception e){
                return false;
            }

            if(1 < basicSpawn || basicSpawn < 0){
                return false;
            }
            if(1 < altSpawn || altSpawn < 0){
                return false;
            }
            System.out.println("Valid input.");
            return true;
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(UPDATE_ACTION_COMMAND)){
                if(validInput()){
                    currentBasicFoodSpawnRate = Double.parseDouble(basicFoodSpawnInput.getText());
                    currentAltFoodSpawnRate = Double.parseDouble(altFoodSpawnInput.getText());
                    System.out.println("Changed parameters.");
                }
            } else if(e.getActionCommand().equals(RESET_DEFAULTS_ACTION_COMMAND)){
                currentBasicFoodSpawnRate = BASIC_FOOD_SPAWN_RATE;
                currentAltFoodSpawnRate = ALTERNATE_FOOD_SPAWN_RATE;
                basicFoodSpawnInput.setText(Double.toString(currentBasicFoodSpawnRate));
                altFoodSpawnInput.setText(Double.toString(currentAltFoodSpawnRate));
                System.out.println("Reset parameters");
            }
        }
    }

}
