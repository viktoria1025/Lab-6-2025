package threads;

import functions.Function;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore generatorSemaphore;
    private final Semaphore integratorSemaphore;

    public Integrator(Task task, Semaphore generatorSemaphore, Semaphore integratorSemaphore) {
        this.task = task;
        this.generatorSemaphore = generatorSemaphore;
        this.integratorSemaphore = integratorSemaphore;
    }

    public void run() {
        try {
            for (int i = 0; i < task.getTasks(); i++) {
                if (isInterrupted()) return;

                // Ждем разрешения на чтение
                integratorSemaphore.acquire();

                Function function = task.getFunction();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();

                if (function != null) {
                    double result = functions.Functions.integrate(function, left, right, step);
                    System.out.println("Result " + left + " " + right + " " + step + " " + result);
                }

                // Разрешаем следующую генерацию
                generatorSemaphore.release();
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}