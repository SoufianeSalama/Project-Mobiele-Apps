package be.ucll.project2;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TakenFragment extends Fragment {
    View myView;
    testDB db;
    ArrayAdapter<Taak> mAdapter;
    ListView takenListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.taken_layout, container, false);

        takenListView = (ListView) myView.findViewById(R.id.takenLijst);

        getTakenMetSimpleAdapter();

        return myView;
    }

    public void getTakenMetSimpleAdapter() {
        Context context = getActivity().getApplicationContext();

        db = new testDB(context);
        ArrayList<Taak> takenLijst = db.getTasks();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();

        for (Taak taak : takenLijst) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("titel", taak.getTaakTitel());
            map.put("beschrijving", taak.getTaakBeschrijving());
            map.put("datum", taak.getTaakDatum());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.taak_rij;
        String[] from = {"titel", "beschrijving", "datum"};
        int[] to = {R.id.taaknaam, R.id.taakbeschrijving, R.id.taakdatum};

        // create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        takenListView.setAdapter(adapter);
    }
}

