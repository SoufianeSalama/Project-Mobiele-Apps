package be.ucll.project2;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Meneer Doos on 2/12/2016.
 */

public class RSSItem {
    private String title = null;
    private String description = null;
    private String link = null;
    private String pubDate = null;

    //http://developer.android.com/reference/java/text/SimpleDateFormat.html
    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public void setTitle(String title)     {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description)     {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getPubDateFormatted() {
        try {
            String pubDateFormatted;
            if (pubDate != null) {
                Date date = dateInFormat.parse(pubDate.trim());
                pubDateFormatted = dateOutFormat.format(date);
            }
            else{
               pubDateFormatted="datum";

            }
            return pubDateFormatted;

        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}