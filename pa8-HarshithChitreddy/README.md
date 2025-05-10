[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/9WOKlD3B)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=16970215)
# PA8: Machine Learning!

# Motivation
Up to now, you have been writing code to cluster images into groups using pre-defined similarity 
measures. But what if you could learn a better similarity measure? You can, and this week you 
will.

In particular, this week you will create and train a perceptron, one of the first and simplest 
learning machines. In particular, you will train a perceptron to classify histograms extracted from 
depth images.

It might feel like this week’s assignment isn’t an extension of PA7, but that’s not true. You will 
be reusing code to read images, compute histograms, and manage files of files, for example. You 
will not, however, be clustering images. Do not throw your clustering code away, however; you 
will need it for PA9

# Perceptrons 
A perceptron is a linear classifier that is trained to separate instances of one class from instances of another. 
When the perceptron is given a new input sample, it produces a “classness” value. If this value is positive, 
the perceptron thinks the sample is a member of the class; if the value is negative, it thinks the sample is a 
member of some other class. The bigger the magnitude of the returned value, the more certain the 
perceptron is.

In our case, the input is a 64-element normalized histogram, of the type you have been using for many 
assignments now. The perceptron is a set of 65 weights, one for each input (i.e. one for each element in the 
histogram) plus one extra weight. We will call these weights $w_0$ through $w_{63}$, and we will call the extra 
weight b (for ‘bias’). We will use a similar terminology for the histogram: the first histogram value will be 
$h_0$, while the last will be $h_{63}$. To evaluate a perceptron on a histogram, compute the following:


$$
y = b + \sum_{i=0}^{63} w_i*h_i
$$

Our goal is not just to evaluate samples using perceptrons, however; it is to train perceptrons by setting the 
weights. We do this by initializing all the weights (including b) to zero. We will then train the weights by 
testing them on a set of training samples, and updating the weights a little bit each time.

To update the weights of the perceptron given an input sample, we compute y using the equation above 
with the current (pre-update) values of w and b, and the h values from the training sample. We also 
introduce one more value d. d is set to 1 of the training sample is positive (i.e. it comes from the target 
class) or -1 if the training sample is negative. 

We then update the weights w as follows:

$$
w_i \leftarrow w_i + (d - y)*h_i
$$

You also update the bias weight b as follows:

$$
b \leftarrow b + (d - y)
$$

Now, if we update the perceptron just once on a single training sample, the weights will only move a little 
off their initial setting. But if we update the perceptron many times with different training samples (some 
positive, some negative), it will try to accommodate all of them. If we repeat that process many times, we 
get … an ok perceptron.

# Task 

***The Header information for these .pgm files is different from previous files: An acceptable header starts with P2, not P3***

Your PA8 program will take two arguments. The first is a file of depth image file names, as in 
PA7. The filenames will be of the form classN_#.pgm, as before. You should read in the depth 
images, and compute a normalized histogram for each (as before). The second argument is a class 
number N. Any image whose filename begins with classN is a positive sample. Images with 
filenames beginning with any other class number are negative samples. 

Your program will initialize a perceptron to have all zero weights, and then it will update the 
perceptron using the images named in the first argument, where positive versus negative labels 
are assigned according to the 2nd argument. Updating the perceptron once for every input image is 
called an epoch. Your program will run 100 training epochs, meaning that if there are M training 
images in the input file, you will update the perceptron M times (once for each training image), 
and then repeat that process 100 times. There should be a total of 100*M perceptron updates.

After the 100th epoch, your program should write out your perceptron weights. In 
particular, it should print out on a single line (separated by single spaces) the 64 weights in 
order, followed by the bias (b). It should then print a newline.

***The Header information for these .pgm files is different from previous files: An acceptable header starts with P2, not P3***

### Output


```
Example for gradle run -q --args="'input_files/correctfiles.txt' '1'":
0.000000 0.000000 -0.163387 -0.151285 -0.066565 -0.344929 -0.580933 -0.596682 -0.803491 -0.537688 -0.546648 -0.159860 -0.237191 -0.229397 -0.264899 -0.204398 -0.156277 -0.259867 -0.306215 -1.402892 -1.135745 -0.554350 -0.170574 1.645242 2.672255 4.107011 5.207983 3.011428 2.062676 1.119031 0.690420 0.929341 0.438889 0.501041 0.475619 0.533738 0.553464 0.259871 0.331111 -0.062162 -0.058675 0.288081 -0.217577 -0.519618 -0.276495 -0.467983 -0.356379 -0.580643 -0.123592 -0.597473 -0.454268 -0.640858 -0.453260 -0.592533 -0.390420 -0.484363 -0.327973 -0.360029 -0.336971 -0.316612 -0.569286 -0.309974 -0.311961 -4.807033 2.337790
```


### Submitting Your Work

Your submission must be in the main branch of your GitHub repository.

### Grading Your Homework

Run your code with the following command:

```
gradle run -q --args="'input_files/inputfileName' 'x'"
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

-  Don’t recompute histograms. The normalized histograms never change.
-  The numbers must be accurate up to the 5th decimal place this can be accomplished with  ```System.out.format("%.6f ", Weight_Value_I);```
-  An acceptable header starts with P2, not P3: If you fail tests because you don't fix how you read your header you will not get pitty points.

### Policies
- 3-Day Late policy applies.
---

