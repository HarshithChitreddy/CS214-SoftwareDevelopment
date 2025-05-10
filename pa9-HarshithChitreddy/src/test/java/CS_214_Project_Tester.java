import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CS_214_Project_Tester {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testInvalidArgumentsCount() {
        CS_214_Project.main(new String[]{"input_files/train.txt", "3"});
        assertTrue(errContent.toString().contains("Error: Provide exactly three arguments"));
    }

    @Test
    public void testNonIntegerK() {
        CS_214_Project.main(new String[]{"input_files/train.txt", "input_files/test.txt", "three"});
        assertTrue(errContent.toString().contains("Error: K must be an integer."));
    }

    @Test
    public void testInvalidKValue() {
        CS_214_Project.main(new String[]{"input_files/train.txt", "input_files/test.txt", "0"});
        assertTrue(errContent.toString().contains("Error: K must be a positive integer."));
    }

    @Test
    public void testInsufficientClassesInTraining() {
        String invalidTrainFile = "input_files/invalid_train.txt";
        createFile(invalidTrainFile, "input_files/train/class1_8.pgm");
        CS_214_Project.main(new String[]{invalidTrainFile, "input_files/test.txt", "3"});
        assertTrue(errContent.toString().contains("Error: Training data must have at least two classes."));
        deleteFile(invalidTrainFile);
    }

    @Test
    public void testInsufficientTestImages() {
        String emptyTestFile = "input_files/empty_test.txt";
        createFile(emptyTestFile, "");
        CS_214_Project.main(new String[]{"input_files/train.txt", emptyTestFile, "3"});
        assertTrue(errContent.toString().contains("Error: Test data must contain at least one image."));
        deleteFile(emptyTestFile);
    }

    @Test
    public void testSuccessfulClusteringK2() {
        CS_214_Project.main(new String[]{"input_files/train.txt", "input_files/test.txt", "2"});
        String output = outContent.toString().trim();
        String[] clusters = output.split("\n");
        assertEquals(2, clusters.length); 
        String allTestFiles = String.join(" ", clusters);
        assertTrue(allTestFiles.contains("class0_1.pgm"));
        assertTrue(allTestFiles.contains("class0_15.pgm"));
    }

    @Test
    public void testSuccessfulClusteringK3() {
        CS_214_Project.main(new String[]{"input_files/train.txt", "input_files/test.txt", "3"});
        String output = outContent.toString().trim();
        String[] clusters = output.split("\n");
        assertEquals(3, clusters.length);
        String allTestFiles = String.join(" ", clusters);
        assertTrue(allTestFiles.contains("class0_1.pgm"));
        assertTrue(allTestFiles.contains("class0_14.pgm"));
    }

    @Test
    public void testInvalidTrainFileFormat() {
        String invalidTrainFile = "input_files/invalid_format_train.txt";
        createFile(invalidTrainFile, "invalid content");
        CS_214_Project.main(new String[]{invalidTrainFile, "input_files/test.txt", "3"});
        assertTrue(errContent.toString().contains("Error processing training file"));
        deleteFile(invalidTrainFile);
    }

    private void createFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Error creating test file: " + fileName, e);
        }
    }

    private void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Error deleting test file: " + fileName);
        }
    }
}
