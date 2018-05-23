/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

/**
 *
 * @author bouzi
 */
public class Client {
    //attributs de la classe 
    private static int count = 0; 
    private final int idClient;
    private String nomClient;
    
    //constructeur de la classe 
    public Client (String n){
        idClient =++count; 
        count ++; 
        nomClient = n;
    }
    
    // ------ getters -------  
    public int getIdClient (){
        return idClient;
    }
    public String getNomClient (){
        return nomClient; 
    }
   
    // ------ setters -------
    public void setNomClient (String n){
        nomClient = n; 
    }
}
