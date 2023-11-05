# Lab Report 3
## Part 1 - Bugs

The buggy code being used:
```
  static int[] reversed(int[] arr) {
    int[] newArray = new int[arr.length];
    for(int i = 0; i < arr.length; i += 1) {
      arr[i] = newArray[arr.length - i - 1];
    }
    return arr;
  }
```
### A failure-inducing input for the buggy program, as a JUnit test and any associated code (write it as a code block in Markdown):
```
@Test 
public void testReversed() {
    int[] input1 = { 3, 4 };
    int[] output = ArrayExamples.reverse(input1);
    assertArrayEquals(new int[]{ 4, 3 }, output);
}
```

### The symptom, as the output of running the tests (provide it as a screenshot):
![Error](errormessage.png)

### The bug, as the before-and-after code change required to fix it (as two code blocks in Markdown):
The for loop body needs to assign the ```newArray``` with values from the old array using the index ```arr.length - i - 1``` to be in reverse order. The old code only assigned the old array to the new array values, and the new array values only had 0, so the new array values did not change.
Also, the return statement should return ```newArray``` since it should return the new array and not the old array, since we are creating a new array.
The corrected code:
```
      newArray[i] = arr[arr.length - i - 1];
    }
    return newArray;
  }
```
### An input that doesn’t induce a failure, as a JUnit test and any associated code (write it as a code block in Markdown)
```
@Test 
public void testReversed2() {
    int[] input1 = { 3, 4, 5 };
    int[] output = ArrayExamples.reverse(input1);
    assertArrayEquals(new int[]{ 5, 4, 3 }, output);
}
```
### The symptom, as the output of running the tests (provide it as a screenshot):
![Success](successmessage.png)

## Part 2 - Researching Commands
I will use the ```find``` command for this.

