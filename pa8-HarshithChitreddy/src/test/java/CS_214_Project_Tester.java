import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CS_214_Project_Tester {

    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorContent = new ByteArrayOutputStream();
    private final PrintStream originalOutput = System.out;
    private final PrintStream originalError = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputContent));
        System.setErr(new PrintStream(errorContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOutput);
        System.setErr(originalError);
    }

    @Test
    public void testValidInputFileAndClassNumber() {
        CS_214_Project.main(new String[]{"input_files/correctfiles.txt", "1"});
        String output = outputContent.toString().trim();
        String[] values = output.split("\\s+");
        
        assertEquals(65, values.length);
        
        for (String value : values) {
            assertDoesNotThrow(() -> Double.parseDouble(value));
        }
    }

    @Test
    public void testInvalidArgumentCount() {
        CS_214_Project.main(new String[]{"input_files/correctfiles.txt"});
        assertTrue(errorContent.toString().contains("Error: Provide exactly two arguments"));
    }

    @Test
    public void testInvalidClassNumberFormat() {
        CS_214_Project.main(new String[]{"input_files/correctfiles.txt", "notANumber"});
        assertTrue(errorContent.toString().contains("Error: Class number must be an integer"));
    }

    @Test
    public void testNonexistentInputFile() {
        CS_214_Project.main(new String[]{"nonexistent_file.txt", "1"});
        assertTrue(errorContent.toString().contains("Error: Input file not found"));
    }


    @Test
    public void testInvalidImageFileFormat() throws IOException {
        File invalidFile = new File("invalid_image.txt");
        FileWriter writer = new FileWriter(invalidFile);
        writer.write("invalid_image.pgm");
        writer.close();
        CS_214_Project.main(new String[]{"invalid_image.txt", "1"});
        assertTrue(errorContent.toString().contains("Error processing file"));
        invalidFile.delete();
    }

    @Test
    public void testValidClassNumberRange() {
        for (int i = 1; i <= 5; i++) {
            outputContent.reset();
            errorContent.reset();
            CS_214_Project.main(new String[]{"input_files/correctfiles.txt", String.valueOf(i)});
            String output = outputContent.toString().trim();
            String[] values = output.split("\\s+");
            assertEquals(65, values.length);
        }
    }


    @Test
    public void testReadPGMFileWithInvalidFormat() throws IOException {
        File invalidFile = createInvalidPGMFile("invalid_format.pgm", "P2\n128 128\n255\n0 0 0");
        assertThrows(IllegalArgumentException.class, () -> CS_214_Project.readPGMFile(invalidFile.getPath()));
        invalidFile.delete();
    }

    @Test
    public void testReadPGMFileWithInvalidDimensions() throws IOException {
        File invalidFile = createInvalidPGMFile("invalid_dimensions.pgm", "P3\n64 64\n255\n0 0 0");
        assertThrows(IllegalArgumentException.class, () -> CS_214_Project.readPGMFile(invalidFile.getPath()));
        invalidFile.delete();
    }

    private File createInvalidPGMFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
        return file;
    }
}
