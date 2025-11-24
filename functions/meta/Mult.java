package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function f1;
    private Function f2;

    public Mult(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        double left1 = f1.getLeftDomainBorder();
        double left2 = f2.getLeftDomainBorder();

        if (left1 > left2) {
            return left1;
        } else {
            return left2;
        }
    }

    public double getRightDomainBorder() {
        double right1 = f1.getRightDomainBorder();
        double right2 = f2.getRightDomainBorder();

        if (right1 < right2) {
            return right1;
        } else {
            return right2;
        }
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}