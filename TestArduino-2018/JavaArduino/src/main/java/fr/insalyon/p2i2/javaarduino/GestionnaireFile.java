/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoManager;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author bouzi
 */
public class GestionnaireFile {
    //Attributs de la classe GestionairFile
    ArrayList <Groupe> listeGroupe = new ArrayList <Groupe>(); 
    private Client client ; 
    private Connection connection; 
    
    
    //Constructeur
    public GestionnaireFile (Client c ,Connection con){
        client = c;
        connection = con ;
        this.init(); 
    }
        
    
    public void init(){
    
        String bd = "G223_B_BD2";
        String login = "G223_B";
        String mdp = "G223_B";
	try {

            // Chargement de la classe du driver par le DriverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            // Création d'une connexion sur la base de donnée
            connection = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/" + bd, login, mdp);
            System.out.println("Connexion établie...");

        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        // Objet matérialisant la console d'exécution (Affichage Écran / Lecture Clavier)
        final Console console = new Console();

        // Affichage sur la console
        console.log("DÉBUT du programme TestArduino");

        console.log("TOUS les Ports COM Virtuels:");
        for (String port : ArduinoManager.listVirtualComPorts()) {
            console.log(" - " + port);
        }
        console.log("----");

        // Recherche d'un port disponible (avec une liste d'exceptions si besoin)
        String myPort = ArduinoManager.searchVirtualComPort("COM0", "/dev/tty.usbserial-FTUS8LMO", "COM1", "COM2", "COM3", "COM7");

        console.log("CONNEXION au port " + myPort);
        
        ArduinoManager arduino;
        arduino = new ArduinoManager(myPort) {
            @Override
            protected void onData(String line) {

                // Cette méthode est appelée AUTOMATIQUEMENT lorsque l'Arduino envoie des données
                // Affichage sur la Console de la ligne transmise par l'Arduino
                console.println("ARDUINO >> " + line);

                String[] splitted = line.split(",");
                //insertion de la mesure en BD
                try {
                    //Creation de la requete
                    
                    String sqlStr = "INSERT INTO Mesure(idCapteur, dateMesure, valeur) VALUES (?,?,?)";
                    PreparedStatement ps= connection.prepareStatement(sqlStr);
                    int idCapteur = Integer.parseInt(splitted[0]);
                    double valeur = Double.parseDouble(splitted[1]);
                    long timestamp = Long.parseLong(splitted[2]);
                    java.sql.Timestamp times = new java.sql.Timestamp(timestamp - 7200000); //local time -> utc (2 hour)
                    
                    ps.setInt(1, idCapteur);
                    ps.setTimestamp(2, times);
                    ps.setDouble(3, valeur);
                    //execution de la requete
                    ps.executeUpdate();
                }
                catch(NumberFormatException e){
                    //si une erreur se produit, affichage du message correspondant
                    System.out.println(e.getMessage());
                } catch (SQLException e) {
                    //si une erreur se produit, affichage du message correspondant
                    System.out.println(e.getMessage());
                }
            }     
        };


        try {

            console.log("DÉMARRAGE de la connexion");
            // Connexion à l'Arduino
            arduino.start();

            console.log("BOUCLE infinie en attente du Clavier");
            // Boucle d'ecriture sur l'arduino (execution concurrente au thread)
            boolean exit = false;

            while (!exit) {

                // Lecture Clavier de la ligne saisie par l'Utilisateur
                String line = console.readLine("Envoyer une ligne (ou 'stop') > ");

                if (line.length() != 0) {

                    // Affichage sur l'écran
                    console.log("CLAVIER >> " + line);

                    // Test de sortie de boucle
                    exit = line.equalsIgnoreCase("stop");

                    if (!exit) {
                        // Envoi sur l'Arduino du texte saisi au Clavier
                        arduino.write(line);
                    }
                }
            }

            console.log("ARRÊT de la connexion");
            // Fin de la connexion à l'Arduino
            arduino.stop();

        } catch (IOException ex) {
            // Si un problème a eu lieu...
            console.log(ex);
        }
        this.initCapteur();
        this.setDistanceX();
        this.updateDistanceX();
    }
    public void initListGroupe (){
        int idGroupe ; 
        String nomGroupe; 
              try{            
            String query = "select * from Groupe";
       
            // Construction de l'objet « requête parametrée »
            Statement statement= connection.createStatement();
            
            //execution de la requete
            ResultSet rs = statement.executeQuery(query);
            System.out.println("requete executee ....");
            
            while ( rs.next () ) {
            idGroupe =rs.getInt("idGroupe"); 
            nomGroupe = rs.getString("nomGroupe");
            listeGroupe.add(new Groupe (idGroupe, nomGroupe)); 
            }
        }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            System.out.println(e.getMessage());
            System.exit(0);
            } 
    }
    
    /** Méthode permettant d'associer à chaque groupe de la liste des groupes du gestionnaire 
    *   les différents capteurs installés dans le groupe */
    
     public void initCapteur(){ 
        Capteur cap; 
        int idGroupe; 
        for (Groupe grp : listeGroupe){ 
            idGroupe= grp.getIdGroupe();         
            try{            
                String query = "select * from Capteur where idGroupe= ? ";

                // Construction de l'objet « requête parametrée »
                PreparedStatement ps = connection.prepareStatement(query);

                // transformation en requête statique
                ps.setInt(1,idGroupe) ; 

                //execution de la requete
                ResultSet rs = ps.executeQuery();
                System.out.println("requete executee ....");

                while ( rs.next () ) {
                    cap = new Capteur (rs.getInt("idCapteur"),idGroupe,rs.getInt("numSerie"));
                    grp.listeCapteur.add(cap);    
                }
            }
            catch(SQLException e){
                //si une erreur se produit, affichage du message correspondant
                System.out.println(e.getMessage());
                System.exit(0);
                }
            }
     }
     
        /** Méthode permettant d'associer à chaque capteur (d'un même groupe)
         *  la distance X à laquelle il est positionné par rapport au  mur (obstacle à vide) 
          */
    
        public void setDistanceX(){ 
            String query = "select MAX(m.valeur) from Capteur c, Mesure m where c.idCapteur=? and m.idCapteur= c.idCapteur ;";
            for (Groupe grp : listeGroupe ){                
                for (Capteur c : grp.listeCapteur ){
                       try{
                        // Construction de l'objet « requête parametrée »
                        PreparedStatement ps = connection.prepareStatement(query);

                         // transformation en requête statique
                        ps.setInt(1,c.getIdCapteur()) ; 

                        //execution de la requete
                        ResultSet rs = ps.executeQuery();
                        System.out.println("requete executee ....");

                        c.localisation.setDistanceX(rs.getInt(query));
                    }
                    catch(Exception e){
                        //si une erreur se produit, affichage du message correspondant
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }
                }
            }
        }
        
        /** Méthode permettant d'insérer la valeur de la distanceX pour chaque entité capteur d'un même groupe 
          */

        public void updateDistanceX (){ 
            String query = "UPDATE Capteur set distanceX =? where idCapteur =?";
            for (Groupe grp : listeGroupe){
                for (Capteur c : grp.listeCapteur ){                    
                    try{
                        // Construction de l'objet « requête parametrée »
                        PreparedStatement ps = connection.prepareStatement(query);

                        // transformation en requête statique
                        ps.setInt(1,c.localisation.getDistanceX()) ; 
                        ps.setInt(2,c.getIdCapteur());

                        //execution de la requete
                        ps.executeUpdate();
                        System.out.println("update réussie");
                    }

                    catch(SQLException e){
                        //si une erreur se produit, affichage du message correspondant
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }
                }
        }
        }
        // ------ getters -------  
        public Client getClient (){
            return client; 
        }
}
