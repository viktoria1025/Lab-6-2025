package threads;

import functions.Function;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasks(); i++) {
            double left, right, step, result;
            Function function;

            synchronized (task) {
                // Ждем пока появятся данные
                while (task.getFunction() == null) {
                    try {
                        task.wait(100);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                function = task.getFunction();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
                result = functions.Functions.integrate(function, left, right, step);

                // Очищаем данные
                task.setFunction(null);

                System.out.println("Result " + left + " " + right + " " + step + " " + result);
                task.notify();
            }
        }
    }
}