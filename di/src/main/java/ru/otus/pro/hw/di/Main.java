package ru.otus.pro.hw.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@ComponentScan
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Repository<Product> repository = context.getBean(Repository.class);
        Cart cart = context.getBean(Cart.class);

        System.out.println("Products list");
        repository.getAll().forEach(System.out::println);

        System.out.println("To add products to the cart use command: /add <id> ");
        System.out.println("To delete products from the cart use command: /del <id> ");
        System.out.println("For exit press Q ");
        var line = "";
        do {
            System.out.println("Type command:");
            line = sc.nextLine();
            if (line == null || line.isBlank()) {
                continue;
            }
            if (exit(line)) {
                break;
            }

            var arg = line.trim().split(" ");
            if (arg.length != 2) {
                System.out.println("Wrong command");
                continue;
            }
            try {
                var id = Integer.parseInt(arg[1]);
                if (arg[0].equalsIgnoreCase("/add")) {

                    cart.add(id);
                    System.out.println("Items in cart: ");
                    cart.view().forEach(System.out::println);
                    continue;
                } else if (arg[0].equalsIgnoreCase("/del")) {
                    cart.delete(id);
                    System.out.println("Items in cart: ");
                    cart.view().forEach(System.out::println);
                    continue;
                }
                System.out.println("Wrong command");
            } catch (RuntimeException e) {
                System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            }
        } while (true);
    }

    private static boolean exit(String line) {
        if (line.trim().toLowerCase().equals("q")) {
            return true;
        }
        return false;
    }
}
