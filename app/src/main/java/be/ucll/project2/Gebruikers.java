package be.ucll.project2;

/**
 * Created by Soufiane on 25/12/2016.
 */

public class Gebruikers {

    public Gebruikers(){

    }

    // Item id
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    // Item naam
    @com.google.gson.annotations.SerializedName("gebruikersnaam")
    private String mGebruikersnaam;

    // Item naam
    @com.google.gson.annotations.SerializedName("naam")
    private String mNaam;

    // Item naam
    @com.google.gson.annotations.SerializedName("wachtwoord")
    private String mWachtwoord;

    // Item naam
    @com.google.gson.annotations.SerializedName("campusId")
    private String mCampusId;

    // Item naam
    @com.google.gson.annotations.SerializedName("klas")
    private String mKlas;

    // Item naam
    @com.google.gson.annotations.SerializedName("groep")
    private String mGroep;



    // get id
    public String getId(){
        return mId;
    }

    // get Naam
    public String getNaam(){    return mNaam;   }

    // get Naam
    public String getGebruikersnaam(){
        return mGebruikersnaam;
    }

    // get Naam
    public String getWachtwoord(){
        return mWachtwoord;
    }

    // get Naam
    public String getCampusId(){
        return mCampusId;
    }

    // get Naam
    public String getKlas(){
        return mKlas;
    }

    // get Naam
    public String getGroep(){
        return mGroep;
    }





    /**
     * Sets the id
     *
     * @param text
     *            text to set
     */
    public final void setId(String text) {        mId = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setNaam(String text) {        mNaam = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setGebruikersnaam(String text) {        mGebruikersnaam = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setWachtwoord(String text) {        mWachtwoord = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setCampusId(String text) {        mCampusId = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setKlas(String text) {        mKlas = text;    }

    /**
     * Sets the naam
     *
     * @param text
     *            text to set
     */
    public final void setGroep(String text) {        mGroep = text;    }




}
