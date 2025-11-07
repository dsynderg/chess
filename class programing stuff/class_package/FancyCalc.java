package class_package;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class FancyCalc {
    static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.printf("Type your numbers%n>>> ");

            String line = scanner.nextLine();
            var numbers = line.split(" ");

            int result = 0;
            for (var number : numbers) {
                result += Integer.parseInt(number);
            }
            var equation = String.join(" + ", numbers);
            System.out.printf("%s = %d%n", equation, result);
        }
    }

}
