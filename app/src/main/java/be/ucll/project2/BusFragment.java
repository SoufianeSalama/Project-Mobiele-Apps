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

/**
 * Created by Meneer Doos on 20/11/2016.
 */

public class BusFragment extends Fragment {
    View myView;

    WebView webViewBusRealtime;
    private SharedPreferences savedValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.bus_layout,container, false);

        //savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE); ->werkt niet in een fragement

        savedValues = this.getActivity().getSharedPreferences("SavedValues",MODE_PRIVATE);


        webViewBusRealtime =(WebView) myView.findViewById(R.id.webViewBusRealtime);
        webViewBusRealtime.getSettings().setJavaScriptEnabled(true);
        webViewBusRealtime.loadUrl("https://www.delijn.be/realtime/410147/50");
        getGebruikerData();
        return myView;
    }

    public void getGebruikerData(){
        Gson gson = new Gson();
        String json = savedValues.getString("Gebruiker", "");
        Gebruikers obj = gson.fromJson(json, Gebruikers.class);
        System.out.println(obj.getNaam());
        System.out.println(obj.getGebruikersnaam());
        System.out.println(obj.getGroep());
        System.out.println(obj.getKlas());
    }



}
