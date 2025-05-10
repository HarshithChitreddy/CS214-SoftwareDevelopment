import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CS_214_Project {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error: Invalid number of arguments.");
            System.exit(1);
        }

        int[] histogramCounts = new int[64];

        try (Scanner fileScanner = new Scanner(new File(args[0]))) {
            while (fileScanner.hasNext()) {
                if (fileScanner.hasNextInt()) {
                    int value = fileScanner.nextInt();
                    if (value >= 0 && value <= 255) {
                        int binIndex = value / 4;
                        histogramCounts[binIndex]++;
                    } else {
                        System.err.println("Error: Number out of range (0-255).");
                        System.exit(1);
                    }
                } else {
                    fileScanner.next(); // Consume the invalid input
                    System.err.println("Error: Invalid input detected.");
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
            System.exit(1);
        }

        for (int count : histogramCounts) {
            System.out.print(count + " ");
        }
        System.out.println();
    }
}
