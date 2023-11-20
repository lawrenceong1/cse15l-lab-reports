# Lab Report 4

## Log into ieng6:

input:
```
ssh cs15lfa23bb@ieng6.ucsd.edu
```

output:
```
Last login: Mon Oct 16 10:19:17 2023 from 69.196.45.21
Hello cs15lfa23bb, you are currently logged into ieng6-202.ucsd.edu

You are using 0% CPU on this system

Cluster Status 
Hostname     Time    #Users  Load  Averages  
ieng6-201   19:40:02   22  22.54,  22.17,  22.14
ieng6-202   19:40:01   16  1.04,   1.13,   1.13
ieng6-203   19:40:01   13  4.04,   4.09,   4.17

 
Sun Nov 19, 2023  7:41pm - Prepping cs15lfa23
```

## Clone your fork of the repository from your Github account (using the SSH URL):

input:
```
git clone https://github.com/lawrenceong1/lab7.git
```

output:
```
Cloning into 'lab7'...
remote: Enumerating objects: 58, done.
remote: Counting objects: 100% (24/24), done.
remote: Compressing objects: 100% (12/12), done.
remote: Total 58 (delta 15), reused 12 (delta 12), pack-reused 34
Receiving objects: 100% (58/58), 376.37 KiB | 1.27 MiB/s, done.
Resolving deltas: 100% (21/21), done.
```

## Run the tests, demonstrating that they fail:

```cd lab7```: Change the directory our lab7 directory

```bash test.sh```: Run input: ```test.sh``` which will test our code:

Output:
```
JUnit version 4.13.2
..E
Time: 0.613
There was 1 failure:
1) testMerge2(ListExamplesTests)
org.junit.runners.model.TestTimedOutException: test timed out after 500 milliseconds
        at java.base/java.util.Arrays.copyOf(Arrays.java:3512)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3481)
        at java.base/java.util.ArrayList.grow(ArrayList.java:237)
        at java.base/java.util.ArrayList.grow(ArrayList.java:244)
        at java.base/java.util.ArrayList.add(ArrayList.java:454)
        at java.base/java.util.ArrayList.add(ArrayList.java:467)
        at ListExamples.merge(ListExamples.java:42)
        at ListExamplesTests.testMerge2(ListExamplesTests.java:19)

FAILURES!!!
Tests run: 2,  Failures: 1
```

## Edit the code file to fix the failing test

input: ```vim ListExamples.java```

keys pressed: ```j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j```: The key ```j``` is pressed 43 times starting from line 1 to get to line 44 of ```index1 += 1;```.
keys pressed: ```l l l l l l l l l l l```: They key ```l``` is pressed 11 times in order to get to the 1 portion of ```index1```.
Now that we are on the string ```1```, we press the key ```x``` in order to delete the string ```1```. 
Now we press, ```i```, in order to enter insert mode. We then press the key ```2```, so now the new line should look like ```index2 += 1;```.
Then, we press ```<esc>``` in order to exit insert mode. Then, we type the input ```:wq``` in order to save the ```ListExamples.java``` and exit the vim editor.

## Run the tests, demonstrating that they now succeed
input: ```bash test.sh```

output:
```
JUnit version 4.13.2
..
Time: 0.017

OK (2 tests)
```

## Commit and push the resulting change to your Github account (you can pick any commit message!)

input: ```git add ListExamples.java``` add ListExamples.java to the staging area

input: ```git commit -m "Update ListExamples.java"``` make a commit with a commit message

input: ```git push``` -- push the changes to Github

This then outputs this:
```
Warning: Permanently added the RSA host key for IP address '140.82.113.4' to the list of known hosts.
Enumerating objects: 5, done.
Counting objects: 100% (5/5), done.
Delta compression using up to 8 threads
Compressing objects: 100% (3/3), done.
Writing objects: 100% (3/3), 315 bytes | 315.00 KiB/s, done.
Total 3 (delta 2), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (2/2), completed with 2 local objects.
To github.com:lawrenceong1/lab7.git
   327ab1a..a4558b0  main -> main
```

The change has been successfully committed and pushed to Github.
