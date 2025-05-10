import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CS_214_Project {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Error: Provide exactly three arguments - input file, integer K, and similarity measure (1, 2, or 3).");
            return;
        }

        String inputFileName = args[0];
        int K, similarityMeasure;

        try {
            K = Integer.parseInt(args[1]);
            similarityMeasure = Integer.parseInt(args[2]);
            if (K <= 0 || similarityMeasure < 1 || similarityMeasure > 3) {
                System.err.println("Error: K must be greater than zero and similarity measure must be 1, 2, or 3.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: K and similarity measure must be integers.");
            return;
        }

        List<Image> images = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        try {
            File inputFile = new File(inputFileName);
            Scanner inputScanner = new Scanner(inputFile);
            while (inputScanner.hasNextLine()) {
                String filePath = inputScanner.nextLine().trim();
                if (filePath.isEmpty()) continue;
                String fileName = new File(filePath).getName();
                try {
                    Image image = readPGMFile(filePath);
                    images.add(image);
                    fileNames.add(fileName);
                } catch (Exception e) {
                    System.err.println("Error processing file: " + filePath + " - " + e.getMessage());
                    return;
                }
            }
            inputScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found - " + inputFileName);
            return;
        }

        int N = images.size();
        if (K > N) {
            System.err.println("Error: K cannot be greater than the number of images (" + N + ").");
            return;
        }
        if (N < 2) {
            System.err.println("Error: Need at least two valid images.");
            return;
        }

        SimilarityMeasure measure;
        switch (similarityMeasure) {
            case 1: measure = new NormHist(); break;
            case 2: measure = new NormHist4(); break;
            case 3: measure = new InvSquareDiff(); break;
            default: throw new IllegalArgumentException("Invalid similarity measure");
        }

        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            List<String> singleName = new ArrayList<>();
            singleName.add(fileNames.get(i));
            clusters.add(new Cluster(singleName, images.get(i)));
        }

        while (clusters.size() > K) {
            double maxSimilarity = -1.0;
            int clusterA = -1, clusterB = -1;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double similarity = measure.calculateSimilarity(clusters.get(i).image, clusters.get(j).image);
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                        clusterA = i;
                        clusterB = j;
                    }
                }
            }
            if (clusterA != -1 && clusterB != -1) {
                Cluster merged = Cluster.merge(clusters.get(clusterA), clusters.get(clusterB));
                clusters.remove(clusterB);
                clusters.remove(clusterA);
                clusters.add(merged);
            } else {
                break;
            }
        }

        for (Cluster cluster : clusters) {
            Collections.sort(cluster.fileNames);
        }
        clusters.sort(Comparator.comparing(c -> c.fileNames.get(0)));

        for (int i = 0; i < clusters.size(); i++) {
            System.out.print(String.join(" ", clusters.get(i).fileNames));
            if (i < clusters.size() - 1) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static Image readPGMFile(String filePath) throws Exception {
        Scanner scanner = new Scanner(new File(filePath));
        if (!scanner.hasNext()) {
            scanner.close();
            throw new IllegalArgumentException("Empty file.");
        }
        String formatIdentifier = scanner.next();
        if (!formatIdentifier.equals("P3")) {
            scanner.close();
            throw new IllegalArgumentException("Invalid file format. Expected P3.");
        }
        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IllegalArgumentException("Missing image width.");
        }
        int imageWidth = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IllegalArgumentException("Missing image height.");
        }
        int imageHeight = scanner.nextInt();
        if (imageWidth != 128 || imageHeight != 128) {
            scanner.close();
            throw new IllegalArgumentException("Invalid image dimensions. Expected 128x128.");
        }
        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IllegalArgumentException("Missing max pixel value.");
        }
        int maxPixelValue = scanner.nextInt();
        if (maxPixelValue > 255) {
            scanner.close();
            throw new IllegalArgumentException("Invalid max pixel value.");
        }
        List<Integer> pixelData = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int x = scanner.nextInt();
                if (x >= 0 && x <= 255) {
                    pixelData.add(x);
                } else {
                    scanner.close();
                    throw new IllegalArgumentException("Invalid pixel value: " + x);
                }
            } else {
                scanner.next();
            }
        }
        scanner.close();
        if (pixelData.size() != imageWidth * imageHeight) {
            throw new IllegalArgumentException("Pixel count mismatch. Expected: " + (imageWidth * imageHeight) + ", Found: " + pixelData.size());
        }
        return new Image(imageWidth, imageHeight, pixelData);
    }

    public static class Image {
        int width, height;
        List<Integer> pixelData;

        public Image(int width, int height, List<Integer> pixelData) {
            this.width = width;
            this.height = height;
            this.pixelData = pixelData;
        }

        public double[] getNormalizedHistogram() {
            return getNormalizedHistogramForRegion(0, 0, width, height);
        }

        public double[] getNormalizedHistogramForRegion(int startX, int startY, int endX, int endY) {
            int[] histogram = new int[64];
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    int pixelValue = pixelData.get(y * width + x);
                    int binIndex = Math.min(pixelValue / 4, 63);
                    histogram[binIndex]++;
                }
            }
            double[] normalizedHistogram = new double[64];
            int totalPixels = (endX - startX) * (endY - startY);
            for (int i = 0; i < histogram.length; i++) {
                normalizedHistogram[i] = (double) histogram[i] / totalPixels;
            }
            return normalizedHistogram;
        }

        public int getPixelValue(int x, int y) {
            return pixelData.get(y * width + x);
        }
    }

    public static class Cluster {
        List<String> fileNames;
        Image image;

        public Cluster(List<String> fileNames, Image image) {
            this.fileNames = new ArrayList<>(fileNames);
            this.image = image;
        }

        public static Cluster merge(Cluster c1, Cluster c2) {
            List<String> mergedNames = new ArrayList<>();
            mergedNames.addAll(c1.fileNames);
            mergedNames.addAll(c2.fileNames);
            Collections.sort(mergedNames);
            List<Integer> mergedPixelData = new ArrayList<>(c1.image.pixelData.size());
            for (int i = 0; i < c1.image.pixelData.size(); i++) {
                int avgPixel = (c1.image.pixelData.get(i) + c2.image.pixelData.get(i)) / 2;
                mergedPixelData.add(avgPixel);
            }
            Image mergedImage = new Image(c1.image.width, c1.image.height, mergedPixelData);
            return new Cluster(mergedNames, mergedImage);
        }
    }

    abstract static class SimilarityMeasure {
        public abstract double calculateSimilarity(Image img1, Image img2);
    }

    static class NormHist extends SimilarityMeasure {
        @Override
        public double calculateSimilarity(Image img1, Image img2) {
            double[] hist1 = img1.getNormalizedHistogram();
            double[] hist2 = img2.getNormalizedHistogram();
            return calculateHistogramComparison(hist1, hist2);
        }

        private double calculateHistogramComparison(double[] hist1, double[] hist2) {
            double minHistogramSum = 0.0;
            for (int i = 0; i < hist1.length; i++) {
                minHistogramSum += Math.min(hist1[i], hist2[i]);
            }
            return minHistogramSum;
        }
    }

    static class NormHist4 extends SimilarityMeasure {
        @Override
        public double calculateSimilarity(Image img1, Image img2) {
            double similarity = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    int startX = i * 64;
                    int startY = j * 64;
                    double[] hist1 = img1.getNormalizedHistogramForRegion(startX, startY, startX + 64, startY + 64);
                    double[] hist2 = img2.getNormalizedHistogramForRegion(startX, startY, startX + 64, startY + 64);
                    similarity += calculateHistogramComparison(hist1, hist2);
                }
            }
            return similarity / 4;
        }

        private double calculateHistogramComparison(double[] hist1, double[] hist2) {
            double minHistogramSum = 0.0;
            for (int i = 0; i < hist1.length; i++) {
                minHistogramSum += Math.min(hist1[i], hist2[i]);
            }
            return minHistogramSum;
        }
    }

    static class InvSquareDiff extends SimilarityMeasure {
    @Override
    public double calculateSimilarity(Image img1, Image img2) {
        double sumSquaredDiff = 0;
        for (int y = 0; y < img1.height; y++) {
            for (int x = 0; x < img1.width; x++) {
                int diff = img1.getPixelValue(x, y) - img2.getPixelValue(x, y);
                sumSquaredDiff += diff * diff;
            }
        }
        return 1.0 / (1 + Math.sqrt(sumSquaredDiff));
    }
}
}