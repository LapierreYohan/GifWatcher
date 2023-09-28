package com.example.phonenetworks.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields implements Serializable {
    @SerializedName("num_site")
    @Expose
    private String numSite;
    @SerializedName("coordonnees")
    @Expose
    private List<Float> coordonnees;
    @SerializedName("nom_com")
    @Expose
    private String nomCom;
    @SerializedName("code_op")
    @Expose
    private int codeOp;
    @SerializedName("nom_op")
    @Expose
    private String nomOp;
    @SerializedName("nom_reg")
    @Expose
    private String nomReg;
    @SerializedName("insee_com")
    @Expose
    private String inseeCom;
    @SerializedName("insee_dep")
    @Expose
    private String inseeDep;
    @SerializedName("nom_dep")
    @Expose
    private String nomDep;
    @SerializedName("technologies")
    @Expose
    private String technologies;

    private int image;

    public String getNumSite() {
        return numSite;
    }

    public void setNumSite(String numSite) {
        this.numSite = numSite;
    }

    public List<Float> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(List<Float> coordonnees) {
        this.coordonnees = coordonnees;
    }

    public String getNomCom() {
        return nomCom;
    }

    public void setNomCom(String nomCom) {
        this.nomCom = nomCom;
    }

    public int getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(int codeOp) {
        this.codeOp = codeOp;
    }

    public String getNomOp() {
        return nomOp;
    }

    public void setNomOp(String nomOp) {
        this.nomOp = nomOp;
    }

    public String getNomReg() {
        return nomReg;
    }

    public void setNomReg(String nomReg) {
        this.nomReg = nomReg;
    }

    public String getInseeCom() {
        return inseeCom;
    }

    public void setInseeCom(String inseeCom) {
        this.inseeCom = inseeCom;
    }

    public String getInseeDep() {
        return inseeDep;
    }

    public void setInseeDep(String inseeDep) {
        this.inseeDep = inseeDep;
    }

    public String getNomDep() {
        return nomDep;
    }

    public void setNomDep(String nomDep) {
        this.nomDep = nomDep;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public int getImage() {return this.image;}
    public void setImage(int id) {this.image = id;}
}
