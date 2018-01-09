package com.company.ecosystem;

import java.util.Arrays;
import java.util.Random;

public class DNA {
    Double[] genes;
    Random ran;

    DNA() {
        ran = new Random();
        genes = new Double[3];
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

    void mutate(Random r) {
        int geneIndex = r.nextInt(genes.length);
        genes[geneIndex] = r.nextDouble();
    }

    public Double[] getGenes() {
        return genes;
    }

    public void setGenes(Double[] genes) {
        this.genes = genes;
    }

    public Random getRan() {
        return ran;
    }

    public void setRan(Random ran) {
        this.ran = ran;
    }
}
