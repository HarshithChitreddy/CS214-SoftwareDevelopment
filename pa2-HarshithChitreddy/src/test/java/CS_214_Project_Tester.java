import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeEach;

import java.io.*;

public class CS_214_Project_Tester {
    ByteArrayOutputStream out;
    ByteArrayOutputStream err;
    PrintStream originalOp;
    PrintStream originalError;

    @BeforeEach
    public void setUp(){
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        originalOp = System.out;
        originalError = System.err;
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    public void tearDown(){
        System.setOut(originalOp);
        System.setErr(originalError);
    }

    @Test
    public void test1_204rightinput_766rightinput() {
        CS_214_Project.main(new String[]{"input_files/204_right_input.txt", "input_files/776_right_input.txt"});
        String exp_Op = "0.597841";
        assertEquals(exp_Op, out.toString().trim());
    }

    @Test
    public void test2_204rightinput_204rightinput() {
        CS_214_Project.main(new String[]{"input_files/204_right_input.txt", "input_files/204_right_input.txt"});
        String exp_Op = "0.661952";
        assertEquals(exp_Op,out.toString().trim());
    }

    @Test
    public void test4_singletonmatch() {
        CS_214_Project.main(new String[]{"input_files/singletonmatch1.txt", "input_files/singletonmatch2.txt"});
        String exp_Op = "1.000000";
        assertEquals(exp_Op,out.toString().trim());
    }

    @Test
    public void test3_error_negative_input() {
        CS_214_Project.main(new String[]{"input_files/204_right_input.txt", "input_files/error_negative_input.txt"});
        String errorOutput = err.toString();
        assertTrue(errorOutput.contains("Error: Out of range input value (0-255 in file"));
    }   
}