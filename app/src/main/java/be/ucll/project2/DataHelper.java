package be.ucll.project2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by Soufiane on 31/12/2016.
 */

public class DataHelper {

    //Proberen om allee in deze klasse de Azure DB te benaderen

    private MobileServiceClient mClient;
    private MobileServiceTable<Gebruikers> mGebruiker;
    private MobileServiceTable<Campussen> mCampussen;

    private SharedPreferences savedValues;

    private String gebruikersnaam;
    private String wachtwoord;

    private List<Campussen> lijstCampussen;

    private String resultaat = "Error";


    public DataHelper(Activity activityNaam){
        savedValues = activityNaam.getSharedPreferences("SavedValues",MODE_PRIVATE);

        try{
            mClient = new MobileServiceClient(
                    "https://projectmobieleapps.azurewebsites.net",
                    activityNaam
            );

            mGebruiker = mClient.getTable(Gebruikers.class);
            mCampussen = mClient.getTable(Campussen.class);
        }
        catch(MalformedURLException e){
            Log.e("Malformed url" ,e.getMessage());
        }
    }

    public void setGebruikersnaamWachtwoord(String gebruikersnaam, String wachtwoord){
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord     = wachtwoord;
    }
    public String getResultaat(){
        return this.resultaat;
    }

    public void controleerGebruiker(){

        mGebruiker.where().field("gebruikersnaam").eq(val(this.gebruikersnaam)).top(1).execute(new TableQueryCallback<Gebruikers>() {
            @Override
            public void onCompleted(List<Gebruikers> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null){

                    if (result.isEmpty()){
                        resultaat = "Unknown";
                    }
                    else{
                        for (Gebruikers ge : result){
                            if (ge.getWachtwoord().equals(wachtwoord)){
                                resultaat = "True";
                            }
                            else{
                                resultaat = "NotEqual";
                            }
                        }
                    }
                }
                else{
                    Log.e("failed", exception.getMessage());
                }
            }
        });
    }

   public Campussen getCampusVanGebruiker(){
        // Nodig om de bushaltes te krijgen van de campus waar de gebruiker les volgt, deze zitten in shared onder key Campussen
        // Maar eerst het campusId van de Campus opzoeken in shared onder de key gebruiker
        String CampusId;
        //return "403383";

        Gebruikers gebruiker = getGebruiker();
        CampusId = gebruiker.getCampusId();
        System.out.println(CampusId);

        gebruiker.getCampusId().toString().equals(CampusId);
        lijstCampussen = getCampussenStorage();
        for(Campussen campus : lijstCampussen) {
            System.out.println(campus.getNaam());
            System.out.println(campus.getId());


            if(campus.getId().equals(CampusId)) {
                System.out.println("de gelukkige is " + campus.getNaam());

                return campus;
            }
        }
        return null;

    }

    public void saveGebruiker(Gebruikers gebruiker){
        SharedPreferences.Editor prefsEditor = savedValues.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gebruiker);
        prefsEditor.putString("Gebruiker", json);
        prefsEditor.commit();
    }

    public Gebruikers getGebruiker(){
        Gson gson = new Gson();
        String json = savedValues.getString("Gebruiker", "");
        Gebruikers gebruiker = gson.fromJson(json, Gebruikers.class);
        System.out.println(gebruiker.getNaam());
        System.out.println(gebruiker.getGebruikersnaam());
        System.out.println(gebruiker.getGroep());
        System.out.println(gebruiker.getKlas());

        return gebruiker;
    }

    public void saveCampussenUitAzure(){
        //Controleren of er een key Campussen bestaat
        Gson gson2 = new Gson();
        String json2 = savedValues.getString("Campussen", "");
        if (json2.equals("")){
            //er bestaat niks onder de key Campussen
            // alle campussen van Azure afhalen
            mCampussen.select().execute(new TableQueryCallback<Campussen>() {
                @Override
                public void onCompleted(List<Campussen> lijstCampussen, int count, Exception exception, ServiceFilterResponse response) {
                    if (exception == null){
                        SharedPreferences.Editor e = savedValues.edit();

                        // convert java object to JSON format,
                        // and returned as JSON formatted string
                        String jsonLijstCampussen = new Gson().toJson(lijstCampussen);
                        e.putString("Campussen", jsonLijstCampussen);
                        e.commit();
                        System.out.println("Campussen in sharedPreferences");
                    }
                    else{
                        Log.e("failed", exception.getMessage());
                    }
                }
            });
        }
        else{
            //alle campussen staan onder de key Campussen
            System.out.println("Campussen staan al in sharedPreferences");

        }
    }

    public List<Campussen> getCampussenStorage(){
        Gson gson = new Gson();
        String json = savedValues.getString("Campussen", "");
        Type type = new TypeToken< List < Campussen >>() {}.getType();
        lijstCampussen = gson.fromJson(json, type);
        return lijstCampussen;
    }
}
