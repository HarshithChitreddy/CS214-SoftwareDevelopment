[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/cfUO4xGe)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=16207907)


---

# Programming Assignment #5  
**CS214 Fall 2024**


## "Clustering Images in Java"

### Motivation

In PA4, you converted a set of images into normalized histograms and found the most similar image for each one. Now, you'll extend this to clustering images based on their similarities using an algorithm called agglomerative clustering. The task is more complex, emphasizing **object-oriented design**.

### Task

Your PA5 program will take two command-line arguments:
1. A text file containing a list of filenames of depth images. This is structured the same as PA4's input files.
2. An integer `K`, representing the target number of clusters. `K` should be greater than zero and less than or equal to the number of images.

The goal is to cluster the images into `K` groups based on their normalized histogram similarities. 

#### Clustering Process:
- Start with each image in its own cluster.
- If `K = N`, where `N` is the number of images, the clustering is complete.
- If `K < N`, find the two most similar clusters and merge them. Repeat this process until the number of clusters equals `K`.

#### Definitions:
- A cluster is a set of one or more images.
- The normalized histogram of a cluster is the average of the normalized histograms of the images in the cluster.This means that each cluster will have its own histogram that will be used for the determining similarity to other images. This histogram should be updated as new images get added to the cluster. 
- The similarity between two clusters is the similarity between their normalized histograms, The pairwise minimum calculated in PA3 and PA4.

### Output

Your program should output `K` lines, each containing the filenames of the images in one cluster. Each image filename should appear exactly once. If there is an error, your program should print a meaningful message beginning with `Error` to `System.err` and nothing else. The file names in one cluster must be seperated by one space, and seperate clusters must be seperated by one newline, any other seperation by whitespace will not be accepted.
```
Example for gradle run -q --args="'input_files/correctfiles.txt' '4'":
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
gradle run -q --args="'input_files/inputfileName' 'K'"
```
We expect your code to obtain at least 75\% coverage to receive credit for testing.

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

- This project builds off of PA4, the error cases in previous PA's will still be error cases in this PA
- Each of the Clusters should have the names sorted, this can be done with a single call if you keep all names stored in a list.
```
 Example:
        ArrayList<String> names = new ArrayList<String>
        /*add all the clusters to the correct place, and add all the names to names.
        code...
        */
        Collections.sort(names);
        /*Names is now Sorted*/
```
- This Project is an order of magnitude more complicated if you neglect to implement an object oriented programming based approach. If you come to the TA’s with errors and bugs, and your code is all in primitive arrays, you might not be helped. 
- You should create more example files to fully test your code, this will be left to your discretion.

### Policies
3-Day Late policy applies.

