package be.ucll.project2;

/**
 * Created by Soufiane on 13/01/2017.
 */

public class Taak {

    private int taakId;
    private String taakTitel;
    private String taakBeschrijving;
    private String taakDatum;


    public int getTaakId(){
        return taakId;
    }

    public void setTaakId(int taakId){
        this.taakId = taakId;
    }

    public String getTaakTitel(){
        return taakTitel;
    }

    public void setTaakTitel(String titel){
        this.taakTitel = titel;
    }

    public String getTaakBeschrijving(){
        return taakBeschrijving;
    }

    public void setTaakBeschrijving(String beschrijving){
        this.taakBeschrijving = beschrijving;
    }

    public String getTaakDatum(){
        return taakDatum;
    }

    public void setTaakDatum(String datum){
        this.taakDatum = datum;
    }

}
