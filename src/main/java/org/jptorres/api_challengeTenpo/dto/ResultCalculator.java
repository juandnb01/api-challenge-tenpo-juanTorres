package org.jptorres.api_challengeTenpo.dto;

/**
 * DTO que encapsula únicamente el resultado final del cálculo.
 * Puede utilizarse en contextos donde solo se requiera el valor resultante.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
public class ResultCalculator {

    private double result;

    // Getter
    public double getResult() {
        return result;
    }

    // Setter
    public void setResult(double result) {
        this.result = result;
    }
}
