import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
 
import java.io.PrintStream;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterAll;
import java.io.ByteArrayOutputStream;
 
public class CS_214_Project_Tester {
 
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;
    private CS_214_Project X;
 
    @AfterEach
    public void cleanup() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
 
    @BeforeEach
    public void build() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        // CS_214_Project.clear();
    }
 
    private void createTestFile(String fileName, String content) throws Exception {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(content);
        }
    }
 
    // @Test
    // public void testValidPGMFile() throws Exception {
    // String pgmContent = "P3\n2 2\n255\n0 255 0 255\n255 0 255 0";
    // createTestFile("input_files/example1.pgm", pgmContent);
 
    // CS_214_Project.Image image =
    // CS_214_Project.readPGMFile("input_files/example1.pgm");
    // assertNotNull(image);
    // assertEquals(2, image.width);
    // assertEquals(2, image.height);
    // assertEquals(4, image.pixelData.size());
    // }
 
    // @Test
    // public void testInvalidFileFormat() {
    // Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    // String invalidContent = "P4\n2 2\n255\n255 0 255 0";
    // createTestFile("input_files/invalid_format.pgm", invalidContent);
    // CS_214_Project.readPGMFile("input_files/invalid_format.pgm");
    // });
 
    // String expectedMessage = "Invalid file format.";
    // String actualMessage = exception.getMessage();
    // assertTrue(actualMessage.contains(expectedMessage));
    // }
 
    @Test
    public void testNegativeWidth() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            String negativeWidthContent = "P3\n-2 2\n255\n0 255 0 255";
            createTestFile("input_files/negativeWidth.pgm", negativeWidthContent);
            CS_214_Project.readPGMFile("input_files/negativeWidth.pgm");
        });
 
        String expectedMessage = "Invalid image dimensions or pixel value.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
 
    @Test
    public void testNegativeHeight() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            String negativeHeightContent = "P3\n2 -2\n255\n0 255 0 255";
            createTestFile("input_files/negativeHeight.pgm", negativeHeightContent);
            CS_214_Project.readPGMFile("input_files/negativeHeight.pgm");
        });
 
        String expectedMessage = "Invalid image dimensions or pixel value.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
 
    @Test
    public void testMaxPixelValueExceeds255() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            String invalidMaxPixelContent = "P3\n2 2\n300\n0 255 0 255";
            createTestFile("input_files/morePixel.pgm", invalidMaxPixelContent);
            CS_214_Project.readPGMFile("input_files/morePixel.pgm");
        });
 
        String expectedMessage = "Invalid image dimensions or pixel value.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
 
    @Test
    public void testPixelCountMismatch() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            String mismatchContent = "P3\n2 2\n255\n0 255 0";
            createTestFile("input_files/mismatchPixcel.pgm", mismatchContent);
            CS_214_Project.readPGMFile("input_files/mismatchPixcel.pgm");
        });
 
        String expectedMessage = "Pixel count mismatch.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
 
    @Test
    public void testFileNotFound() {
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            // Attempt to read a non-existent file
            CS_214_Project.readPGMFile("input_files/non_existent_file.pgm");
        });
 
        String expectedMessage = "input_files/non_existent_file.pgm (No such file or directory)";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("No such file or directory"));
    }
 
    @Test
    public void testMissingHeader() {
        String inputFileName = "input_files/missingHeader.pgm";
 
 
    @Test
    public void testPixelSizeMismatch() {
        String inputFileName = "input_files/mismatchPixcel.pgm";
 
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CS_214_Project.readPGMFile(inputFileName);
        });
 
        String expectedMessage = "Pixel count mismatch.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
 
    @Test
    public void rightOutput() {
        CS_214_Project.main(new String[] { "input_files/twofiles.txt" });
        String output = "example1.pgm example2.pgm 0.990784\n" +
                "example2.pgm example1.pgm 0.990784";
        assertEquals(output, outContent.toString().trim());
    }
 
}
}
 