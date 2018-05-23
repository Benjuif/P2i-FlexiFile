package fr.insalyon.p2i2.javaarduino;

import java.util.*;

public class Groupe {
	// attributs de la classe localisation
	private final int idGroupe;
	private String nomGroupe;
	private ArrayList<Capteur> listeCapteur = new ArrayList<Capteur>();
        
	//Constructeur de la classe
	public Groupe(int id, String nom){
            idGroupe = id;
            nomGroupe = nom;	
        }
        
        // ------ getters -------      
        public int getIdGroupe(){
            return idGroupe;
	}
	public String getNom(){
            return nomGroupe;
	}
        public ArrayList<Capteur> getListeCapteur(){
            return listeCapteur;
        }
}
		
