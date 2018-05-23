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
public class Stats {
        //attributs de la classe stats 
        private int idStats ;  
        private Date date; 
        private int longueur;
        private Date temps;

        //constructeur de la classe 
        public Stats( ){
          
        }
        
        // ------ getters -------     
        public int getidStats(){
            return idStats; 
        }

        public Date getDate(){
            return date; 
        }

        
        // ------ setters -------
        public void setDate (Date d){
            date = d;
        }

}
