[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/V81MFnyR)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=16657725)


# Programming Assignment #6  
**CS214, Fall 2024**


## "Better Image Comparisons in Java"

### Motivation

By now, your program has grown significantly. This assignment will challenge you to modify it to handle different image similarity measures. Additionally, this assignment introduces inheritance opportunities in your object-oriented design.

### Task

Your PA6 program will take three arguments:
1. A text file containing a list of filenames of depth images.
2. An integer `K` representing the number of clusters (between 1 and `N`, where `N` is the number of images).
3. An integer specifying the similarity measure to use (1 for NormHist, 2 for NormHist4, 3 for InvSquareDiff).

In PA6, your program will take three arguments. The first two are the same as the arguments to 
PA5. The first is a single file containing a list of filenames of depth images, and the second the 
number of clusters K. As in PA5, your program will cluster the N images into K clusters using 
agglomerative clustering. Unlike in PA5, all depth images must be 128x128 pixels. Any other 
size is an error.

This time, your goal is to test different image similarity measures. In PA5, the image similarity 
measure was normalized histogram intersection (from here on out, **NormHist**). This is a pretty 
weak image similarity measure. It turns out, it is better if you localize the histograms. So for our 
second similarity measure, we will divide the image into quarters. The first quarter has X 
coordinates from 0 to 63 and Y coordinates from 0 to 63. The second quarter has X coordinates 
from 64 to 127, and Y coordinates from 0 to 63. The third quarter is X in [0, 63] and Y in [64, 
127], while the 4th quarter is X in [64, 127] and Y in [64, 127]. The second similarity measure 
(**NormHist4**) creates four normalized histograms from every image – one for each quarter – and 
compares images by computing the normalized histogram intersection between the 1st quarters of 
the two images, the second quarters, and so forth, and then taking the average of the four 
normalized histogram intersection measures.

We will also use a 3rd similarity measure. Back in PA3, you compared images using the sum of 
the pixel-wise squared difference. That is a distance measure, not a similarity measure, because a 
perfect match is small instead of large. So we will convert that into a similarity measure by taking 
its inverse. In particular, if ‘d’ is the sum of pixel-wise differences between two images, let $S = 1 
/ (d + 1)$ be the third similarity measure. We will call this similarity measure **InvSquareDiff**. 

Your task in PA6 is to cluster the set of images into K clusters using agglomerative clustering and 
one of the 3 similarity measures, as determined by the 3rd argument. If the 3rd argument is 1, you 
should cluster the images using **NormHist**. If the 3rd argument is 2, you should cluster the 
images using **NormHist4**. If the 3rd argument is 3, cluster the images using **InvSquareDiff**. Either 
way, you then output the result of clustering as in PA5.

The program will cluster the images using agglomerative clustering based on the selected similarity measure.

#### Similarity Measures:
1. **NormHist**: As in PA5, compares images using a normalized histogram of the entire image.
2. **NormHist4**: Divides each image into four quarters, calculates normalized histograms for each quarter, and averages the four histogram intersection measures.
3. **InvSquareDiff**: Converts the sum of pixel-wise squared differences into a similarity measure by taking its inverse: $S = \frac{1}{d + 1}$, where $d$ is the sum of squared differences.

### Output

The output should be `K` lines, each containing the filenames of the images in one cluster, similar to PA5. Each image filename should appear exactly once.

```
Example for gradle run -q --args="'input_files/correctfiles.txt' '4' '1'":
	example1.pgm example2.pgm 
	example3.pgm 
	example4.pgm example5.pgm 
	example6.pgm example7.pgm  
```
```
gradle run -q --args="'input_files/correctfiles.txt' '3' '2'"
example1.pgm example2.pgm 
example3.pgm 
example4.pgm example5.pgm example6.pgm example7.pgm 
```
```
Example for gradle run -q --args="'input_files/correctfiles.txt' '4' '3'":
	example1.pgm example2.pgm 
	example3.pgm 
	example4.pgm example5.pgm 
	example6.pgm example7.pgm  
```

### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/inputfileName' 'K' 'X'"
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

- Use inheritance to implement the three different similarity measures.
- Reuse code within the `NormHist4` measure for computing the histograms.
- There should only be one agglomerative clustering routine in your code.

### Policies
3-Day Late Period applies.

---
