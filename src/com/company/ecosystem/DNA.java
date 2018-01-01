package com.company.ecosystem;

import java.util.Arrays;
import java.util.Random;

public class DNA {
    Double[] genes;
    Random ran;

    DNA() {
        ran = new Random();
        genes = new Double[1];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = ran.nextDouble();
        }
    }

    DNA(Double[] newGenes) {
        genes = newGenes;
    }

    DNA copy() {
        Double[] newGenes = Arrays.copyOf(genes, genes.length);
        return new DNA(newGenes);
    }
}
