package be.ucll.project2;
import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Created by Meneer Doos on 2/12/2016.
 */

public class RSSFeedHandler extends DefaultHandler {
    private RSSFeed feed;
    private RSSItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedPubDateHasBeenRead = false;

    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;

    private String des ="";
    private String tekst ="";


    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            item = new RSSItem();
            return;
        }
        else if (qName.equals("title")) {
            isTitle = true;
            return;
        }
        else if (qName.equals("description")) {
            isDescription = true;
            return;
        }
        else if (qName.equals("link")) {
            isLink = true;
            return;
        }
        else if (qName.equals("pubDate")) {
            isPubDate = true;
            return;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            feed.addItem(item);
            return;
        }
        if (qName.equals("description")) {
            isDescription = false;
            tekst=Html.fromHtml(des).toString();
            item.setDescription(tekst);
            des="";
            return;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String s = new String(ch, start, length);
        if (isTitle) {
            if (feedTitleHasBeenRead == false) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }
            else {
                item.setTitle(s);
            }
            isTitle = false;
        }
        else if (isLink) {
            item.setLink(s);
            isLink = false;
        }
        else if (isDescription) {


//            for (Character c : ch
//                 ) {
//
//                s += c.toString() ;
//
//            }

               des = des + s;
        }
        else if (isPubDate) {
            if (feedPubDateHasBeenRead == false) {
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;
            }
            else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }
    }
}
