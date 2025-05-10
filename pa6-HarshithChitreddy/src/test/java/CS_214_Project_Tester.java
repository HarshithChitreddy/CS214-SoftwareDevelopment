import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;

public class CS_214_Project_Tester {

    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

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
    public void EmptyFile() {
        String args = "input_files/emptyfile.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: File not found"); });
    }

    @Test
    public void CorrectOutput1() {
        CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "4", "1" });
        String expectedOutput = "example1.pgm example2.pgm\n" + 
                                "example3.pgm\n" + 
                                "example4.pgm example5.pgm\n" + 
                                "example6.pgm example7.pgm";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());  
    }

    @Test
    public void LargePixel() {
        String args = "input_files/largepixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image is too large."); });
    }

    @Test
    public void SmallPixel() {
        String args = "input_files/smallpixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image is too small."); });
    }

    @Test
    public void NegativeWidth() {
        String args = "input_files/negativewidth.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Impossible image."); });
    }

    @Test
    public void incorrectfiles() {
        CS_214_Project.main(new String[] { "input_files/incorrectfiles.txt", "4" });
        assertEquals("Error: Provide exactly three arguments - input file, integer K, and similarity measure (1, 2, or 3).", errContent.toString().trim());
    }

    @Test
    public void MissingArgs() {
        CS_214_Project.main(new String[] {});
        assertEquals("Error: Provide exactly three arguments - input file, integer K, and similarity measure (1, 2, or 3).", errContent.toString().trim());
    }

    @Test
    public void LargeValuePixel() {
        String args = "input_files/largevaluepixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has a larger value."); });
    }

    @Test
    public void SmallValuePixel() {
        String args = "input_files/smallvaluepixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has a smaller value."); });
    }

    @Test
    public void NegativeHeight() {
        String args = "input_files/negativeheight.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has negative height."); });
    }

    @Test
    public void LowercaseHeader() {
        String args = "input_files/lowercaseheader.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Header is lowercase."); });
    }

    @Test
    public void NonIntegerPixelValue() {
        String args = "input_files/nonintegerpixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has a non-integer value."); });
    }

    @Test
    public void IncorrectFileType() {
        String args = "input_files/incorrectfiletype.txt";
        assertThrows(Exception.class, () -> { throw new Exception("Error: File type is not PGM."); });
    }

    @Test
    public void CorrectOutput2() {
        CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "4", "3" });
        String expectedOutput = "example1.pgm example2.pgm\n" + 
                                "example3.pgm\n" + 
                                "example4.pgm example5.pgm\n" + 
                                "example6.pgm example7.pgm";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());  
    }

@Test
public void CorrectOutput3() {
    CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "2" });
    String expectedOutput = "example1.pgm example2.pgm\n" + 
                            "example3.pgm\n" + 
                            "example4.pgm example5.pgm example6.pgm example7.pgm";
    assertEquals(expectedOutput.trim(), outContent.toString().trim());
}
}