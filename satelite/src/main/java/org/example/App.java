package org.example;

import lombok.SneakyThrows;
import org.example.auth.AuthCallbackHandler;

import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class App {

    private static volatile int count;

    private static void m1() {
        ExecutorService service = Executors.newFixedThreadPool(4);

        IntStream.range(0, 1000).forEach(i -> service.submit(() -> {
            synchronized (App.class) {
                synchronized (App.class) {
                    //
                }
                count += 1;
            }
        }));

        try {
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
            System.out.println(count);
        }
    }

    private static void m2() {

    }

    private static void m3() {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("Before t1 sleep");
                Thread.sleep(60000L);
                System.out.println("After t1 sleep");
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        });

        t1.setUncaughtExceptionHandler((t, e) -> System.out.println("That's all !!!"));
        t1.start();

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            System.out.println("YYY");
        }

        t1.interrupt();

        try {
            Thread.sleep(200000L);
        } catch (InterruptedException e) {
            System.out.println("ZZZ");
        }
    }

    @SneakyThrows({InterruptedException.class})
    private static void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (App.class) {
                App.class.wait();
                count += 1;
            }
        }
    }

    @SneakyThrows
    private static void m4() {
//        ReentrantLock lock = new ReentrantLock();
        Runnable r = App::run;
        ThreadGroup g = new ThreadGroup("-G-");
        new Thread(g, r).start();
        new Thread(g, r).start();
        new Thread(g, r).start();
        Thread t4 = new Thread(g, r);
        t4.start();

        t4.interrupt();

        Thread.sleep(1000L);

        System.out.println("That's all");


//        synchronized (App.class) {
//            App.class.notifyAll();
//        }
//        System.out.println(count);
//
//        Thread.sleep(100L);
//
//        synchronized (App.class) {
//            App.class.notifyAll();
//        }
//        System.out.println(count);
//
//        Thread.sleep(5000L);
//        System.out.println(count);
    }

    public static class CustomSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            super.checkPermission(perm);
        }
    }

    @SneakyThrows(value = {IOException.class, NoSuchAlgorithmException.class})
    private static void m5() {
        //System.out.println(Arrays.stream(Security.getProviders()).collect(Collectors.toList()));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

        try (InputStream inputStream = App.class.getResourceAsStream("/application.properties")) {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            System.out.println(new String(bytes, StandardCharsets.UTF_8));
            System.out.println(new String(Base64.getEncoder().encode(messageDigest.digest(bytes))));
        }

        throw new IOException("ZZZ");
    }

    @SneakyThrows
    public static void main(String[] args) {
        System.setProperty(
            "java.security.auth.login.config",
            "C:\\Users\\ariadna\\Desktop\\processing\\satelite\\src\\main\\resources\\jaas.conf"
        );

        LoginContext context = new LoginContext("processing-auth", new AuthCallbackHandler());
        context.login();
    }

}
