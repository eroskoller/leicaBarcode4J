/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dasa.leica;

import com.google.gson.Gson;

/**
 *
 * @author eros
 */
public class Lamina {

    private String amostra;
    private String fap;
    private String codeOrdem;
    private String nomePac;
    private Character sexo;
    private String dataNasc;

    public Lamina(String amostra, String fap, String codeOrdem) {
        this.amostra = amostra;
        this.fap = fap;
        this.codeOrdem = codeOrdem;
    }

    public Lamina(String amostra, String fap, String codeOrdem, String nomePac, Character sexo, String dataNasc) {
        this.amostra = amostra;
        this.fap = fap;
        this.codeOrdem = codeOrdem;
        this.nomePac = nomePac;
        this.sexo = sexo;
        this.dataNasc = dataNasc;
    }

    public String getAmostra() {
        return amostra;
    }

    public void setAmostra(String amostra) {
        this.amostra = amostra;
    }

    public String getFap() {
        return fap;
    }

    public void setFap(String fap) {
        this.fap = fap;
    }

    public String getCodeOrdem() {
        return codeOrdem;
    }

    public void setCodeOrdem(String codeOrdem) {
        this.codeOrdem = codeOrdem;
    }

    public String getNomePac() {
        return nomePac;
    }

    public void setNomePac(String nomePac) {
        this.nomePac = nomePac;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    @Override
    public String toString() {
        return  new Gson().toJson(this);
    }
    
    
    
}
