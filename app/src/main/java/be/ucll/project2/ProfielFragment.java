package be.ucll.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import static android.content.Context.MODE_PRIVATE;


public class ProfielFragment extends Fragment implements OnClickListener {

    View myView;
    TextView textViewNaam;
    TextView textViewRNummer;
    TextView textViewKlas;
    TextView textViewGroep;
    TextView textViewCampus;
    CardView CardViewUcll;
    CardView CardViewIntranet;
    CardView CardViewToledo;
    CardView CardViewLessenrooster;

    private SharedPreferences savedValues;

    private DataHelper dh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.profiel_layout,container, false);

        textViewNaam    = (TextView) myView.findViewById(R.id.textViewNaam);
        textViewKlas    = (TextView) myView.findViewById(R.id.textViewKlas);
        textViewGroep   = (TextView) myView.findViewById(R.id.textViewGroep);
        textViewCampus  = (TextView) myView.findViewById(R.id.textViewCampus);
        textViewRNummer = (TextView) myView.findViewById(R.id.textViewRNummer);

        CardViewUcll    = (CardView) myView.findViewById(R.id.CardViewUcll);
        CardViewUcll.setOnClickListener(this);

        CardViewIntranet = (CardView) myView.findViewById(R.id.CardViewIntranet);
        CardViewIntranet.setOnClickListener(this);

        CardViewToledo = (CardView) myView.findViewById(R.id.CardViewToledo);
        CardViewToledo.setOnClickListener(this);

        CardViewLessenrooster = (CardView) myView.findViewById(R.id.CardViewLessenrooster);
        CardViewLessenrooster.setOnClickListener(this);

        dh = new DataHelper(this.getActivity());

        Gebruikers gebruiker = dh.getGebruiker();
        Campussen campus = dh.getCampusVanGebruiker();

        textViewNaam.setText(gebruiker.getNaam().toString());
        textViewRNummer.setText(gebruiker.getGebruikersnaam().toString());
        textViewKlas.setText(gebruiker.getKlas().toString());
        textViewGroep.setText(gebruiker.getGroep().toString());

        textViewCampus.setText(campus.getNaam());


        return myView;
    }


    @Override
    public void onClick(View v) {
        System.out.println("In de onCLick");
        String url;
        Intent i;
        switch (v.getId()){
            case R.id.CardViewUcll:
                url = "http://www.ucll.be";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.CardViewIntranet:
                url = "https://intranet.ucll.be";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.CardViewToledo:
                url = "http://toledo.kuleuven.be/portal";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.CardViewLessenrooster:
                url = "https://rooster.ucll.be/";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

        }
    }
}
