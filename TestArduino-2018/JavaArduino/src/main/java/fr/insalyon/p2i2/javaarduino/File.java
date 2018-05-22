package fr.insalyon.p2i2.javaarduino;


import java.sql.Date;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bouzi
 */
public class File extends Groupe{
    private final int idFile;  // a discuter 
    private int longeur; 
    public Date tmpAttente; 
    private Date dateMesure; 
    
    public File (int idG, String nom, int d, int idF){
        super (idG,nom,d);
        idFile=idF;
    }
   
    
    public int getIdFile (){
        return idFile;
    }
    public int getLongeur(){
        return longeur; 
    }
    public Date getTmpAttente(){
        return tmpAttente ; 
    }
    public Date getDateMesure(){
        return dateMesure; 
    }
    
    public void setLongeur(int l) {
        longeur = l;
    }
    public void setTmpAttente(Date t ){
        tmpAttente = t;
    }
    public void setDateMesure (Date t){
        dateMesure = t;
    }
    
}
