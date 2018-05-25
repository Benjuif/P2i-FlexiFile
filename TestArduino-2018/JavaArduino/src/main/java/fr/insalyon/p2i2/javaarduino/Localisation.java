/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.sql.Date;

/**
 *
 * @author bouzi
 */
public class Localisation {
        
        // attributs de la classe localisation
        private int idLocalisation; 
        private int idCapteur; 
        private int position; 
        private int distanceX ; 
        private Date dateDebut; 
        private Date dateFin; 
        
        //Constructeur de la classe
        public Localisation(int idL, int idC,int p , Date date ){
            idLocalisation = idL ;
            idCapteur = idC;
            position= p; 
            dateDebut= date; 
            
        }
        
        public Localisation(){
        }

        
        // ------ getters -------
        public int getIdLocalisation(){
            return idLocalisation;
	}
        public int getIdCapteur(){
            return idCapteur;
	}
        public int getDistanceX(){
            return distanceX;
	}
        public int getPosition(){
            return position;   
        }
        public Date getDateDebut(){
                return dateDebut;
        }
        public Date getDateFin(){
                return dateFin; 
        }
        
        // ------ setters -------
        public void setDistanceX(int X){
		distanceX = X;
	}
        public void setDateDebut (Date d){
            dateDebut = d; 
        }
        public void setDateFin (Date d){
            dateDebut = d; 
        }
}
