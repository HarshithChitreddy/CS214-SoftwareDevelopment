import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CS_214_Project {
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("Error: Two file names should be entered as arguements");
            return;
        }

        String fileN1 = args[0];
        String fileN2 = args[1];

        int[] cts1 = new int[64];
        int[] cts2 = new int[64];

        boolean hasErr = false;

        hasErr = readFile(fileN1, cts1);
        if (hasErr) return;

        hasErr = readFile(fileN2, cts2);
        if(hasErr) return;

        double[] normalized1 = normalize(cts1);
        double[] normalized2 = normalize(cts2);

        double res = compareHistograms(normalized1, normalized2);
        System.out.printf("%.6f", res);   
    }

    public static boolean readFile(String fileN, int[] cts){
        boolean hasErr = false;
        try (Scanner scanner = new Scanner(new File(fileN))){
            while (scanner.hasNext()){
                String temp = scanner.next();
                try{
                    Integer val = Integer.parseInt(temp);
                    if (val < 0 || val > 255){
                        System.err.println("Error: Out of range input value (0-255 in file" + fileN);
                        return true;
                    }
                    cts[(int) Math.floor(val / 4)] += 1;
                }catch (NumberFormatException e) {
                    System.err.println("Error: Incorrect input. A non-integer value was found in file" + fileN);
                    return true;
                }
            } 
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found" + fileN);
            return true;
        }
        return hasErr;
    }  

    public static double[] normalize(int[] cts){
        double [] normalized = new double[64];
        int totalCt = 0;
        for (int ct : cts){
            totalCt += ct;
        }
        for (int i = 0; i < cts.length; i++){
            normalized[i] = (double) cts[i] / totalCt;
        }
        return normalized;
    }

    public static double compareHistograms(double[] his1, double[] his2){
        double sum = 0;
        for (int i = 0; i < his1.length; i++){
            sum += his1[i] * his2[i];
        }
        return sum;
    }
}