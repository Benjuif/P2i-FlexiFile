package fr.insalyon.p2i2.javaarduino;


import java.sql.Date;


public class Capteur {
	
	private final int idCapteur; 
	private final int numSerie;
        private final int idGroupe;
        
        private int distanceX; 
        
        private int position; 
        private Date dateDebut; 
        private Date dateFin;
	
	public Capteur (int id, int num,int idG){
		idCapteur = id; 
		numSerie = num; 
                idGroupe = idG; 
	}
	
	public int getIdCapteur(){
		return idCapteur;
	}
        public int getIdGroupe(){
		return idGroupe;
	}
	public int getNumSerie(){
		return numSerie;
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
        public void setDistanceX(int X){
		distanceX = X;
	}
}

