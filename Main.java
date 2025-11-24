import functions.*;
import functions.basic.*;
import threads.*;
import java.io.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    public static void nonThread() {
        Random random = new Random();
        Task task = new Task();
        task.setTasks(100);

        for (int i = 0; i < task.getTasks(); i++) {
            task.setFunction(new Log(1 + random.nextDouble() * 9));
            task.setLeft(random.nextDouble() * 100);
            task.setRight(100 + random.nextDouble() * 100);
            task.setStep(random.nextDouble());
            System.out.println("Source " + task.getLeft() + " " + task.getRight() + " " + task.getStep());
            double result = Functions.integrate(task.getFunction(), task.getLeft(), task.getRight(), task.getStep());
            System.out.println("Result " + task.getLeft() + " " + task.getRight() + " " + task.getStep() + " " + result);
        }
    }
    public static void simpleThreads() {
        Task task = new Task();
        task.setTasks(100);

        Thread generThread = new Thread(new SimpleGenerator(task));
        Thread integratThread = new Thread(new SimpleIntegrator(task));

        generThread.start();
        try {
            Thread.sleep(50); // ждем 50 мс
        }
        catch (InterruptedException e) {
        }
        integratThread.start();

        try {
            generThread.join();
            integratThread.join();
        } catch (InterruptedException e) {
        }
    }
    public static void complicatedThreads() {
        Task task = new Task();
        task.setTasks(100);

        Semaphore generatorSemaphore = new Semaphore(1);  // Generator начинает первым
        Semaphore integratorSemaphore = new Semaphore(0); // Integrator ждет разрешения

        Generator generator = new Generator(task, generatorSemaphore, integratorSemaphore);
        Integrator integrator = new Integrator(task, generatorSemaphore, integratorSemaphore);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50); // Ждем 50 мс и прерываем
        }
        catch (InterruptedException e) {
        }

        generator.interrupt();
        integrator.interrupt();

        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
        }

        System.out.println("закончено");
    }

    public static void main(String[] args) throws IOException {
        Exp exp = new Exp();
        double theoret = Math.E - 1;

        System.out.println("интерграл экспоненты");
        System.out.println("Теоретическое значение " + theoret);

        double resul = Functions.integrate(exp, 0, 1, 0.0001);

        System.out.println("Результат с вычислениями через метод трапеции с шагом 0.0001 " + resul);
        System.out.println("Разница " + Math.abs(resul - theoret));

        if (Math.abs(resul - theoret) < 0.0000001) {
            System.out.println("Точность 1e-7");
        }

        nonThread();
        simpleThreads();
        complicatedThreads();
    }
}