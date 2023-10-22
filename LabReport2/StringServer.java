import java.io.IOException;
import java.net.URI;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    private StringBuilder message = new StringBuilder();
    private int messageCount = 0;


    public String handleRequest(URI url) {
        String path = url.getPath(); // e.g. "/add-message"
        String query = url.getQuery(); // e.g. "message=hello"
        if (path.equals("/add-message")) {
            if (query == null) {
                return "No query string provided";
            }
            String[] parts = query.split("=");
            if (parts.length != 2) {
                return "Invalid query string";
            }
            String s = parts[1];
            messageCount++;
            message.append(messageCount + ". " + s + "\n");
            return message.toString();
        } else {
            return "404 Not Found";
        }
    }
}

class StringServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
