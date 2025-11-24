package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;
public class Generator extends Thread {
    private final Task task;
    private final Semaphore generatorSemaphore;
    private final Semaphore integratorSemaphore;

    public Generator(Task task, Semaphore generatorSemaphore, Semaphore integratorSemaphore) {
        this.task = task;
        this.generatorSemaphore = generatorSemaphore;
        this.integratorSemaphore = integratorSemaphore;
    }

    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < task.getTasks(); i++) {
                if (isInterrupted()) return;

                // Ждем разрешения на запись
                generatorSemaphore.acquire();

                // Генерируем данные
                double base = 1 + random.nextDouble() * 9;
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);

                System.out.println("Source " + left + " " + right + " " + step);

                // Разрешаем интегрирование
                integratorSemaphore.release();
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}