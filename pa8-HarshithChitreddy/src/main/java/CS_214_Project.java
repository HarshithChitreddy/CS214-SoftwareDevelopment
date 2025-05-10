import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CS_214_Project {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Provide exactly two arguments - input file and class number.");
            return;
        }

        String inputFilePath = args[0];
        int targetClassNumber;

        try {
            targetClassNumber = Integer.parseInt(args[1]);
            if (targetClassNumber <= 0) {
                System.err.println("Error: Class number must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Class number must be an integer.");
            return;
        }

        List<double[]> imageHistograms = new ArrayList<>();
        List<Integer> imageLabels = new ArrayList<>();

        try {
            File inputFile = new File(inputFilePath);
            Scanner fileScanner = new Scanner(inputFile);
            while (fileScanner.hasNextLine()) {
                String imagePath = fileScanner.nextLine().trim();
                if (imagePath.isEmpty()) continue;

                String imageName = new File(imagePath).getName();
                try {
                    Image image = readPGMFile(imagePath);
                    double[] normalizedHistogram = image.getNormalizedHistogram();
                    imageHistograms.add(normalizedHistogram);

                    int extractedClassNumber = Integer.parseInt(imageName.split("_")[0].substring(5));
                    imageLabels.add(extractedClassNumber == targetClassNumber ? 1 : -1);
                } catch (Exception e) {
                    System.err.println("Error processing file: " + imagePath + " - " + e.getMessage());
                    return;
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found - " + inputFilePath);
            return;
        }

        if (imageHistograms.isEmpty()) {
            System.err.println("Error: Need at least two valid images.");
            return;
        }

        Perceptron perceptronModel = new Perceptron();

        // Training loop
        for (int epoch = 0; epoch < 100; epoch++) {
            for (int i = 0; i < imageHistograms.size(); i++) {
                perceptronModel.train(imageHistograms.get(i), imageLabels.get(i));
            }
        }

        double[] finalWeights = perceptronModel.getWeights();
        for (double weight : finalWeights) {
            System.out.printf("%.6f ", weight);
        }
        System.out.printf("%.6f%n", perceptronModel.getBias());
    }

    public static Image readPGMFile(String imagePath) throws Exception {
        Scanner fileScanner = new Scanner(new File(imagePath));
        if (!fileScanner.hasNext()) {
            fileScanner.close();
            throw new IllegalArgumentException("Empty file.");
        }
        String fileFormat = fileScanner.next();
        if (!fileFormat.equals("P2")) {
            fileScanner.close();
            throw new IllegalArgumentException("Invalid file format. Expected P2.");
        }
        if (!fileScanner.hasNextInt()) {
            fileScanner.close();
            throw new IllegalArgumentException("Missing image width.");
        }
        int imageWidth = fileScanner.nextInt();
        if (!fileScanner.hasNextInt()) {
            fileScanner.close();
            throw new IllegalArgumentException("Missing image height.");
        }
        int imageHeight = fileScanner.nextInt();
        if (imageWidth != 128 || imageHeight != 128) {
            fileScanner.close();
            throw new IllegalArgumentException("Invalid image dimensions. Expected 128x128.");
        }
        if (!fileScanner.hasNextInt()) {
            fileScanner.close();
            throw new IllegalArgumentException("Missing max pixel value.");
        }
        int maxPixelValue = fileScanner.nextInt();
        if (maxPixelValue > 255) {
            fileScanner.close();
            throw new IllegalArgumentException("Invalid max pixel value.");
        }
        List<Integer> pixelValues = new ArrayList<>();
        while (fileScanner.hasNext()) {
            if (fileScanner.hasNextInt()) {
                int pixelValue = fileScanner.nextInt();
                if (pixelValue >= 0 && pixelValue <= 255) {
                    pixelValues.add(pixelValue);
                } else {
                    fileScanner.close();
                    throw new IllegalArgumentException("Invalid pixel value: " + pixelValue);
                }
            } else {
                fileScanner.next();
            }
        }
        fileScanner.close();
        if (pixelValues.size() != imageWidth * imageHeight) {
            throw new IllegalArgumentException("Pixel count mismatch. Expected: " + (imageWidth * imageHeight) + ", Found: " + pixelValues.size());
        }
        return new Image(imageWidth, imageHeight, pixelValues);
    }

    public static class Image {
        int width, height;
        List<Integer> pixelValues;

        public Image(int width, int height, List<Integer> pixelValues) {
            this.width = width;
            this.height = height;
            this.pixelValues = pixelValues;
        }

        public double[] getNormalizedHistogram() {
            int[] histogramBins = new int[64];
            for (int pixelValue : pixelValues) {
                int binIndex = Math.min(pixelValue / 4, 63);
                histogramBins[binIndex]++;
            }
            double[] normalizedHistogram = new double[64];
            int totalPixels = width * height;
            for (int i = 0; i < histogramBins.length; i++) {
                normalizedHistogram[i] = (double) histogramBins[i] / totalPixels;
            }
            return normalizedHistogram;
        }
    }
}
