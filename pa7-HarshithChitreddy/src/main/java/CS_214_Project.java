import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CS_214_Project {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Error: Provide exactly three arguments - input file, integer K, and similarity measure (1, 2, 3, or 4).");
            return;
        }

        String inputFilePath = args[0];
        int clusterCount;
        int similarityOption;

        try {
            clusterCount = Integer.parseInt(args[1]);
            similarityOption = Integer.parseInt(args[2]);
            if (clusterCount <= 0 || similarityOption < 1 || similarityOption > 4) {
                System.err.println("Error: K must be greater than zero and similarity measure must be 1, 2, 3, or 4.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: K and similarity measure must be integers.");
            return;
        }

        List<ImageData> imageList = new ArrayList<>();
        List<String> imageNames = new ArrayList<>();

        try {
            File inputFile = new File(inputFilePath);
            Scanner fileScanner = new Scanner(inputFile);

            while (fileScanner.hasNextLine()) {
                String imagePath = fileScanner.nextLine().trim();
                if (imagePath.isEmpty())
                    continue;
                String imageName = new File(imagePath).getName();

                try {
                    ImageData image = loadPGMImage(imagePath);
                    imageList.add(image);
                    imageNames.add(imageName);
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

        int imageCount = imageList.size();

        if (clusterCount > imageCount) {
            System.err.println("Error: K cannot be greater than the number of images (" + imageCount + ").");
            return;
        }

        if (imageCount < 2) {
            System.err.println("Error: Need at least two valid images.");
            return;
        }

        SimilarityMetric metric = selectSimilarityMetric(similarityOption);

        List<ImageCluster> imageClusters = new ArrayList<>();
        for (int i = 0; i < imageCount; i++) {
            List<String> singleImageName = new ArrayList<>();
            singleImageName.add(imageNames.get(i));
            imageClusters.add(new ImageCluster(singleImageName, imageList.get(i)));
        }

        while (imageClusters.size() > clusterCount) {
            double maxSimilarity = -1.0;
            int firstCluster = -1;
            int secondCluster = -1;

            for (int i = 0; i < imageClusters.size(); i++) {
                for (int j = i + 1; j < imageClusters.size(); j++) {
                    double similarity = metric.calculateSimilarity(imageClusters.get(i).image, imageClusters.get(j).image);
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                        firstCluster = i;
                        secondCluster = j;
                    }
                }
            }

            if (firstCluster != -1 && secondCluster != -1) {
                ImageCluster mergedCluster = ImageCluster.mergeClusters(imageClusters.get(firstCluster), imageClusters.get(secondCluster));
                imageClusters.remove(secondCluster);
                imageClusters.remove(firstCluster);
                imageClusters.add(mergedCluster);
            } else {
                break;
            }
        }

        double qualityScore = computeClusteringQuality(imageClusters);
        System.out.format("%.6f%n", qualityScore);
    }

    private static double computeClusteringQuality(List<ImageCluster> clusters) {
        int totalImages = 0;
        int correctlyClusteredImages = 0;

        for (ImageCluster cluster : clusters) {
            Map<String, Integer> categoryCount = new HashMap<>();
            for (String fileName : cluster.imageNames) {
                String category = fileName.split("_")[0];
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }

            String mainCategory = Collections.max(categoryCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            correctlyClusteredImages += categoryCount.get(mainCategory);
            totalImages += cluster.imageNames.size();
        }

        return (double) correctlyClusteredImages / totalImages;
    }

    public static ImageData loadPGMImage(String imagePath) throws Exception {
        Scanner fileScanner = new Scanner(new File(imagePath));
        if (!fileScanner.hasNext()) {
            fileScanner.close();
            throw new IllegalArgumentException("Empty file.");
        }

        String fileType = fileScanner.next();

        if (!fileType.equals("P3")) {
            fileScanner.close();
            throw new IllegalArgumentException("Invalid file format. Expected P3.");
        }

        if (!fileScanner.hasNextInt()) {
            fileScanner.close();
            throw new IllegalArgumentException("Missing image width.");
        }
        int width = fileScanner.nextInt();

        if (!fileScanner.hasNextInt()) {
            fileScanner.close();
            throw new IllegalArgumentException("Missing image height.");
        }
        int height = fileScanner.nextInt();

        if (width != 128 || height != 128) {
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

        List<Integer> pixelData = new ArrayList<>();
        while (fileScanner.hasNext()) {
            if (fileScanner.hasNextInt()) {
                int pixelValue = fileScanner.nextInt();
                if (pixelValue >= 0 && pixelValue <= 255) {
                    pixelData.add(pixelValue);
                } else {
                    fileScanner.close();
                    throw new IllegalArgumentException("Invalid pixel value: " + pixelValue);
                }
            } else {
                fileScanner.next();
            }
        }

        fileScanner.close();

        if (pixelData.size() != width * height) {
            throw new IllegalArgumentException("Pixel count mismatch. Expected: " + (width * height)
                    + ", Found: " + pixelData.size());
        }

        return new ImageData(width, height, pixelData);
    }

    private static SimilarityMetric selectSimilarityMetric(int metricType) {
        switch (metricType) {
            case 1:
                return new HistogramNorm();
            case 2:
                return new HistogramNorm4();
            case 3:
                return new InverseSquareDifference();
            case 4:
                return new HistogramNorm9();
            default:
                throw new IllegalArgumentException("Invalid similarity measure type");
        }
    }

    public static class ImageData {
        int width, height;
        List<Integer> pixelValues;

        public ImageData(int width, int height, List<Integer> pixelValues) {
            this.width = width;
            this.height = height;
            this.pixelValues = pixelValues;
        }

        public double[] getNormalizedHistogram() {
            return calculateRegionHistogram(0, 0, width, height);
        }

        public double[] calculateRegionHistogram(int startX, int startY, int endX, int endY) {
            int[] histogram = new int[64];
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    int pixelValue = pixelValues.get(y * width + x);
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
            return pixelValues.get(y * width + x);
        }
    }

    public static class ImageCluster {
        List<String> imageNames;
        ImageData image;

        public ImageCluster(List<String> imageNames, ImageData image) {
            this.imageNames = new ArrayList<>(imageNames);
            this.image = image;
        }

        public static ImageCluster mergeClusters(ImageCluster cluster1, ImageCluster cluster2) {
            List<String> combinedNames = new ArrayList<>();
            combinedNames.addAll(cluster1.imageNames);
            combinedNames.addAll(cluster2.imageNames);
            Collections.sort(combinedNames);

            List<Integer> combinedPixelValues = new ArrayList<>(cluster1.image.pixelValues.size());
            for (int i = 0; i < cluster1.image.pixelValues.size(); i++) {
                int averagePixel = (cluster1.image.pixelValues.get(i) + cluster2.image.pixelValues.get(i)) / 2;
                combinedPixelValues.add(averagePixel);
            }

            ImageData mergedImage = new ImageData(cluster1.image.width, cluster1.image.height, combinedPixelValues);
            return new ImageCluster(combinedNames, mergedImage);
        }
    }

    interface SimilarityMetric {
        double calculateSimilarity(ImageData img1, ImageData img2);
    }

    static class HistogramNorm implements SimilarityMetric {
        @Override
        public double calculateSimilarity(ImageData img1, ImageData img2) {
            double[] hist1 = img1.getNormalizedHistogram();
            double[] hist2 = img2.getNormalizedHistogram();
            return computeHistogramComparison(hist1, hist2);
        }

        private double computeHistogramComparison(double[] hist1, double[] hist2) {
            double minSum = 0.0;
            for (int i = 0; i < hist1.length; i++) {
                minSum += Math.min(hist1[i], hist2[i]);
            }
            return minSum;
        }
    }

    static class HistogramNorm4 implements SimilarityMetric {
        @Override
        public double calculateSimilarity(ImageData img1, ImageData img2) {
            double similarity = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    int startX = i * 64;
                    int startY = j * 64;
                    double[] hist1 = img1.calculateRegionHistogram(startX, startY, startX + 64, startY + 64);
                    double[] hist2 = img2.calculateRegionHistogram(startX, startY, startX + 64, startY + 64);
                    similarity += computeHistogramComparison(hist1, hist2);
                }
            }
            return similarity / 4;
        }

        private double computeHistogramComparison(double[] hist1, double[] hist2) {
            double minSum = 0.0;
            for (int i = 0; i < hist1.length; i++) {
                minSum += Math.min(hist1[i], hist2[i]);
            }
            return minSum;
        }
    }

    static class InverseSquareDifference implements SimilarityMetric {
        @Override
        public double calculateSimilarity(ImageData img1, ImageData img2) {
            double squaredDiffSum = 0;
            for (int y = 0; y < img1.height; y++) {
                for (int x = 0; x < img1.width; x++) {
                    int diff = img1.getPixelValue(x, y) - img2.getPixelValue(x, y);
                    squaredDiffSum += diff * diff;
                }
            }
            return 1.0 / (squaredDiffSum + 1);
        }
    }

    static class HistogramNorm9 implements SimilarityMetric {
        @Override
        public double calculateSimilarity(ImageData img1, ImageData img2) {
            double similarity = 0;
            int[] xRanges = {0, 43, 86, 128};
            int[] yRanges = {0, 43, 86, 128};

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    double[] hist1 = img1.calculateRegionHistogram(xRanges[i], yRanges[j], xRanges[i+1], yRanges[j+1]);
                    double[] hist2 = img2.calculateRegionHistogram(xRanges[i], yRanges[j], xRanges[i+1], yRanges[j+1]);
                    similarity += computeHistogramComparison(hist1, hist2);
                }
            }
            return similarity / 9;
        }

        private double computeHistogramComparison(double[] hist1, double[] hist2) {
            double minSum = 0.0;
            for (int i = 0; i < hist1.length; i++) {
                minSum += Math.min(hist1[i], hist2[i]);
            }
            return minSum;
        }
    }
}