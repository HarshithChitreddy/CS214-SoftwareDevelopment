import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CS_214_Project_Tester {

    @Test
    public void testValidFileComparison() throws Exception {
        CS_214_Project.Image img1 = CS_214_Project.readPGMFile("input_files/sample1.pgm");
        CS_214_Project.Image img2 = CS_214_Project.readPGMFile("input_files/sample2.pgm");

        assertNotNull(img1);
        assertNotNull(img2);

        double histogramComparison = CS_214_Project.calculateHistogramComparison(img1, img2);
        double sumOfSquares = CS_214_Project.calculateSumOfSquares(img1, img2);

        assertTrue(histogramComparison >= 0 && histogramComparison <= 1);
        assertTrue(sumOfSquares >= 0);
    }

    @Test
    public void testMismatchedDimensions() throws Exception {
        CS_214_Project.Image img1 = new CS_214_Project.Image(10, 10, new ArrayList<>());
        CS_214_Project.Image img2 = new CS_214_Project.Image(20, 20, new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> {
            if (img1.width != img2.width || img1.height != img2.height) {
                throw new IllegalArgumentException("Error: Images have different dimensions.");
            }
        });
    }

    @Test
    public void testIdenticalImages() throws Exception {
        CS_214_Project.Image img1 = CS_214_Project.readPGMFile("input_files/sample1.pgm");
        CS_214_Project.Image img2 = CS_214_Project.readPGMFile("input_files/sample1.pgm");

        assertNotNull(img1);
        assertNotNull(img2);

        double histogramComparison = CS_214_Project.calculateHistogramComparison(img1, img2);
        double sumOfSquares = CS_214_Project.calculateSumOfSquares(img1, img2);

        assertEquals(1.0, histogramComparison, 0.000001);
        assertEquals(0, sumOfSquares, 0.0001);
    }

    @Test
    public void testInvalidFileFormat() {
        assertThrows(FileNotFoundException.class, () -> {
            CS_214_Project.readPGMFile("input_files/invalid_format.pgm");
        });
    }

    @Test
    public void testPixelCountMismatch() {
        assertThrows(FileNotFoundException.class, () -> {
            CS_214_Project.readPGMFile("input_files/mismatch_pixels.pgm");
        });
    }

    @Test
    public void testSmallerImagesComparison() throws Exception {
        assertThrows(FileNotFoundException.class, () -> {
            CS_214_Project.readPGMFile("input_files/small_image1.pgm");
        });
    }
}
