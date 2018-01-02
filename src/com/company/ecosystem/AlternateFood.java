package com.company.ecosystem;

public class AlternateFood extends Food {

    AlternateFood(int worldWidth, int worldHeight, int foodRadius) {
        super();

        Double[] loc = new Double[2];
        loc[0] = this.getRan().nextDouble() * worldWidth;
        loc[1] = this.getRan().nextDouble() * worldHeight;
        location = loc;

        radius = foodRadius * 1.0;
    }
}
