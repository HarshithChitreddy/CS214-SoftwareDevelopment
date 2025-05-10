[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/ucrWprFL)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=15950632)


# Programming Assignment #3  
**CS214 Fall 2024**


## "Comparing Images in Java"

### Motivation

In the last two weeks, you have been creating histograms from sets of numbers. However, these weren't just arbitrary numbers; they were depth images of human hands. A depth image, similar to a standard image, consists of pixels arranged in an (x, y) grid, but instead of color, each pixel represents a distance from the camera to the object. The value `255` signifies that the distance is too great for the sensor to measure.

This week, you'll be dealing with actual image files in `.pgm` format, which include header information that specifies the image size, allowing you to view the inputs as images. You'll need to adapt your code to read these files, handle potential errors, and modify your histogram comparison logic. Additionally, you'll implement a new method of comparing two depth images.

### Task

Your program will take two file names as arguments. The input files should be images in `.pgm` format. Each `.pgm` file begins with the characters `P3`, followed by whitespace, the image width, height, and the maximum pixel value (255).

It will look something like:
P3
126 126
255
white space means it could also all be on one line.

The image data consists of integers between 0 and 255, separated by whitespace, with exactly `width * height` values. If the file does not conform to this format, your program should print an error message to `System.err` and nothing else.

**Step 1: Comparing Histograms**

In the previous assignment, you compared two histograms by normalizing them, multiplying corresponding bins, and summing the results. This time, you'll normalize the histograms as before, but instead of multiplying, you'll compute the pairwise minimum of the bins and sum these minimums. This will produce a value between `0` and `1`.

**Step 2: Pixel-by-Pixel Comparison**

You'll also compare the two images directly without creating histograms. For each pixel, subtract the value in the first image from the value in the second image, square the result, and sum these squared differences. This gives the sum of squared differences between the images. This requires storing the raw pixel values of the images.
If the two files contain images that are of diffrent sizes, i.e. the height and width are not the same for both images, then the program should print an error.

When there are no errors, your program should print two values to `System.out`, separated by whitespace: the histogram comparison value (between `0` and `1`) and the sum of squared differences (a large value).

### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/inputfileName' 'input_files/inputfileName'"
```
Part of your grade for this assignment will be based on your test coverage. After successfully running `gradle test` your test coverage will be available for display at `/build/jacocoHtml/index.html`. You must have a minimum of 50% coverage to get full points for this portion of your grade.

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

- Ensure the number of pixels declared in the header matches the number of pixels in the file.
- If the dimensions of the image are negetive it should print an error message
- Test your own code, just because the sample inputs gennerate a correct output does not mean you cover all the diffrent casses 

### Policies

All work you submit must be your own. You may not submit code written by others. The department's academic integrity policies apply.

Late submissions are not accepted unless an unforeseeable emergency occurs, such as a medical crisis or a death in the immediate family.
