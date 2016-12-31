package be.ucll.project2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    View myView;

    private MapView mapView;
    private GoogleMap googleMap;

    private MobileServiceClient mClient;
    private MobileServiceTable<Campussen> mCampussen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.map_layout,container, false);

        mapView = (MapView) myView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        try{
            mClient = new MobileServiceClient(
                    "https://projectmobieleapps.azurewebsites.net",
                    this.getActivity()
            );

            mCampussen = mClient.getTable(Campussen.class);
            System.out.println("try gelukt");

        }
        catch(MalformedURLException e) {
            Log.e("Malformed url", e.getMessage());
            System.out.println("try mislukt");
        }
        return myView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney and move the camera
        LatLng thuis = new LatLng(50.899, 5.355);
        System.out.println(thuis);
        googleMap.addMarker(new MarkerOptions().position(thuis).title("Thuis"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thuis, 10));

        getLocaties();
    }

    private void getLocaties(){
        System.out.println("in de methode");
        mCampussen.select().execute(new TableQueryCallback<Campussen>() {
            @Override
            public void onCompleted(List<Campussen> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null){
                    System.out.println("succeeded");
                    for (Campussen campus : result){

                        System.out.println(campus.getNaam());
                        System.out.println(campus.getOpleidingen());
                        LatLng coor = new LatLng(Double.parseDouble(campus.getCoordinaatlat()), Double.parseDouble(campus.getCoordinaatlng()));
                        googleMap.addMarker(new MarkerOptions().position(coor).title(campus.getNaam()).snippet(campus.getAdres()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_logo_ucllb)));
                        System.out.println(coor);
                    }
                }

                else{
                    Log.e("failed", exception.getMessage());

                }
            }
        });


    }
}
