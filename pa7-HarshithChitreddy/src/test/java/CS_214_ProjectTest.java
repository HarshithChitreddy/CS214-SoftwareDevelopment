import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;

public class CS_214_ProjectTest {

private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
private static ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
private static final PrintStream originalOut = System.out;
private static final PrintStream originalErr = System.err;

@BeforeEach
public void setupStreams() {
outputStream = new ByteArrayOutputStream();
errorStream = new ByteArrayOutputStream();
System.setOut(new PrintStream(outputStream));
System.setErr(new PrintStream(errorStream));
}

@AfterEach
public void resetStreams() {
System.setOut(originalOut);
System.setErr(originalErr);
}

@Test
public void testCorrectOutputWithMeasure1() {
    CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "1" });
    String output = outputStream.toString().trim();
    double score = Double.parseDouble(output);
    assertEquals(0.857143, score, 0.000001);
}

@Test
public void testCorrectOutputWithMeasure2() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "2" });
String output = outputStream.toString().trim();
double score = Double.parseDouble(output);
assertEquals(0.714286, score, 0.000001);
}

@Test
public void testCorrectOutputWithMeasure3() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "4", "3" });
String output = outputStream.toString().trim();
double score = Double.parseDouble(output);
assertEquals(1.000000, score, 0.000001);
}

@Test
public void testCorrectOutputWithMeasure4() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "4" });
String output = outputStream.toString().trim();
double score = Double.parseDouble(output);
assertEquals(0.714286, score, 0.000001);
}

//@Test
//public void testEmptyFileError() {
//CS_214_Project.main(new String[] { "input_files/emptyfile.txt", "3", "1" });
//assertEquals("Error: Input file not found - input_files/empty_file.txt", errorStream.toString().trim());
//}

@Test
public void testArgumentCountError() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3" });
assertEquals("Error: Provide exactly three arguments - input file, integer K, and similarity measure (1, 2, 3, or 4).", errorStream.toString().trim());
}

@Test
public void testInvalidSimilarityMeasure() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "5" });
assertEquals("Error: K must be greater than zero and similarity measure must be 1, 2, 3, or 4.", errorStream.toString().trim());
}

@Test
public void testInvalidKValue() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "0", "1" });
assertEquals("Error: K must be greater than zero and similarity measure must be 1, 2, 3, or 4.", errorStream.toString().trim());
}

@Test
public void testKGreaterThanImageCount() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "10", "1" });
assertEquals("Error: K cannot be greater than the number of images (7).", errorStream.toString().trim());
}

@Test
public void testNonIntegerKValue() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3.5", "1" });
assertEquals("Error: K and similarity measure must be integers.", errorStream.toString().trim());
}

@Test
public void testNonIntegerSimilarityMeasure() {
CS_214_Project.main(new String[] { "input_files/correctfiles.txt", "3", "1.5" });
assertEquals("Error: K and similarity measure must be integers.", errorStream.toString().trim());
}
}