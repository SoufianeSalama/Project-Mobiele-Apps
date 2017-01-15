package be.ucll.project2;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    View myView;

    private MapView mapView;
    private GoogleMap googleMap;

    private SharedPreferences savedValues;

    private List<Campussen> LijstCampussen;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.map_layout,container, false);

        mapView = (MapView) myView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        savedValues = this.getActivity().getSharedPreferences("SavedValues",MODE_PRIVATE);

        return myView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney and move the camera
        LatLng thuis = new LatLng(50.899, 5.355);
        System.out.println(thuis);
        //googleMap.addMarker(new MarkerOptions().position(thuis).title("Thuis"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thuis, 10));

        getCampussen(); // -> vanuit de sharedPreferences

    }


    private void getCampussen(){

        DataHelper dh = new DataHelper(this.getActivity());
        LijstCampussen = dh.getCampussenStorage();

        for (Campussen campus : LijstCampussen){
            LatLng coor = new LatLng(
                    Double.parseDouble(campus.getCoordinaatlat()),
                    Double.parseDouble(campus.getCoordinaatlng()));

            googleMap.addMarker(new MarkerOptions()
                    .position(coor)
                    .title(campus.getNaam())
                    .snippet(campus.getAdres())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_logo_ucllb)));


        }
    }
}
