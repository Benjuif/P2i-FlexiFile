package fr.insalyon.p2i2.javaarduino;

import java.util.*;

public class Groupe {
	
	private final int idGroupe;
	private String nomGroupe;
	private int densite;
        
	public ArrayList<Capteur> listCapteur = new ArrayList<Capteur>();
	
	public Groupe(int id, String nom, int d){
		idGroupe = id;
		nomGroupe = nom;
		densite = d;
	}
	
        public Groupe (int id){
                idGroupe = id;   
        }
	
        public int getIdGroupe(){
		return idGroupe;
	}
	public String getNom(){
		return nomGroupe;
	}
	public int getDensite(){
		return densite;
	}
        
   
}
		
