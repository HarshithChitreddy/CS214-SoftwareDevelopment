import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CS_214_Project {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Provide exactly two input files.");
            return;
        }

        String file1 = args[0];
        String file2 = args[1];

        try {
            Image img1 = readPGMFile(file1);
            Image img2 = readPGMFile(file2);

            if (img1.width != img2.width || img1.height != img2.height) {
                System.err.println("Error: Images have different dimensions.");
                return;
            }

            double histogramComparisonValue = calculateHistogramComparison(img1, img2);
            double sumOfSquares = calculateSumOfSquares(img1, img2);

            System.out.printf("%.6f %.0f%n", histogramComparisonValue, sumOfSquares);
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static Image readPGMFile(String filename) throws Exception {
        Scanner scanner = new Scanner(new File(filename));
        String magicNumber = scanner.next();
        if (!magicNumber.equals("P3")) {
            throw new IllegalArgumentException("Invalid file format.");
        }

        int width = scanner.nextInt();
        int height = scanner.nextInt();
        int maxPixelValue = scanner.nextInt();

        if (maxPixelValue != 255 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid image dimensions or pixel value.");
        }

        List<Integer> pixels = new ArrayList<>();
        while (scanner.hasNextInt()) {
            pixels.add(scanner.nextInt());
        }

        if (pixels.size() != width * height) {
            throw new IllegalArgumentException("Pixel count mismatch.");
        }

        return new Image(width, height, pixels);
    }

    public static double calculateHistogramComparison(Image img1, Image img2) {
        int[] hist1 = new int[64];
        int[] hist2 = new int[64];

        for (int pixel : img1.pixels) {
            int binIndex = pixel / 4;
            hist1[binIndex]++;
        }
        for (int pixel : img2.pixels) {
            int binIndex = pixel / 4;
            hist2[binIndex]++;
        }

        double sumOfMin = 0;
        for (int i = 0; i < hist1.length; i++) {
            sumOfMin += Math.min(hist1[i], hist2[i]);
        }

        int totalPixels = img1.width * img1.height;
        return sumOfMin / totalPixels;
    }

    public static double calculateSumOfSquares(Image img1, Image img2) {
        double sumOfSquares = 0;
        for (int i = 0; i < img1.pixels.size(); i++) {
            int diff = img1.pixels.get(i) - img2.pixels.get(i);
            sumOfSquares += diff * diff;
        }
        return sumOfSquares;
    }

    public static class Image {
        int width, height;
        List<Integer> pixels;

        public Image(int width, int height, List<Integer> pixels) {
            this.width = width;
            this.height = height;
            this.pixels = pixels;
        }
    }
}
