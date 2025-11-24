package functions;

import functions.meta.*;

public class Functions {

    // Приватный конструктор, чтобы нельзя было создать объект класса
    private Functions() {
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double left, double right, double step) {
        // Проверка границ
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы вне области определения");
        }
        if (left >= right) throw new IllegalArgumentException("Левая граница должна быть < правой");
        if (step <= 0) throw new IllegalArgumentException("Шаг должен быть > 0");

        double sum = 0;
        double x = left;

        // Суммируем площади трапеций
        while (x < right) {
            double x2 = Math.min(x + step, right);
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(x2);
            sum += (y1 + y2) * (x2 - x) / 2; // Площадь трапеции
            x = x2;
        }
        return sum;
    }
}