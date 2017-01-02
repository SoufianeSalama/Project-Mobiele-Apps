package be.ucll.project2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InstellingenFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private SharedPreferences pref;
    private boolean geofenceEnabled;
    private String nieuwsUrl;

    private GeofenceHelper gh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        gh = new GeofenceHelper(this.getActivity());

    }


    @Override
    public void onResume() {
        super.onResume();
        geofenceEnabled = pref.getBoolean("geofenceEnabled", true);
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onResume();

    }

    // Dit event wordt telkens afgevuurd als de gebruiker in de instellingen pagina op de switch klikt
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("geofence_enabled")){
            geofenceEnabled = pref.getBoolean(key, true);
            if (geofenceEnabled){
                // Geofences toevoegen
                gh.geofencesToevoegen();
            }
            else{
                // Geofences verwijderen
                gh.geofencesVerwijderen();
            }
            System.out.println(geofenceEnabled);
        }

        if (key.equals("nieuws_url")){
            nieuwsUrl = pref.getString(key, "https://www.ucll.be/rss.xml");

            System.out.println(nieuwsUrl);
        }
    }
}
