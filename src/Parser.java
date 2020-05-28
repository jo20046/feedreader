import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class Parser extends DefaultHandler {

    Parser() {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("rss_feed.xml"), this);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void startDocument() throws SAXException {
        System.out.println("Es geht los!");
    }

    public void startElement (String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        System.out.println("NS:" + namespaceURI + "| localName:" + localName + "|qName:" + qName);
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.println("Attribut:" + atts.getQName(i) + "=" + atts.getValue(i));
        }
    }

    public void characters (char[] ch, int s, int e) {
        String str = new String(ch);
        System.out.println(">>" + str.substring(s, s + e) + "<<");
    }

    public void endDocument() throws SAXException {
        System.out.println("Ende!");
    }
}
