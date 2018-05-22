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
   private int idCapteur; 
   private Date dateMesure ;
   private double valeur ; 
   
   
   public Mesure(int id,Date d, double val){
       idCapteur = id; 
       dateMesure = d; 
       valeur = val; 
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

}
