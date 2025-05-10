import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CS_214_Project_Tester {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalStdOut = System.out;
    private final PrintStream originalStdErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(outputStreamCaptor)); // Capture stderr too
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalStdOut);
        System.setErr(originalStdErr);
    }

    private void createTestFile(String fileName, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
        }
    }

    @Test
    public void testNonIntegerInput() throws IOException {
        String inputData = "0 1 2 3 hello\n";
        String testFileName = "non_integer_input.txt";
        createTestFile(testFileName, inputData);

        // Call the main method and check the output
        CS_214_Project.main(new String[]{testFileName});

        // Check if the output matches the expected error message
        assertEquals("Error: Invalid input detected.", outputStreamCaptor.toString().trim());
    }
}
