# Programming Assignment #1

## CS214 Fall 2024

## Histograms

### Motivation

The primary goal of this assignment is to give you a gentle introduction to the more demanding expectations in CS214. For starters, you should notice that the assignment specifies what to do, not how to do it. We will not tell you what classes to define or what methods they should have (although we may give hints). You are expected to design your own programs from scratch. Program design is part of the assignment. Also, there may be times when part of the assignment specification is ambiguous. In such cases, it is incumbent on you to ask the instructor for clarification. The instructor is playing the role of client in these exercises, and what the client intends is correct. If you are not sure of exactly what the client wants, you need to find out. "But I thought it was supposed to…" is not an excuse.

Along the same lines, testing your program is your responsibility. We will not hand out every test file you need. Nor is it sufficient to handle legal input files; when given an illegal file, you must print an error statement starting with "Error" followed by a description of the error then cease running. To do this, you need to come up with your own test files and test your code. Indeed, designing test cases is an important part of program design. Testing for errors is your responsibility. The assignment below is to compute a histogram. But here is a hint: never trust a user or a file. What if the file has letters in it? Or punctuation? Or nothing at all? It is part of this and all following assignments to test for error cases and handle them cleanly.

### Task

Your program will take a single file name as an argument. The text file should contain 1 or more values, where a value is an integer between 0 and 255. Your program will write 64 numbers (separated by white space) to `System.out`. The first value written to `System.out` is the number of input values in the range [0, 3] (i.e., 0 to 3 inclusive). The second output value is the number of input values in the range [4, 7]. In general, the nth output value is the number of input values in the range [4×(n-1), (4×n)-1]. Equivalently, input value X is added to output count number `Math.floor(X/4)`.

The format of the input file is flexible. It may contain many numbers on one line, or many lines with one (or a few) numbers each. It may contain blank lines. However, it should only contain integers between 0 and 255 (inclusive) and white space. If the file contains numbers outside of this range, or anything other than integer numbers, your program should write out an error message to `System.err` starting with `Error` followed by a descriptive message. This program should not output anything other than an error message. Otherwise, your program should write 64 values to `System.out`.

### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/inputfileName'"
```
We will pull your code from your repository. It must contain, at a minimum two
`Java` files. One **must** be named `CS_214_Project.java`. We will run your
program by starting it with that class name. The second file **must** be named
`CS_214_Project_Tester.java`. This will be used to run your `JUnit` tests.
All future assignments **must** contain these two files, although they may
contain different code as needed by the particulars of that assignment.

~~~~
public class CS_214_Project {
  public static void main (String[] args) {
    // your code goes here
    // You may just call the main of some other class you wrote.
  }
}
~~~~

~~~~
import org.junit.Test;

// other imports as needed

public CS_214_Project_Tester {
  @Test
  public void test1_OrWhateverNameMakesSense() {
    // code for your first test
  }

  @Test
  public void test2_OrWhateverNameMakesSense() {
    // code for your second test
  }
}
~~~~

### Hints

- There are helpful libraries provided in the code distributed under 'src' in this repo.
- You may use the input files we provided in this repo to test your code but you will need to develop your own tests to get full credit.
- Never trust a file or a user. You can be sure that we will include test cases with illegally formatted input, illegal arguments, etc.
- Use meaningful names for your methods and variables to avoid confusion and make your code easier to understand and debug.
- If you want to end the program early use the function `System.exit(0);`. This will stop the program from continuing. *This can be useful for stopping the program if it hits an error*
- Test your program on the department machines in CSB120. That is where we will evaluate it. Your grade depends on how your program performs on those machines. (Warning: Java may behave differently across different environments.)

### Policies

All work you submit must be your own. You may not submit code written by a peer, a former student, or anyone else. You may not copy or buy code from the web. The department academic integrity policies apply.

There is a 3 day late period for this assignment but each day it is late your grade will receive a 10% deduction.
