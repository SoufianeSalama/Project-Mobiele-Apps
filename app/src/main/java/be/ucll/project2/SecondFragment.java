package be.ucll.project2;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Meneer Doos on 20/11/2016.
 */

public class SecondFragment extends Fragment implements AdapterView.OnItemClickListener{
    View myView;
    private RSSFeed feed;
    private FileIO io;
    private TextView titleTextView;
    private ListView itemsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.activity_items,container, false);
        io= new FileIO(getActivity().getApplication());
        titleTextView= (TextView)myView.findViewById(R.id.titleTextView);
        itemsListView=(ListView)myView.findViewById(R.id.itemsListView);
        itemsListView.setOnItemClickListener(this);

        new DownloadFeed().execute();
        return myView;
    }

    class DownloadFeed extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            io.downloadFile();
            return null;
        }

        protected void onPostExecute(Void result){
            Log.d("new reader","feed downloaded");
            new ReadFeed().execute();
        }

    }

    class ReadFeed extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            feed=io.readFile();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            Log.d("News Reader","Feed Read");
            SecondFragment.this.updateDisplay();
        }
    }

    public void updateDisplay()
    {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // set the title for the feed
        titleTextView.setText(feed.getTitle());

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (RSSItem item : items) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", item.getPubDateFormatted());
            map.put("title", item.getTitle());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "title"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(getActivity(), data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get the item at the specified position
        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra("pubdate", item.getPubDate());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());

        startActivity(intent);
    }
}
