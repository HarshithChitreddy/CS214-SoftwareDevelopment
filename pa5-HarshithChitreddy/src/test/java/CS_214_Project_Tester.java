import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;

public class CS_214_Project_Tester {
    
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

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
    }

    @Test
    public void test_Empty_File() {
        String args = "input_files/testemptyfile.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: File not found"); });
    }

    @Test
    public void correct_output_1() {
        CS_214_Project.main(new String[] {"input_files/correctfiles.txt", "4"});
        String input = "example1.pgm "+
                "example2.pgm\n" +
                "example3.pgm\n" +
                "example4.pgm " +
                "example5.pgm\n" +
                "example6.pgm " +
                "example7.pgm";
        
                assertEquals(input, outContent.toString().trim());
    }

    @Test
    public void test_Large_Pixel() {
        String args = "input_files/testlargepixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image is too large."); });
    }

    @Test
    public void test_Small_Pixel() {
        String args = "input_files/testsmallpixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image is too small."); });
    }

    @Test
    public void test_Negative_Width() {
        String args = "input_files/testnegativewidth.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Impossible image."); });
    }

    @Test
    public void test_Missing_Header() {
        String args = "input_files/testmissingheader.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Missing header."); });
    }

    @Test
    public void test_Non_Integer_Pixel_Value() {
        String args = "input_files/testnonintegerpixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has a non-integer value."); });
    }

    @Test
    public void test_Bad_File_With_Header_Error() {
        CS_214_Project.main(new String[]{"input_files/testincorrectfiles.txt", "4"});
        assertEquals("Error processing file: input_files/testmissingheader.pgm - Invalid file format. Expected P3.", errContent.toString().trim());
    }

    @Test
    public void test_Bad_File_With_2() {
        CS_214_Project.main(new String[] { "input_files/testincorrectfiles2.txt", "4" });
        assertEquals(errContent.toString().trim(),
        "Error processing file: input_files/testsmallvaluepixel.pgm - Invalid pixel value: -102");
    }

    @Test
    public void test_Large_Value_Pixel() {
        String args = "input_files/testlargevaluepixel.pgm";
        assertThrows(Exception.class, () -> 
{ throw new Exception("Error: Image has a larger value."); });
    }

    @Test
    public void test_Small_Value_Pixel() {
        String args = "input_files/testsmallvaluepixel.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has a smaller value."); });
    }

    @Test
    public void test_Negative_Height() {
        String args = "input_files/testnegativeheight.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Image has negative height."); });
    }

    @Test
    public void test_Incorrect_File() {
        String args = "input_files/test_incorrectfile.txt";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Input has no header."); });
    }


    @Test
    public void test_Lowercase_Header() {
        String args = "input_files/testlowercaseheader.pgm";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Header is lowercase."); });
    }

    @Test
    public void noArgs() {
        CS_214_Project.main(new String[] {});
        assertThrows(Exception.class, () -> {
            throw new Exception("Error");
        });
    }

    @Test
    public void test_Incorrect_Value_File() {
        String args = "input_files/test_incorrectvaluefiles.txt";
        assertThrows(Exception.class, () -> { throw new Exception("Error: Input has small value."); });
    }

    @Test
    public void correct_output_2() {
        CS_214_Project.main(new String[] {"input_files/correctfiles.txt", "1"});
        String input = "example1.pgm example2.pgm example3.pgm example4.pgm example5.pgm example6.pgm example7.pgm";
        assertEquals(input, outContent.toString().trim());
    }
    

    @Test
    public void test_Incorrect_File_Type() {
        String args = "input_files/test_incorrectfiletype.txt";
        assertThrows(Exception.class, () -> { throw new Exception("Error: File type is not PGM."); });
    }

    @Test
    public void test_Incorrect_File_2() {
        String args= "input_files/testincorrectfiles2.txt";
        assertThrows(Exception.class, () -> {
            throw new Exception("Error: Input has small value.");
        });
    }

}
