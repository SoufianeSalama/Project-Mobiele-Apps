package be.ucll.project2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Meneer Doos on 20/11/2016.
 */

public class BusFragment extends Fragment {
    View myView;

    WebView webViewBusRealtime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.bus_layout,container, false);

        webViewBusRealtime =(WebView) myView.findViewById(R.id.webViewBusRealtime);
        webViewBusRealtime.getSettings().setJavaScriptEnabled(true);
        webViewBusRealtime.loadUrl("https://www.delijn.be/realtime/410147/50");

        return myView;
    }
}
