package functions.basic;

import functions.Function;

public class Log implements Function {
    private double basis;

    public Log(double basis) {
        if (basis <= 0 || basis == 1) {
            throw new IllegalArgumentException("Основание должно быть юольше 0 и не равно 1");
        }
        this.basis = basis;
    }
    public double getLeftDomainBorder() {
        return 0;
    }
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        if (x == 1.0) {
            return 0.0;
        }
        return Math.log(x) / Math.log(basis);
    }
    public double getBasis() {
        return basis;
    }
}
