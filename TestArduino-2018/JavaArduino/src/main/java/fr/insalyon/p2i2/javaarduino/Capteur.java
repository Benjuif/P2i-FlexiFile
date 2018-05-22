package fr.insalyon.p2i2.javaarduino;


import java.util.*;

public class Capteur {
	
        // attributs de la classe capteur 
	private final int idCapteur; 
        private final int idGroupe;
	private final int numSerie;
        private final String fabricant = "Grove";
        public ArrayList <Mesure> listeMesure ; 
        public Localisation localisation; 
	
        //Constructeur de la classe
	public Capteur (int id, int idG,int num){
            idCapteur = id; 
            numSerie = num; 
            idGroupe = idG; 
	}

        // ------ getters -------
	public int getIdCapteur(){
            return idCapteur;
	}
        public int getIdGroupe(){
            return idGroupe;
	}
	public int getNumSerie(){
            return numSerie;
	}
        public String getFabricant(){
            return fabricant;
        }

}

