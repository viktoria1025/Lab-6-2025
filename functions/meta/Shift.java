package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function f;
    private double shX;
    private double shY;

    public Shift(Function f, double shX, double shY) {
        this.f = f;
        this.shX = shX;
        this.shY = shY;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + shX;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + shX;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f.getFunctionValue(x - shX) + shY;
    }
}