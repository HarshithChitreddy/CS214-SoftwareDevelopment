import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CS_214_Project {
    public static void main(String[] args){
        if(args.length != 1){
            System.err.println("Error: An arguement should include only one file name.");
            return;
        }

        String fileName = args[0];
        int[] histog = new int[64];
        boolean hasError = false;

    try (Scanner scanner = new Scanner(new File(fileName))){
        if (!scanner.hasNext()) {
            System.err.println("Error: The input file is empty.");
            hasError = true;
        }
        
        while (scanner.hasNext()) {
            String line = scanner.next();
            try {
                Integer val = Integer.parseInt(line);
            if (val < 0 || val > 255) {
                System.err.println("Error: Input value out of range (0-255)");
                hasError = true;
                break;
            }
            histog [(int) Math.floor(val/4)]+= 1;
        }catch (Exception e){
            System.err.println("Error: Invalid input. The value was not an integer.");
            hasError = true;
        } 
    }
    }catch (FileNotFoundException e) {
        System.out.println("Error: Failed to locate the file");
        hasError = true;
    }

    if(!hasError){
        for (int count : histog){
            System.out.print(count+" ");
        }
    }        
 }
}