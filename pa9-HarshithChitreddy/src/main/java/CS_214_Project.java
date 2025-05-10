import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CS_214_Project {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Error: Provide exactly three arguments - training set file, test set file, and K.");
            return;
        }

        String trainingFileName = args[0];
        String testFileName = args[1];
        int K;

        try {
            K = Integer.parseInt(args[2]);
            if (K <= 0) {
                System.err.println("Error: K must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: K must be an integer.");
            return;
        }

        Map<Integer, List<Image>> trainingData = new HashMap<>();
        List<Image> testImages = new ArrayList<>();
        List<String> testFileNames = new ArrayList<>();

        
        try {
            File trainingFile = new File(trainingFileName);
            Scanner trainingScanner = new Scanner(trainingFile);
            while (trainingScanner.hasNextLine()) {
                String filePath = trainingScanner.nextLine().trim();
                if (filePath.isEmpty()) continue;
                try {
                    Image image = readPGMFile(filePath);
                    int classLabel = extractClassLabel(filePath);
                    trainingData.computeIfAbsent(classLabel, k -> new ArrayList<>()).add(image);
                } catch (Exception e) {
                    System.err.println("Error processing training file: " + filePath + " - " + e.getMessage());
                    return;
                }
            }
            trainingScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Training file not found - " + trainingFileName);
            return;
        }

        if (trainingData.size() < 2) {
            System.err.println("Error: Training data must have at least two classes.");
            return;
        }

      
        try {
            File testFile = new File(testFileName);
            Scanner testScanner = new Scanner(testFile);
            while (testScanner.hasNextLine()) {
                String filePath = testScanner.nextLine().trim();
                if (filePath.isEmpty()) continue;
                try {
                    Image image = readPGMFile(filePath);
                    testImages.add(image);
                    testFileNames.add(new File(filePath).getName());
                } catch (Exception e) {
                    System.err.println("Error processing test file: " + filePath + " - " + e.getMessage());
                    return;
                }
            }
            testScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Test file not found - " + testFileName);
            return;
        }

        if (testImages.isEmpty()) {
            System.err.println("Error: Test data must contain at least one image.");
            return;
        }

      
        Map<Integer, Perceptron> perceptrons = new HashMap<>();
        for (Map.Entry<Integer, List<Image>> entry : trainingData.entrySet()) {
            int classLabel = entry.getKey();
            List<Image> classImages = entry.getValue();
            Perceptron perceptron = new Perceptron();
            for (int epoch = 0; epoch < 100; epoch++) {
                for (Image image : classImages) {
                    perceptron.train(image.getNormalizedHistogram(), 1);
                }
                for (Map.Entry<Integer, List<Image>> otherEntry : trainingData.entrySet()) {
                    if (otherEntry.getKey() != classLabel) {
                        for (Image image : otherEntry.getValue()) {
                            perceptron.train(image.getNormalizedHistogram(), -1);
                        }
                    }
                }
            }
            perceptrons.put(classLabel, perceptron);
        }

    
        double[][] similarityMatrix = new double[testImages.size()][testImages.size()];
        for (int i = 0; i < testImages.size(); i++) {
            for (int j = 0; j < testImages.size(); j++) {
                double similarity = 0.0;
                for (Perceptron perceptron : perceptrons.values()) {
                    double yi = perceptron.evaluate(testImages.get(i).getNormalizedHistogram());
                    double yj = perceptron.evaluate(testImages.get(j).getNormalizedHistogram());
                    similarity += Math.pow(yi - yj, 2);
                }
                similarityMatrix[i][j] = 1/ (1 + similarity);
            }
        }
        
        
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < testImages.size(); i++) {
            clusters.add(new Cluster(Collections.singletonList(testFileNames.get(i))));
        }

     
        while (clusters.size() > K) {
            double maxMinSimilarity = Double.NEGATIVE_INFINITY;
            int clusterA = -1, clusterB = -1;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double similarity = calculateClusterSimilarity(clusters.get(i), clusters.get(j), similarityMatrix, testFileNames);
                    if (similarity > maxMinSimilarity) {
                        maxMinSimilarity = similarity;
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
                clusters.sort(Comparator.comparing(cluster -> cluster.fileNames.get(0))); // Sort after merging
            }
        }

     
        clusters.sort(Comparator.comparing(cluster -> cluster.fileNames.get(0)));
        for (Cluster cluster : clusters) {
            Collections.sort(cluster.fileNames);
            System.out.println(String.join(" ", cluster.fileNames));
        }
    }

    private static double calculateClusterSimilarity(Cluster c1, Cluster c2, double[][] similarityMatrix, List<String> testFileNames) {
        double minSimilarity = Double.NEGATIVE_INFINITY;
        for (String fileName1 : c1.fileNames) {
            int index1 = testFileNames.indexOf(fileName1);
            for (String fileName2 : c2.fileNames) {
                int index2 = testFileNames.indexOf(fileName2);
                double similarity = similarityMatrix[index1][index2];
                minSimilarity = Math.max(similarity, minSimilarity);
            }
        }
        return minSimilarity;
    }

    public static Image readPGMFile(String filePath) throws Exception {
        Scanner scanner = new Scanner(new File(filePath));
        if (!scanner.hasNext()) throw new IllegalArgumentException("Empty file.");
        String formatIdentifier = scanner.next();
        if (!formatIdentifier.equals("P2")) throw new IllegalArgumentException("Invalid file format. Expected P2.");
        int imageWidth = scanner.nextInt();
        int imageHeight = scanner.nextInt();
        if (imageWidth != 128 || imageHeight != 128)
            throw new IllegalArgumentException("Invalid image dimensions. Expected 128x128.");
        int maxPixelValue = scanner.nextInt();
        if (maxPixelValue > 255)
            throw new IllegalArgumentException("Invalid max pixel value.");
        List<Integer> pixelData = new ArrayList<>();
        while (scanner.hasNextInt()) {
            int pixel = scanner.nextInt();
            if (pixel < 0 || pixel > 255)
                throw new IllegalArgumentException("Invalid pixel value: " + pixel);
            pixelData.add(pixel);
        }
        scanner.close();
        if (pixelData.size() != imageWidth * imageHeight)
            throw new IllegalArgumentException("Pixel count mismatch.");
        return new Image(imageWidth, imageHeight, pixelData);
    }

    private static int extractClassLabel(String filePath) {
        String fileName = new File(filePath).getName();
        return Integer.parseInt(fileName.split("_")[0].substring(5));
    }

    public static class Cluster {
        List<String> fileNames;

        public Cluster(List<String> fileNames) {
            this.fileNames = new ArrayList<>(fileNames);
            Collections.sort(this.fileNames);
        }

        public static Cluster merge(Cluster c1, Cluster c2) {
            List<String> mergedFileNames = new ArrayList<>(c1.fileNames);
            mergedFileNames.addAll(c2.fileNames);
            Collections.sort(mergedFileNames);
            return new Cluster(mergedFileNames);
        }
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
            int[] histogram = new int[64];
            for (int pixelValue : pixelData) {
                int binIndex = Math.min(pixelValue / 4, 63);
                histogram[binIndex]++;
            }
            double[] normalizedHistogram = new double[64];
            for (int i = 0; i < histogram.length; i++) {
                normalizedHistogram[i] = (double) histogram[i] / (width * height);
            }
            return normalizedHistogram;
        }
    }

    public static class Perceptron {
        private double[] weights;
        private double bias;
        private static final int HISTOGRAM_SIZE = 64;

        public Perceptron() {
            weights = new double[HISTOGRAM_SIZE];
            bias = 0.0;
        }

        public void train(double[] histogram, int label) {
            double y = evaluate(histogram);
            double update = label - y;
            for (int i = 0; i < HISTOGRAM_SIZE; i++) {
                weights[i] += update * histogram[i];
            }
            bias += update;
        }

        public double evaluate(double[] histogram) {
            double sum = bias;
            for (int i = 0; i < HISTOGRAM_SIZE; i++) {
                sum += weights[i] * histogram[i];
            }
            //System.out.println(sum);
            return sum;
            //> 0 ? 1 : -1;
        }
    }
}
