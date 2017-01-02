package be.ucll.project2;

/**
 * Created by Meneer Doos on 2/12/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.Context.MODE_PRIVATE;

public class FileIO {
    private String URL_STRING ="";// "http://www.hln.be/rss.xml";
    private final String FILENAME = "news_feed.xml";
    private Context context = null;

    private SharedPreferences pref;

    public FileIO (Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.URL_STRING = pref.getString("nieuws_url", "https://www.ucll.be/rss.xml");

    }

    public void downloadFile() {
        try{
            // get the URL
            //URL url = new URL("http://www.hln.be/rss.xml");
            URL url = new URL(this.URL_STRING);

            // get the input stream
            InputStream in = url.openStream();

            // get the output stream
            FileOutputStream out =
                    context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            Log.e("News reader", e.toString());
        }
    }

    public RSSFeed readFile() {
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            // read the file from internal storage
            FileInputStream in = context.openFileInput(FILENAME);

            // parse the data
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            // set the feed in the activity
            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        }
        catch (Exception e) {
            Log.e("News reader", e.toString());
            return null;
        }
    }

}
