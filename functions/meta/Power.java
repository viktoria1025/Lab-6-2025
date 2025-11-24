package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f;
    private double power;

    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        // Сначала проверяем область определения для x
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        double base = f.getFunctionValue(x);

        // Проверяем основание для возведения в степень
        if (Double.isNaN(base)) {
            return Double.NaN;
        }

        if (power == 2.0) {
            return base * base;  // для квадрата
        }

        if (base < 0 && power != (int) power) {
            return Double.NaN;  // корень из отрицательного числа
        }

        return Math.pow(base, power);
    }
}