package org.jptorres.api_challengeTenpo.dto;

/**
 * Objeto de transferencia de datos que encapsula la respuesta del cálculo.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
public class CalculationResponse {
    private double sum;
    private double percentage;
    private double result;

    /**
     * Constructor que inicializa los atributos con los valores del cálculo.
     *
     * @param sum        Suma de num1 y num2.
     * @param percentage Porcentaje aplicado.
     * @param result     Resultado final del cálculo.
     */
    public CalculationResponse(double sum, double percentage, double result) {
        this.sum = sum;
        this.percentage = percentage;
        this.result = result;
    }

    public double getSum() { return sum; }
    public double getPercentage() { return percentage; }
    public double getResult() { return result; }

    @Override
    public String toString() {
        return "CalculationResponse{" +
                "sum=" + sum +
                ", percentage=" + percentage +
                ", result=" + result +
                '}';
    }
}
