package fr.insalyon.p2i2.javaarduino;
import java.sql.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bouzi
 */
public class Mesure {
        //attributs de la classe 
        private int idMesure;  
        private int idCapteur; 
        private Date dateMesure ;
        private double valeur ; 

        //constructeur de la classe Mesure
        public Mesure(int idM, int idC, Date d, double val){
            idMesure = idM; 
            idCapteur = idC; 
            dateMesure = d; 
            valeur = val; 
        }

        // ------ getters -------
        public int getIdMesure (){
            return idMesure; 
        }
        public int getIdCapteur(){
            return idCapteur; 
        }
        public Date getDateMesure (){
            return dateMesure;
        }
        public double getValeur (){
            return valeur; 
        }
        
        // ------ setters -------
        public void setValeur(int v){
            valeur = v;
        }
}
