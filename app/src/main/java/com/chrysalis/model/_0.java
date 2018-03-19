
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class _0 extends RealmObject {

    @SerializedName("Men's")
    @Expose
    private MenS menS;
    @SerializedName("Women's")
    @Expose
    private WomenS womenS;
    @SerializedName("Kid's")
    @Expose
    private KidS kidS;
    @SerializedName("Pets")
    @Expose
    private Pets pets;
    @SerializedName("Theme")
    @Expose
    private Theme theme;
    @SerializedName("Film & Theatre")
    @Expose
    private FilmTheatre filmTheatre;
    @SerializedName("Unique Fashion")
    @Expose
    private UniqueFashion uniqueFashion;
    @SerializedName("Cosplay")
    @Expose
    private Cosplay cosplay;

    public MenS getMenS() {
        return menS;
    }

    public void setMenS(MenS menS) {
        this.menS = menS;
    }

    public WomenS getWomenS() {
        return womenS;
    }

    public void setWomenS(WomenS womenS) {
        this.womenS = womenS;
    }

    public KidS getKidS() {
        return kidS;
    }

    public void setKidS(KidS kidS) {
        this.kidS = kidS;
    }

    public Pets getPets() {
        return pets;
    }

    public void setPets(Pets pets) {
        this.pets = pets;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public FilmTheatre getFilmTheatre() {
        return filmTheatre;
    }

    public void setFilmTheatre(FilmTheatre filmTheatre) {
        this.filmTheatre = filmTheatre;
    }

    public UniqueFashion getUniqueFashion() {
        return uniqueFashion;
    }

    public void setUniqueFashion(UniqueFashion uniqueFashion) {
        this.uniqueFashion = uniqueFashion;
    }

    public Cosplay getCosplay() {
        return cosplay;
    }

    public void setCosplay(Cosplay cosplay) {
        this.cosplay = cosplay;
    }

}
