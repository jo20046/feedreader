import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Kommentare & Refactoring printRssContent()

public class Feedreader {

    private final String urlString;
    private String htmlContent;
    private String rssContent;
    private String rssFeedURL;
    private final boolean useSecure;
    private boolean connectionOK;

    Feedreader(String urlString, boolean useSecure) {
        this.urlString = urlString;
        this.useSecure = useSecure;

    }

    void printRssContent() {
        getHttpContent(urlString, true);
        if (connectionOK) {
            getRssURL(htmlContent);
            System.out.println(rssFeedURL);
            getHttpContent(rssFeedURL, false);
            Parser parser = new Parser();
            ArrayList<Article> articles = parser.getArticles();
            for (Article article : articles) {
                System.out.println("Titel: " + article.getTitle());
                System.out.println("Link: " + article.getLink());
                System.out.println();
            }
        } else {
            System.out.println("Couldn't connect to " + urlString);
        }
    }

    /**
     * Get the HTML representation of a specified website by connecting via HTTP (or HTTPS).
     *
     * @param urlInput the URL to connect to
     * @param isHTML true for HTML, false for RSS
     */
    private void getHttpContent(String urlInput, boolean isHTML) {

        try {
            HttpURLConnection huc = buildHttpConnection(urlInput);
            StringBuilder content = new StringBuilder();

            if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
                connectionOK = false;
                System.out.println(huc.getResponseMessage());
            } else {
                connectionOK = true;
                System.out.println(huc.getResponseMessage());
                InputStream is = huc.getInputStream();
                Scanner in = new Scanner(is);
                for (String line; in.hasNextLine(); ) {
                    line = in.nextLine();
                    content.append(line).append('\n');
                }
                String filename = isHTML ? "html_page.html" : "rss_feed.xml";
                PrintWriter out = new PrintWriter(filename);
                out.println(content.toString());
                out.close();
            }
            huc.disconnect();
            if (isHTML)
                htmlContent = content.toString();
            else
                rssContent = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Build up a HTTP(S) connection to the specified URL with the GET command. If necessary, adds "http://" or "https://" the URL.
     *
     * @param urlInput the URL to connect to
     * @return prepared HttpURLConnection object
     */
    private HttpURLConnection buildHttpConnection(String urlInput) throws IOException {

        if (useSecure && !urlInput.startsWith("https://")) urlInput = "https://" + urlInput;
        if (!useSecure && !urlInput.startsWith("http://")) urlInput = "http://" + urlInput;

        URL url = new URL(urlInput);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");

        return huc;
    }

    /**
     * Find the RSS feed url in a HTML representation of a website
     * @param html the HTML content
     */
    private void getRssURL(String html) {
        Pattern pattern = Pattern.compile("https://.*?/index\\.rss");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find())
            rssFeedURL = matcher.group();
    }
}
