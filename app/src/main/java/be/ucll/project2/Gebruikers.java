package be.ucll.project2;

/**
 * Created by Soufiane on 25/12/2016.
 */

public class Gebruikers {

    public Gebruikers(){
    }

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("gebruikersnaam")
    private String mGebruikersnaam;

    @com.google.gson.annotations.SerializedName("naam")
    private String mNaam;

    @com.google.gson.annotations.SerializedName("wachtwoord")
    private String mWachtwoord;

    @com.google.gson.annotations.SerializedName("campusId")
    private String mCampusId;

    @com.google.gson.annotations.SerializedName("klas")
    private String mKlas;

    @com.google.gson.annotations.SerializedName("groep")
    private String mGroep;



    public String getId(){
        return mId;
    }

    public String getNaam(){    return mNaam;   }

    public String getGebruikersnaam(){
        return mGebruikersnaam;
    }

    public String getWachtwoord(){
        return mWachtwoord;
    }

    public String getCampusId(){
        return mCampusId;
    }

    public String getKlas(){
        return mKlas;
    }

    public String getGroep(){
        return mGroep;
    }


    public final void setId(String text) {        mId = text;    }

    public final void setNaam(String text) {        mNaam = text;    }

    public final void setGebruikersnaam(String text) {        mGebruikersnaam = text;    }

    public final void setWachtwoord(String text) {        mWachtwoord = text;    }

    public final void setCampusId(String text) {        mCampusId = text;    }

    public final void setKlas(String text) {        mKlas = text;    }

    public final void setGroep(String text) {        mGroep = text;    }




}
