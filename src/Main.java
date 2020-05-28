import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Feedreader feedreader = new Feedreader(getURL(), true);
        feedreader.printRssContent();

    }

    /**
     * Get URL via user input
     * @return the URL as a String
     */
    private static String getURL() {
//        Scanner userInput = new Scanner(System.in);
//        System.out.println("Enter URL:");
//        return userInput.next();
        return "www.spiegel.de";
    }
}
