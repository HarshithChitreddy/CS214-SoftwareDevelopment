import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CS_214_Project {
            
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error: Provide exactly one input file.");
            return;
        }
            
        String inputFileName = args[0];
        List<Image> imageList = new ArrayList<>();
        List<String> imageFileNames = new ArrayList<>();
            
        try {
            File inputFile = new File(inputFileName);
            Scanner inputScanner = new Scanner(inputFile);
            
            while (inputScanner.hasNextLine()) {
                String imagePath = inputScanner.nextLine();
                String imageFileName = new File(imagePath).getName();
            
                try {
                    Image image = readPGMFile(imagePath);
                    imageList.add(image);
                    imageFileNames.add(imageFileName);
                } catch (Exception e) {
                    System.err.println("Error processing file: " + imagePath + " - " + e.getMessage());
                    return;
                }
            }
                     
            if (imageList.size() < 2) {
                System.err.println("Error: Need at least two valid images.");
                return;
            }
            
            for (int i = 0; i < imageList.size(); i++) {
                Image currentImage = imageList.get(i);
                double bestMatchError = Double.NEGATIVE_INFINITY;
                String mostSimilarImageFileName = "";
            
                for (int j = 0; j < imageList.size(); j++) {
                    if (i != j) {
                        double error = compareHistograms(currentImage, imageList.get(j));
                        if (error > bestMatchError) {
                            bestMatchError = error;
                            mostSimilarImageFileName = imageFileNames.get(j);
                        }
                    }
                }
            
                System.out.printf("%s %s %.6f%n", imageFileNames.get(i), mostSimilarImageFileName, bestMatchError);
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found - " + inputFileName);
        }
    }
               
    public static Image readPGMFile(String filePath) throws Exception {
        Scanner scanner = new Scanner(new File(filePath));
        String format = scanner.next();
            
        if (!format.equals("P3")) {
            throw new IllegalArgumentException("Invalid file format.");
        }
            
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        int maxPixelValue = scanner.nextInt();
           
        if (maxPixelValue > 255 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid image dimensions or pixel value.");
        }                
        
        List<Integer> pixelData = new ArrayList<>();
        while (scanner.hasNextInt()) {
            int pixelValue = scanner.nextInt();
            if (pixelValue >= 0 && pixelValue <= 255) {
                pixelData.add(pixelValue);
            } else {
                throw new IllegalArgumentException("Invalid pixel value.");
            }
        }
            
        if (pixelData.size() != width * height) {
            throw new IllegalArgumentException("Pixel count mismatch.");
        }
            
        return new Image(width, height, pixelData);
    }
                
    public static double compareHistograms(Image image1, Image image2) {
        int[] histogram1 = new int[64];
        int[] histogram2 = new int[64];
            
        for (int pixel : image1.pixelData) {
            int binIndex = pixel / 4;
            histogram1[binIndex]++;
        }
               
        for (int pixel : image2.pixelData) {
            int binIndex = pixel / 4;
            histogram2[binIndex]++;
        }
            
        double histogramIntersectionSum = 0;
        for (int i = 0; i < histogram1.length; i++) {
            histogramIntersectionSum += Math.min(histogram1[i], histogram2[i]);
        }
            
        int totalPixels = image1.width * image1.height;
        return histogramIntersectionSum / totalPixels;
    }
            
    public static class Image {
        int width, height;
        List<Integer> pixelData;
            
        public Image(int width, int height, List<Integer> pixelData) {
            this.width = width;
            this.height = height;
            this.pixelData = pixelData;
        }
    }
}