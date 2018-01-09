package com.company.ecosystem;

public class BasicFood extends Food {

    BasicFood(int worldWidth, int worldHeight, int foodRadius) {
        super();

        Double[] loc = new Double[2];
        /*
        if (ran.nextDouble() > 0.5) {
            loc[0] = 2 * worldWidth / 5 + (ran.nextDouble() * worldWidth) / 5;
            loc[1] = 2 * worldHeight / 5 + (ran.nextDouble() * worldHeight) / 5;
        }
        else {
            loc[0] = this.getRan().nextDouble() * worldWidth;
            loc[1] = this.getRan().nextDouble() * worldHeight;
        }*/

        loc[0] = this.getRan().nextDouble() * worldWidth;
        loc[1] = this.getRan().nextDouble() * worldHeight;
        location = loc;

        radius = foodRadius * 1.0;
    }
}
