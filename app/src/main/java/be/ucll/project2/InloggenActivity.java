package be.ucll.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class InloggenActivity extends AppCompatActivity {
    Button buttonInloggen;

    EditText editTextGebruikersnaam;
    EditText editTextWachtwoord;

    private SharedPreferences savedValues;

    Gebruikers gebruiker;
    private MobileServiceClient mClient;
    private MobileServiceTable<Gebruikers> mGebruiker;
    private MobileServiceTable<Campussen> mCampussen;


    private String Wachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inloggen);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        try{
            mClient = new MobileServiceClient(
                    "https://projectmobieleapps.azurewebsites.net",
                    this
            );

            mGebruiker = mClient.getTable(Gebruikers.class);
            mCampussen = mClient.getTable(Campussen.class);
        }
        catch(MalformedURLException e){
            Log.e("Malformed url" ,e.getMessage());
        }

        buttonInloggen = (Button) findViewById(R.id.buttonInloggen);

        editTextGebruikersnaam = (EditText) findViewById(R.id.editTextGebruikersnaam);
        editTextWachtwoord = (EditText) findViewById(R.id.editTextWachtwoord);

        buttonInloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGebruikersnaam.getText().toString().equals("") &&  editTextWachtwoord.getText().toString().equals("")){
                    editTextGebruikersnaam.setError("Vul een gebruikersnaam in (r-nummer)");
                    editTextWachtwoord.setError("Vul een wachtwoord in");
                    System.out.println("gebruikersnaam en wachtwoord is leeg");

                }else if (editTextGebruikersnaam.getText().toString().equals("")){
                    editTextGebruikersnaam.setError("Vul een gebruikersnaam in (r-nummer)");


                }
                else if (editTextWachtwoord.getText().toString().equals("")){
                    editTextWachtwoord.setError("Vul een wachtwoord in");
                    System.out.println(editTextWachtwoord.getText().toString());

                }
                else{
                    controleerGebruiker(editTextGebruikersnaam.getText().toString(), editTextWachtwoord.getText().toString());

                }
            }
        });

    }

    private boolean controleerGebruiker(String gebruikersnaam, final String wachtwoord){
        this.Wachtwoord = wachtwoord;


        mGebruiker.where().field("gebruikersnaam").eq(val(gebruikersnaam)).top(1).execute(new TableQueryCallback<Gebruikers>() {
            @Override
            public void onCompleted(List<Gebruikers> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null){
                    System.out.println("succeeded");
                    System.out.println(result);
                    if (result.isEmpty()){
                        Context context = getApplicationContext();
                        CharSequence text = "Gebruiker niet gekend";

                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                        editTextGebruikersnaam.setError("Gebruiker niet gekend");
                        editTextWachtwoord.setError("Gebruiker niet gekend");
                    }
                    else{
                        for (Gebruikers ge : result){
                            if (ge.getWachtwoord().equals(Wachtwoord)){

                                saveGegevens(ge);


                                Context context = getApplicationContext();
                                Toast toast = Toast.makeText(context, Html.fromHtml("Welkom <font color='#DE0248' ><b>" + ge.getNaam() + "</b></font>"), Toast.LENGTH_LONG);
                                toast.show();
                                Intent intent = new Intent(InloggenActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                            else{
                                AlertDialog alertDialog = new AlertDialog.Builder(InloggenActivity.this).create();
                                alertDialog.setTitle("Fout");
                                alertDialog.setMessage("Onbekende combinatie van gebruikersnaam en wachtwoord.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();


                                editTextGebruikersnaam.setError("Onbekende combinatie van gebruikersnaam en wachtwoord");
                                editTextWachtwoord.setError("Onbekende combinatie van gebruikersnaam en wachtwoord");
                            }
                            System.out.println(ge.getNaam());
                            System.out.println(Wachtwoord);
                            System.out.println(ge.getWachtwoord());
                        }
                    }
                }
                else{
                    Log.e("failed", exception.getMessage());

                }
            }
        });

        return false;

    }

    public void saveGegevens(Gebruikers gebruiker){
        // Gegevens van de ingelogde gebruiker in de SharedPreferencs zetten onder de key Gebruiker
        Editor prefsEditor = savedValues.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gebruiker);
        prefsEditor.putString("Gebruiker", json);
        prefsEditor.commit();

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
                        Editor e = savedValues.edit();

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
}
