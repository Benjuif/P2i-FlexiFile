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
public class File {
        //attributs de la classe file 
        private final int idFile;  
        private int idGroupe; 
        private int longueur; 
        public Date tmpAttente; 
        private Date dateMesure; 

        //Constructeur de la classe 
        public File (int idF, int idG){
            idFile=idF;
            idGroupe= idG; 
        }

        // ------ getters -------    
        public int getIdFile (){
            return idFile;
        }
        public int getIdGroupe(){
            return idGroupe; 
        }
        public int getLongueur(){
            return longueur; 
        }
        public Date getTmpAttente(){
            return tmpAttente ; 
        }
        public Date getDateMesure(){
            return dateMesure; 
        }
        
        // ------ setters -------
        public void setLongeur(int l) {
            longueur = l;
        }
        public void setTmpAttente(Date t ){
            tmpAttente = t;
        }
        public void setDateMesure (Date t){
            dateMesure = t;
        }
    
}
