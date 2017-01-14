package be.ucll.project2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private testDB dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new ProfielFragment())
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Nieuwe taak toevoegen")
                        .setView(R.layout.nieuwetaakdialog)
                        .setPositiveButton("Toevoegen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog dl = (Dialog) dialog;

                                EditText taakTitel = (EditText) dl.findViewById(R.id.editTextTitel);
                                EditText taakBeschrijving =(EditText) dl.findViewById(R.id.editTextBeschrijving);

                                if (taakTitel.getText().toString().equals("")){
                                    taakTitel.setError("Taak moet een titel hebben!");
                                }
                                else{
                                    Taak taak = new Taak();
                                    taak.setTaakTitel(taakTitel.getText().toString());
                                    taak.setTaakBeschrijving(taakBeschrijving.getText().toString());

                                    Calendar c = Calendar.getInstance();
                                    System.out.println("Current time => " + c.getTime());

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c.getTime());

                                    taak.setTaakDatum(formattedDate.toString());

                                    dbhelper = new testDB(context);
                                    dbhelper.insertTask(taak);
                                    /*// de pagina refreshen
                                    TakenFragment tf = new TakenFragment();
                                    tf.getTakenMetSimpleAdapter();*/
                                }
                            }
                        })
                        .setNegativeButton("Annuleer", null)
                        .show();

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();


        if (id == R.id.nav_campussen_layout) {
           fragmentManager.beginTransaction()
            .replace(R.id.content_frame, new MapFragment())
            .commit();// Handle the camera action
        }

        else if (id == R.id.nav_nieuws_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SecondFragment())
                    .commit();
        }

        else if (id == R.id.nav_bussen_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BusFragment())
                    .commit();
        }

        else if (id == R.id.nav_taken_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new TakenFragment())
                    .commit();
        }

        else if (id == R.id.nav_profiel_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ProfielFragment())
                    .commit();

        }

        else if (id == R.id.nav_instellingen_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new InstellingenFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
