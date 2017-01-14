package be.ucll.project2;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TakenFragment extends Fragment implements OnItemLongClickListener {
    View myView;
    testDB db;
    ListView takenListView;
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.taken_layout, container, false);

        CharSequence text = "Hou een taak ingedrukt om deze te verwijderen";

        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
        toast.show();

        takenListView = (ListView) myView.findViewById(R.id.takenLijst);

        getTakenMetSimpleAdapter();
        return myView;
    }

    public void getTakenMetSimpleAdapter() {
        db = new testDB(TakenFragment.this.getActivity());

        ArrayList<Taak> takenLijst = db.getTasks();

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for (Taak taak : takenLijst) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", Integer.toString(taak.getTaakId()));
            map.put("titel", taak.getTaakTitel());
            map.put("beschrijving", taak.getTaakBeschrijving());
            map.put("datum", taak.getTaakDatum());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.taak_rij;
        String[] from = {"id", "titel", "beschrijving", "datum"};
        int[] to = {R.id.taakid, R.id.taaknaam, R.id.taakbeschrijving, R.id.taakdatum};

        // create and set the adapter
        adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        takenListView.setAdapter(adapter);
        takenListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.taakid);
        int taakId = Integer.parseInt(textView.getText().toString());

        db.deleteTask(taakId);
        getTakenMetSimpleAdapter();
        return false;
    }


}

