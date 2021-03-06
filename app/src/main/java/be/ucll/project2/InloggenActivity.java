package be.ucll.project2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
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
    DataHelper dh;
    ProgressDialog dialogLoading;

    private String Wachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inloggen);

        // Werken met klasse DataHelper
        dh = new DataHelper(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        try {
            mClient = new MobileServiceClient(
                    "https://projectmobieleapps.azurewebsites.net",
                    this
            );

            mGebruiker = mClient.getTable(Gebruikers.class);
        } catch (MalformedURLException e) {
            Log.e("Malformed url", e.getMessage());
        }

        buttonInloggen = (Button) findViewById(R.id.buttonInloggen);

        editTextGebruikersnaam = (EditText) findViewById(R.id.editTextGebruikersnaam);
        editTextWachtwoord = (EditText) findViewById(R.id.editTextWachtwoord);

        buttonInloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGebruikersnaam.getText().toString().equals("") && editTextWachtwoord.getText().toString().equals("")) {
                    editTextGebruikersnaam.setError("Vul een gebruikersnaam in (r-nummer)");
                    editTextWachtwoord.setError("Vul een wachtwoord in");
                    System.out.println("gebruikersnaam en wachtwoord is leeg");

                } else if (editTextGebruikersnaam.getText().toString().equals("")) {
                    editTextGebruikersnaam.setError("Vul een gebruikersnaam in (r-nummer)");


                } else if (editTextWachtwoord.getText().toString().equals("")) {
                    editTextWachtwoord.setError("Vul een wachtwoord in");
                    System.out.println(editTextWachtwoord.getText().toString());

                } else {

                    controleerGebruiker(editTextGebruikersnaam.getText().toString(), editTextWachtwoord.getText().toString());
                    dialogLoading = new ProgressDialog(InloggenActivity.this);
                    dialogLoading.setMessage("Inloggen...");
                    dialogLoading.show();

                }
            }
        });

    }

    private boolean controleerGebruiker(String gebruikersnaam, final String wachtwoord) {
        this.Wachtwoord = wachtwoord;


        mGebruiker.where().field("gebruikersnaam").eq(val(gebruikersnaam)).top(1).execute(new TableQueryCallback<Gebruikers>() {
            @Override
            public void onCompleted(List<Gebruikers> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {

                    if (result.isEmpty()) {
                        CharSequence text = "Gebruiker niet gekend";

                        Toast toast = Toast.makeText(InloggenActivity.this, text, Toast.LENGTH_LONG);
                        toast.show();
                        editTextGebruikersnaam.setError("Gebruiker niet gekend");
                        editTextWachtwoord.setError("Gebruiker niet gekend");

                    } else {
                        for (Gebruikers ge : result) {
                            if (ge.getWachtwoord().equals(Wachtwoord)) {

                                dialogLoading.dismiss();

                                if (dh.getGebruiker() == null){
                                    // nieuwe gebruiker(eerste keer gebruik van app), hierin alle data ophalen
                                    dh.saveGebruiker(ge);
                                    dh.saveCampussenUitAzure();

                                    dialogLoading.setMessage("Al uw gegevens ophalen...");
                                    dialogLoading.show();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            /*Toast toast = Toast.makeText(InloggenActivity.this, Html.fromHtml("Welkom <font color='#DE0248' ><b>" + ge.getNaam() + "</b></font>"), Toast.LENGTH_LONG);
                                            toast.show();*/
                                            Intent intent = new Intent(InloggenActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            dialogLoading.dismiss();
                                        }
                                    }, 10000);

                                }
                                else{
                                    dh.saveGebruiker(ge);
                                    dh.saveCampussenUitAzure();
                                    Toast toast = Toast.makeText(InloggenActivity.this, Html.fromHtml("Welkom <font color='#DE0248' ><b>" + ge.getNaam() + "</b></font>"), Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent intent = new Intent(InloggenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }




                            } else {
                                dialogLoading.dismiss();

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
                        }
                    }
                } else {
                    Log.e("failed", exception.getMessage());
                    dialogLoading.dismiss();
                    AlertDialog dialog = new AlertDialog.Builder(InloggenActivity.this)
                            .setTitle("Fout")
                            .setMessage("Bent u verbonden met het internet?")
                            .show();
                }
            }
        });
        return false;
    }

}