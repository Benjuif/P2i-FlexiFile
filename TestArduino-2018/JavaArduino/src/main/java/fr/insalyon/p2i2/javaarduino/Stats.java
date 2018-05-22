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
        private String repas; 
        private Date date; 
        private int valeur ; 

        //constructeur de la classe 
        public Stats(int id, String r){
            idStats= id; 
            repas = r ; 
        }
        
        // ------ getters -------     
        public int getidStats(){
            return idStats; 
        }
        public String getRepas(){
            return repas;
        }
        public Date getDate(){
            return date; 
        }
        public int getValeur(){
            return valeur; 
        }
        
        // ------ setters -------
        public void setDate (Date d){
            date = d;
        }
        public void setValeur (int v){
            valeur = v; 
        }
}
