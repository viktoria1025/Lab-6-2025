package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double scX;
    private double scY;

    public Scale(Function f, double scX, double scY) {
        this.f = f;
        this.scX = scX;
        this.scY = scY;
    }

    public double getLeftDomainBorder() {
        if (scX >= 0) {
            return f.getLeftDomainBorder() * scX;
        } else {
            return f.getRightDomainBorder() * scX;
        }
    }

    public double getRightDomainBorder() {
        if (scX >= 0) {
            return f.getRightDomainBorder() * scX;
        } else {
            return f.getLeftDomainBorder() * scX;
        }
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f.getFunctionValue(x / scX) * scY;
    }
}