# Lab Report 5
## The original post from a student with a screenshot showing a symptom and a description of a guess at the bug/some sense of what the failure-inducing input is. (Don’t actually make the post! Just write the content that would go in such a post)
Student: Hello TA, I got this output when trying to run the command ```bash grade.sh https://github.com/ucsd-cse15l-f22/list-methods-corrected```. I know that this file should get full credit, but my output says otherwise. I was wondering if I can get some guidance on this?

```
Cloning into 'student-submission'...
remote: Enumerating objects: 3, done.
remote: Counting objects: 100% (3/3), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 3 (delta 0), reused 3 (delta 0), pack-reused 0
Receiving objects: 100% (3/3), done.
Finished cloning
Compilation failed

TestListExamples.java:28: error: cannot find symbol
        StringCheck sc = new StringCheck();
        ^
  symbol:   class StringCheck
  location: class TestListExamples
TestListExamples.java:28: error: cannot find symbol
        StringCheck sc = new StringCheck();
                             ^
  symbol:   class StringCheck
  location: class TestListExamples
2 errors

Grade: 1/5. File is found but does not compile.
```

TA: Of course you can get some help on this! Notice how you are given the error message from the grading script that the compilation failed and it prints out the compilation error for you specifically this:

```
TestListExamples.java:28: error: cannot find symbol
        StringCheck sc = new StringCheck();
        ^
  symbol:   class StringCheck
  location: class TestListExamples
TestListExamples.java:28: error: cannot find symbol
        StringCheck sc = new StringCheck();
                             ^
  symbol:   class StringCheck
  location: class TestListExamples
```

Please make sure that you have created a ```StringCheck``` class that implements ```StringChecker``` in ```TestListExamples.java```.

## Another screenshot/terminal output showing what information the student got from trying that, and a clear description of what the bug is.
After the student has added a ```StringCheck``` class that implements ```StringChecker``` in ```TestListExamples.java```, this is the output, which is what we expect:
```
Cloning into 'student-submission'...
remote: Enumerating objects: 3, done.
remote: Counting objects: 100% (3/3), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 3 (delta 0), reused 3 (delta 0), pack-reused 0
Receiving objects: 100% (3/3), done.
Finished cloning
Compilation successful.
Grade: 5/5. All tests passed.
```

## Setup information:
The file & directory structure needed
```
├── list-examples-grader
│   ├── grading-area
│   ├── lib
│   │   ├── hamcrest-core-1.3.jar
│   │   ├── junit-4.13.2.jar
│   ├── grade.sh
│   ├── GradeServer.java
│   ├── Server.java
│   ├── TestListExamples.java

```
The contents of each file before fixing the bug
```grade.sh``` contents:

```
CPATH='.:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar'

rm -rf student-submission
rm -rf grading-area

mkdir grading-area

git clone $1 student-submission
echo 'Finished cloning'


# Draw a picture/take notes on the directory structure that's set up after
# getting to this point

# Then, add here code to compile and run, and do any post-processing of the
# tests

# Check that the student code has the correct file submitted. If they didn’t, detect and give helpful feedback about it. This is not done by the provided code, you should figure out where to add it
# Useful tools here are if and -e/-f. You can use the exit command to quit a bash script early. These are summarized in the week 6 Monday lecture handout and podcast

if [ ! -f student-submission/*.java ]; then
    echo 'Grade: 0/4. File not found.'
    exit 1
fi

# Check that the student code compiles. If it doesn’t, detect and give helpful feedback about it. This is not done by the provided code, you should figure out where to add it
javac -cp $CPATH student-submission/ListExamples.java TestListExamples.java -d grading-area 2> grading-area/compile.txt
if [ $? -ne 0 ]; then
    echo 'Compilation failed'
    echo ''
    cat grading-area/compile.txt
    echo ''
    echo 'Grade: 1/5. File is found but does not compile.'
    exit 1
else 
    echo 'Compilation successful.'
fi

# Run the tests and save the results to a file
java -cp "$CPATH:grading-area" org.junit.runner.JUnitCore TestListExamples > grading-area/result.txt

# Check if all tests passed.
success=$(grep -o "OK" grading-area/result.txt)
if [ $success == "OK" ]
then
    echo "Grade: 5/5. All tests passed."
    exit 0
fi


## If not all tests passed, then we need to calculate the score.

# Extract the line with the total number of tests run from the result file
total_tests_line=$(grep "run: [0-9]*" grading-area/result.txt)

# Extract the number from the grep output
total_tests=$(echo $total_tests_line | awk '{ print $3 }')

# Extract the line with the number of failed tests from the result file
failed_tests_line=$(grep "Failures: [0-9]*" grading-area/result.txt)

# Extract the number from the grep output
failed_tests=$(echo $failed_tests_line | awk '{ print $2 }')

# Calculate the score by subtracting the number of failed tests from the total
score=$((total_tests - failed_tests))

# Print the score
echo ""
echo "Grade: $score/$total_tests"
```

```GradeServer.java``` contents:

```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Stream;

class ExecHelpers {

  /**
    Takes an input stream, reads the full stream, and returns the result as a
    string.

    In Java 9 and later, new String(out.readAllBytes()) would be a better
    option, but using Java 8 for compatibility with ieng6.
  */
  static String streamToString(InputStream out) throws IOException {
    String result = "";
    while(true) {
      int c = out.read();
      if(c == -1) { break; }
      result += (char)c;
    }
    return result;
  }

  /**
    Takes a command, represented as an array of strings as it would by typed at
    the command line, runs it, and returns its combined stdout and stderr as a
    string.
  */
  static String exec(String[] cmd) throws IOException {
    Process p = new ProcessBuilder()
                    .command(Arrays.asList(cmd))
                    .redirectErrorStream(true)
                    .start();
    InputStream outputOfBash = p.getInputStream();
    return String.format("%s\n", streamToString(outputOfBash));
  }

}

class Handler implements URLHandler {
    public String handleRequest(URI url) throws IOException {
       if (url.getPath().equals("/grade")) {
           String[] parameters = url.getQuery().split("=");
           if (parameters[0].equals("repo")) {
               String[] cmd = {"bash", "grade.sh", parameters[1]};
               String result = ExecHelpers.exec(cmd);
               return result;
           }
           else {
               return "Couldn't find query parameter repo";
           }
       }
       else {
           return "Don't know how to handle that path!";
       }
    }
}

class GradeServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}

class ExecExamples {
  public static void main(String[] args) throws IOException {
    String[] cmd1 = {"ls", "lib"};
    System.out.println(ExecHelpers.exec(cmd1));

    String[] cmd2 = {"pwd"};
    System.out.println(ExecHelpers.exec(cmd2));

    String[] cmd3 = {"touch", "a-new-file.txt"};
    System.out.println(ExecHelpers.exec(cmd3));
  }
}
```

```Server.java``` contents:

```
// A simple web server using Java's built-in HttpServer

// Examples from https://dzone.com/articles/simple-http-server-in-java were useful references

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

interface URLHandler {
    String handleRequest(URI url) throws IOException;
}

class ServerHttpHandler implements HttpHandler {
    URLHandler handler;
    ServerHttpHandler(URLHandler handler) {
      this.handler = handler;
    }
    public void handle(final HttpExchange exchange) throws IOException {
        // form return body after being handled by program
        try {
            String ret = handler.handleRequest(exchange.getRequestURI());
            // form the return string and write it on the browser
            exchange.sendResponseHeaders(200, ret.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(ret.getBytes());
            os.close();
        } catch(Exception e) {
            String response = e.toString();
            exchange.sendResponseHeaders(500, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

public class Server {
    public static void start(int port, URLHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        //create request entrypoint
        server.createContext("/", new ServerHttpHandler(handler));

        //start the server
        server.start();
        System.out.println("Server Started! Visit http://localhost:" + port + " to visit.");
    }
}
```

```TestListExamples.java``` contents:

```
import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class IsMoon implements StringChecker {
  public boolean checkString(String s) {
    return s.equalsIgnoreCase("moon");
  }
}



public class TestListExamples {
  @Test(timeout = 500)
  public void testMergeRightEnd() {
    List<String> left = Arrays.asList("a", "b", "c");
    List<String> right = Arrays.asList("a", "d");
    List<String> merged = ListExamples.merge(left, right);
    List<String> expected = Arrays.asList("a", "a", "b", "c", "d");
    assertEquals(expected, merged);
  }

    @Test
    public void testFilter3() {
        StringCheck sc = new StringCheck();
        List<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.add("d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, ListExamples.filter(testList, sc));
    }

    @Test
    public void testFilter() {
      List<String> list = Arrays.asList("moon", "sun", "earth", "mars");
      StringChecker sc = new IsMoon();
      List<String> result = ListExamples.filter(list, sc);
      assertEquals(Arrays.asList("moon"), result);
    }
  
    @Test
    public void testMerge() {
      List<String> list1 = Arrays.asList("apple", "banana", "cherry");
      List<String> list2 = Arrays.asList("date", "elderberry", "fig");
      List<String> result = ListExamples.merge(list1, list2);
      assertEquals(Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "fig"), result);
    }
  
    @Test
    public void testMergeWithOverlap() {
      List<String> list1 = Arrays.asList("apple", "banana", "cherry");
      List<String> list2 = Arrays.asList("banana", "cherry", "date");
      List<String> result = ListExamples.merge(list1, list2);
      assertEquals(Arrays.asList("apple", "banana", "banana", "cherry", "cherry", "date"), result);
    }
}
```

The full command line (or lines) you ran to trigger the bug:

```bash grade.sh https://github.com/ucsd-cse15l-f22/list-methods-corrected```

A description of what to edit to fix the bug:

To fix the bug, there needs to be the class ```StringCheck``` that implements ```StringChecker``` in the ```TestListExamples.java``` so that the class StringCheck is created.

Revised ```TestListExamples.java``` contents:

```
import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class IsMoon implements StringChecker {
  public boolean checkString(String s) {
    return s.equalsIgnoreCase("moon");
  }
}

class StringCheck implements StringChecker {
  public boolean checkString(String s) {
    if (s instanceof String) {
      return true;
    }
    else {
      return false;
    }
  }
}

public class TestListExamples {
  @Test(timeout = 500)
  public void testMergeRightEnd() {
    List<String> left = Arrays.asList("a", "b", "c");
    List<String> right = Arrays.asList("a", "d");
    List<String> merged = ListExamples.merge(left, right);
    List<String> expected = Arrays.asList("a", "a", "b", "c", "d");
    assertEquals(expected, merged);
  }

    @Test
    public void testFilter3() {
        StringCheck sc = new StringCheck();
        List<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.add("d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, ListExamples.filter(testList, sc));
    }

    @Test
    public void testFilter() {
      List<String> list = Arrays.asList("moon", "sun", "earth", "mars");
      StringChecker sc = new IsMoon();
      List<String> result = ListExamples.filter(list, sc);
      assertEquals(Arrays.asList("moon"), result);
    }
  
    @Test
    public void testMerge() {
      List<String> list1 = Arrays.asList("apple", "banana", "cherry");
      List<String> list2 = Arrays.asList("date", "elderberry", "fig");
      List<String> result = ListExamples.merge(list1, list2);
      assertEquals(Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "fig"), result);
    }
  
    @Test
    public void testMergeWithOverlap() {
      List<String> list1 = Arrays.asList("apple", "banana", "cherry");
      List<String> list2 = Arrays.asList("banana", "cherry", "date");
      List<String> result = ListExamples.merge(list1, list2);
      assertEquals(Arrays.asList("apple", "banana", "banana", "cherry", "cherry", "date"), result);
    }
}
```
