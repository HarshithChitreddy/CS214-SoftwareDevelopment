[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/nH__Yddu)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=16030686)


# Programming Assignment #4  
**CS214 Fall 2024**

## "Matching Images in Java"

### Motivation

In PA3, you compared depth images using normalized histograms and direct image comparison. In this assignment, you'll extend PA3 to compare a set of images, determining the closest match for each image based on normalized histograms. This will involve manipulating dynamic sets of images. Additionally, you'll start thinking about which comparison method is more effective. 

### Task

Your program will take a single file name as an argument. The input file is a text file containing the names of image files, one filename per line. Your program will read every image listed in the input file. If there are `N` input images, the output should be `N` lines of text to `System.out`. Each line of output corresponds to one of the input images and contains (1) the input image filename, (2) whitespace, (3) the most similar image filename (based on normalized histogram measure as done in step one of PA3), and (4) the normalized histogram error measure between the two images. The lines should be in order, meaning the first line corresponds to the first input image, the second line to the second input image, and so on. The file contains both the filename and the path. You must detach the file name from the path and only print the file name.

Since the normalized histogram comparison measure is used, the input images do not need to be the same size. However, all filenames in the input file must be valid images, and there should be at least two filenames. If there is an error, your program should print a message to `System.err` and nothing else.

### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/inputfileName'"
```
Your test coverage will be graded as it has been for the last 2 programming assignments. You must achieve at least 75% test coverage to receive points on coverage.

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

After a file is made you can extract the file using the .getName() method.
```
 Example:
        File file = new File(fullpath);
        String file_name = file.getName();
```
If the file path is `input_files/fake_file.txt`, this call will return `fake_file.txt`.

- The amount of images in a given file are variable, which means that a class for the images would be usefull.
- The images structure is the same as they are in PA3, which means errors that occured in PA3 for images still apply.
- System.exit() will lead to errors in your tester code, this means that ALL of those calls need to be removed.
- Your code must be accurate to the 6th decimal place this can be done with the `System.out.format(" %.6f");`
- **We give you a very basic test, with only two images being compared, it is your job to make sure that the code can work for files with MORE than just two files**
- You should create more example files to fully test your code, this will be left to your discretion.

### Policies

All work submitted must be your own. Code written by others may not be submitted. The department's academic integrity policies apply.

Late submissions are not accepted unless due to unforeseeable emergencies, such as a medical crisis or death in the immediate family. In such cases, contact the instructor.
