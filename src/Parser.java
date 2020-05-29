import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Parser extends DefaultHandler {

    private ArrayList<Article> articles;
    private Article article;
    private String data;
    private boolean hasTitle = false;
    private boolean hasLink = false;
    private boolean readAllowed = false;

    Parser() {
        try {
            PrintWriter writer = new PrintWriter("parser_output.txt");
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("rss_feed.xml"), this);
            writer.close();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void startDocument() {
        articles = new ArrayList<>();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        if (qName.equalsIgnoreCase("Item")) {
            readAllowed = true;
            article = new Article();
            data = "";
        } else if (qName.equalsIgnoreCase("title")) {
            hasTitle = true;
        } else if (qName.equalsIgnoreCase("Link")) {
            hasLink = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        if (readAllowed) {
            if (hasTitle) {
                article.setTitle(data);
                hasTitle = false;
            } else if (hasLink) {
                article.setLink(data);
                hasLink = false;
            }

            if (qName.equalsIgnoreCase("Item")) {
                articles.add(article);
                readAllowed = false;
            }
        }
    }

    public void characters(char[] ch, int s, int e) {
        if (readAllowed)
            data = new String(ch, s, e);
    }
}
