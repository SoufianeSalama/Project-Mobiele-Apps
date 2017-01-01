package be.ucll.project2;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.gson.Gson;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class BusFragment extends Fragment {
    View myView;

    WebView webViewBusRealtime;
    private SharedPreferences savedValues;

    private DataHelper dh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.bus_layout,container, false);

        //savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE); ->werkt niet in een fragement

        savedValues = this.getActivity().getSharedPreferences("SavedValues",MODE_PRIVATE);
        String bussen;
        bussen = "403383";

        dh = new DataHelper(this.getActivity());

        //dh.getGebruiker();
        Campussen campus = dh.getCampusVanGebruiker();
        if (campus.equals(null)){
            bussen = "403384";
        }
        else{
            bussen = campus.getBushalte();
        }


        webViewBusRealtime =(WebView) myView.findViewById(R.id.webViewBusRealtime);
        webViewBusRealtime.getSettings().setJavaScriptEnabled(true);
        webViewBusRealtime.loadUrl("https://www.delijn.be/realtime/" + bussen + "/50");
        return myView;
    }




}
