package be.ucll.project2;

/**
 * Created by Soufiane on 31/12/2016.
 */

public class Campussen {

    public Campussen(){   }

    // Item id
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    // Item naam
    @com.google.gson.annotations.SerializedName("naam")
    private String mNaam;

    // Item naam
    @com.google.gson.annotations.SerializedName("adres")
    private String mAdres;

    // Item naam
    @com.google.gson.annotations.SerializedName("coordinaatlng")
    private String mCoordinaatlng;

    // Item naam
    @com.google.gson.annotations.SerializedName("coordinaatlat")
    private String mCoordinaatlat;

    // Item naam
    @com.google.gson.annotations.SerializedName("bushalte")
    private String mBushalte;

    // Item naam
    @com.google.gson.annotations.SerializedName("opleidingen")
    private String mOpleidingen;



    // get id
    public String getId(){
        return mId;
    }

    // get Naam
    public String getNaam(){    return mNaam;   }

    // get Naam
    public String getCoordinaatlng(){
        return mCoordinaatlng;
    }

    // get Naam
    public String getCoordinaatlat(){
        return mCoordinaatlat;
    }

    // get Naam
    public String getBushalte(){
        return mBushalte;
    }

    // get Naam
    public String getOpleidingen(){
        return mOpleidingen;
    }

    // get Naam
    public String getAdres(){
        return mAdres;
    }


    public final void setId(String text) {        mId = text;    }

    public final void setNaam(String text) {        mNaam = text;    }

    public final void setCoordinaatlng(String text) {        mCoordinaatlng = text;    }

    public final void setCoordinaatlat(String text) {        mCoordinaatlat = text;    }

    public final void setBushalte(String text) {        mBushalte = text;    }

    public final void setOpleidingen(String text) {        mOpleidingen = text;    }

    public final void setAdres(String text) {        mAdres = text;    }




}
