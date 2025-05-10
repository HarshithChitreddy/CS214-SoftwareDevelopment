import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class CS_214_Project {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Provide exactly two arguments - input file and integer K.");
            return;
        }

        String fileArg = args[0];
        int numClusters;

        try {
            numClusters = Integer.parseInt(args[1]);
            if (numClusters <= 0) {
                System.err.println("Error: K must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: K must be an integer.");
            return;
        }

        List<Picture> pictures = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        try {
            File inputFile = new File(fileArg);
            Scanner fileScanner = new Scanner(inputFile);

            while (fileScanner.hasNextLine()) {
                String imagePath = fileScanner.nextLine().trim();
                if (imagePath.isEmpty())
                    continue;
                String imageName = new File(imagePath).getName();

                try {
                    Picture picture = loadImage(imagePath);
                    pictures.add(picture);
                    fileNames.add(imageName);
                } catch (Exception e) {
                    System.err.println("Error processing file: " + imagePath + " - " + e.getMessage());
                    return;
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found - " + fileArg);
            return;
        }

        int numImages = pictures.size();

        if (numClusters > numImages) {
            System.err.println("Error: K cannot be greater than the number of images (" + numImages + ").");
            return;
        }

        if (numImages < 2) {
            System.err.println("Error: Need at least two valid images.");
            return;
        }

        List<Cluster> clustersList = new ArrayList<>();
        for (int i = 0; i < numImages; i++) {
            List<String> imageNames = new ArrayList<>();
            imageNames.add(fileNames.get(i));
            clustersList.add(new Cluster(imageNames, pictures.get(i)));
        }

        while (clustersList.size() > numClusters) {
            double highestSimilarity = -1.0;
            int indexA = -1;
            int indexB = -1;

            for (int i = 0; i < clustersList.size(); i++) {
                for (int j = i + 1; j < clustersList.size(); j++) {
                    double similarity = compareHistograms(clustersList.get(i).histogramData,
                            clustersList.get(j).histogramData);
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                        indexA = i;
                        indexB = j;
                    }
                }
            }

            if (indexA != -1 && indexB != -1) {
                Cluster mergedCluster = Cluster.merge(clustersList.get(indexA), clustersList.get(indexB));
                clustersList.remove(indexB);
                clustersList.remove(indexA);
                clustersList.add(mergedCluster);
            } else {
                break;
            }
        }

        for (Cluster cluster : clustersList) {
            Collections.sort(cluster.imageFileNames);
        }

        clustersList.sort(Comparator.comparing(c -> c.imageFileNames.get(0)));

        for (Cluster cluster : clustersList) {
            System.out.println(String.join(" ", cluster.imageFileNames));
        }
    }

    public static Picture loadImage(String filePath) throws Exception {
        Scanner scanner = new Scanner(new File(filePath));
        if (!scanner.hasNext()) {
            scanner.close();
            throw new IllegalArgumentException("Empty file.");
        }

        String formatType = scanner.next();

        if (!formatType.equals("P3")) {
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

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IllegalArgumentException("Missing max pixel value.");
        }
        int maxPixel = scanner.nextInt();

        if (maxPixel > 255 || imageWidth <= 0 || imageHeight <= 0) {
            scanner.close();
            throw new IllegalArgumentException("Invalid image dimensions or pixel value.");
        }

        List<Integer> pixelValues = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int pixel = scanner.nextInt();
                if (pixel >= 0 && pixel <= 255) {
                    pixelValues.add(pixel);
                } else {
                    scanner.close();
                    throw new IllegalArgumentException("Invalid pixel value: " + pixel);
                }
            } else {
                scanner.next();
            }
        }

        scanner.close();

        if (pixelValues.size() != imageWidth * imageHeight) {
            throw new IllegalArgumentException("Pixel count mismatch. Expected: " + (imageWidth * imageHeight)
                    + ", Found: " + pixelValues.size());
        }

        return new Picture(imageWidth, imageHeight, pixelValues);
    }

    public static double compareHistograms(double[] histogram1, double[] histogram2) {
        double minimumSum = 0.0;
        for (int i = 0; i < histogram1.length; i++) {
            minimumSum += Math.min(histogram1[i], histogram2[i]);
        }
        return minimumSum;
    }

    public static class Picture {
        int width, height;
        List<Integer> pixelValues;

        public Picture(int width, int height, List<Integer> pixelValues) {
            this.width = width;
            this.height = height;
            this.pixelValues = pixelValues;
        }

        public double[] calculateHistogram() {
            int[] histogramArray = new int[64];
            for (int pixel : pixelValues) {
                int binIndex = Math.min(pixel / 4, 63);
                histogramArray[binIndex]++;
            }

            double[] normalizedHistogram = new double[64];
            int totalPixels = width * height;
            for (int i = 0; i < histogramArray.length; i++) {
                normalizedHistogram[i] = (double) histogramArray[i] / totalPixels;
            }

            return normalizedHistogram;
        }
    }

    public static class Cluster {
        List<String> imageFileNames;
        double[] histogramData;

        public Cluster(List<String> imageFileNames, Picture picture) {
            this.imageFileNames = new ArrayList<>(imageFileNames);
            this.histogramData = picture.calculateHistogram();
        }

        public static Cluster merge(Cluster cluster1, Cluster cluster2) {
            List<String> mergedFileNames = new ArrayList<>();
            mergedFileNames.addAll(cluster1.imageFileNames);
            mergedFileNames.addAll(cluster2.imageFileNames);
            Collections.sort(mergedFileNames);

            double[] mergedHistogram = new double[64];
            for (int i = 0; i < 64; i++) {
                mergedHistogram[i] = (cluster1.histogramData[i] + cluster2.histogramData[i]) / 2.0;
            }

            Cluster mergedCluster = new Cluster(mergedFileNames, mergedHistogram);
            return mergedCluster;
        }

        private Cluster(List<String> fileNames, double[] normalizedHistogram) {
            this.imageFileNames = new ArrayList<>(fileNames);
            this.histogramData = normalizedHistogram.clone();
        }
    }
}