package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        Random random = new Random();
        for (int i = 0; i < task.getTasks(); i++) {
            synchronized (task) {
                // Ждем пока предыдущие данные обработаются
                while (task.getFunction() != null) {
                    try {
                        task.wait(100);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                // Генерируем новые данные
                task.setFunction(new Log(1 + random.nextDouble() * 9));
                task.setLeft(random.nextDouble() * 100);
                task.setRight(100 + random.nextDouble() * 100);
                task.setStep(random.nextDouble());

                System.out.println("Source " + task.getLeft() + " " + task.getRight() + " " + task.getStep());
                task.notify();
            }
        }
    }
}