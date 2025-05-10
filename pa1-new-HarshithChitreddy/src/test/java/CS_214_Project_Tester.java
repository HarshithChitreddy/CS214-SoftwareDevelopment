import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

public class CS_214_Project_Tester {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    PrintStream originalOp;
    PrintStream originalError;

    @BeforeEach
    public void setUp(){
        originalOp = System.out;
        originalError = System.err;
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterAll
    public static void tearDown(){
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testValidInput(){
        CS_214_Project.main(new String[]{"input_files/204_right_input.txt"});

        String expectedOutput = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 7 12 12 5 30 36 61 55 105 171 135 149 135 124 163 162 178 152 134 57 39 44 28 32 16 24 37 18 16 24 21 35 37 45 40 54 65 50 70 73 82 68 91 93 81 13318";
        assertEquals(expectedOutput, out.toString().trim());
    }
    @Test
    public void testInvalidInput(){
        CS_214_Project.main(new String[]{"input_files/error_negative_input.txt"});

        String errorOp = err.toString();
        String expectedErrorOp1 = "Error: Input value out of range (0-255)";
        String expectedErrorOp2 = "Error: Invalid input. The value was not an integer.";

        assertTrue(errorOp.contains(expectedErrorOp1) || errorOp.contains(expectedErrorOp2));
    }
}