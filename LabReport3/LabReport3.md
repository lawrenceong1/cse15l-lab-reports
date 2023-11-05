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
A failure-inducing input for the buggy program, as a JUnit test and any associated code (write it as a code block in Markdown):
```
	@Test 
	public void testReversed() {
    int[] input1 = { 3, 4 };
    output = ArrayExamples.reverse(input1);
    expected = { 4, 3 };
    assertArrayEquals(new int[]{ 4, 3 }, output);
	}
```

The symptom, as the output of running the tests (provide it as a screenshot):


The bug, as the before-and-after code change required to fix it (as two code blocks in Markdown)

The corrected code:
```
  static int[] reversed(int[] arr) {
    int[] newArray = new int[arr.length];
    for(int i = 0; i < arr.length; i += 1) {
      newArray[i] = arr[arr.length - i - 1];
    }
    return newArray;
  }
```


