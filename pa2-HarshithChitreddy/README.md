[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/kRWUwmlV)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=15743544)


# Programming Assignment #2  
**CS214 Fall 2024**

## "Comparing Histograms in Java"

### Motivation

Last week, you read in and generated a histogram. This week, you'll be extending that to read in, generate, and compare two histograms. This assignment builds on the first one, so if you wrote Assignment #1 using poor design practices like putting a lot of code in `main` or using global variables, this assignment will be more challenging.

Remember, generating test cases and testing your code is largely your responsibility, and you should never trust a user or a file. In the meantime, keep these principles in mind, and you'll find the task more manageable.

### Task

Your program will take two file names as arguments (the `args` parameter in `main` should now have a length of 2, with `args[0]` and `args[1]` holding the file names). The input files are in the same format as Assignment #1: each file should contain one or more values, where a value is an integer between 0 and 255. If either file does not conform to this specification, you should print an error message to `System.err` and return from `main` with a status code of `-1`.

This time, your program will normalize each histogram by summing up the bin counts in the histogram (this should be the same as the number of integers in the file) and then dividing the counts by this sum. Note: histogram counts should now be of type `double` or `float`; otherwise, when you divide by the total count, most values will become zero. The result of histogram normalization is that the bucket counts are now percentages. For example, if the first count in the first histogram is now `0.05`, it suggests that 5% of all the values in the first file are in the range `0-3`.

Note that you normalize each histogram independently. The goal is to make it possible to compare histograms created from different lengths of files.

After you have normalized both histograms, you will multiply them together element-wise. In other words, multiply the first count in the first normalized histogram by the first count in the second normalized histogram, the second element in the first normalized histogram by the second element in the second normalized histogram, and so forth. This will result in 64 values. Sum these 64 values, **round the answer at the 8th decimal place**, and print the result to `System.out`, and return 0 from `main`.

### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/input1' 'input_files/input2'"
```
Part of your grade for this assignment will be based on your test coverage. After successfully running `gradle test` your test coverage will be available for display at `/build/jacocoHtml/index.html`. You must have a minimum of 25% coverage to get full points for this portion of your grade.

We will pull your code from you repository. It must contain, at a minimum two
`Java` files. One **must** be named `CS_214_Project.java`. We will run your program by
starting it with that class name. The second file **must** be named
`CS_214_Project_Tester.java`. This will be used to run your `JUnit` tests. All future
assignments **must** contain these two files, although they may contain
different code as needed by the particulars of that assignment.

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

- Use the command `lynx -dump build/jacocoHtml/index.html` in your terminal on the CS Dept. Machines to quickly view your unit test coverage results.
- There are helpful libraries provided in the code distributed under 'src' in this repo.
- You may use the input files we provided in this repo to test your code but you will need to develop your own tests to get full credit.
- Never trust a file or a user. You can be sure that we will include test cases with illegally formatted input, illegal arguments, etc.
- Use meaningful names for your methods and variables to avoid confusion and make your code easier to understand and debug.
- If you want to end the program early use the function `System.exit(0);`. This will stop the program from continuing. *This can be useful for stopping the program if it hits an error*
- Test your program on the department machines in CSB120. That is where we will evaluate it. Your grade depends on how your program performs on those machines. (Warning: Java may behave differently across different environments.)

### Policies

All work you submit must be your own. You may not submit code written by a peer, a former student, or anyone else. You may not copy or buy code from the web. The department academic integrity policies apply.

There is a 3 day late period for this assignment but each day it is late your grade will receive a 10% deduction.
