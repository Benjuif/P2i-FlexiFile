package fr.insalyon.p2i2.javaarduino;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoManager;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class TestArduino {
    
    public static int main(String[] args)
    {
        TestArduino main = new TestArduino();
        //main.setup();
        main.start();
        return 0;
    }
       
    private Connection connection;
    public GestionnaireFile gestionnaire ; 

    
    public void start (){
        gestionnaire = new GestionnaireFile (new Client ("DDR_INSA"), connection );    
    }
    
    public TestArduino()
    {
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
            
                //calcul longueur file 
                for (Groupe grp: gestionnaire.listeGroupe){
                    // todo : chercher groupe correspondant au capteur
                    try {
                        //Creation de la requete
                        String sqlStr = "INSERT INTO File(longueur, tmpAttente, idGroupe, dateMesure) VALUES (?,?,?,?)";

                        PreparedStatement ps = connection.prepareStatement(sqlStr);
                        int longueur = getLength(grp);
                        long currentTime = System.currentTimeMillis();
                        java.sql.Timestamp time = new java.sql.Timestamp(currentTime - 7200000);

                        ps.setInt(1,longueur);                       
                        ps.setInt (3,grp.getIdGroupe());
                        ps.setTimestamp(4,time);
                      
                        //execution de la requete
                        ps.executeUpdate();
                    }
                    catch(SQLException e){
                        //si une erreur se produit, affichage du message correspondant
                        System.out.println(e.getMessage());
                    }
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

    }
 
    
//
// ------ Méthodes utiles  -------
//
    
    /** Méthode permettant de récupérer les capteurs d'un même groupe passé en paramètre.
     *  @param grp: groupe de capteurs */  
     public void initGroupe(Groupe grp ){ 
        Capteur cap; 
        int idGroupe = grp.getIdGroupe();
        
        try{            
            String query = "select * from Capteur where idGroupe = ?";
       
            // Construction de l'objet « requête parametrée »
            PreparedStatement ps = connection.prepareStatement(query);
            
            // transformation en requête statique
            ps.setInt(1, idGroupe) ; 
            
            //execution de la requete
            ResultSet rs = ps.executeQuery();
            System.out.println("requete executee ....");
            
            while (rs.next()) {
                cap = new Capteur (rs.getInt("idCapteur"), rs.getInt("numSerie"), idGroupe);
                grp.listeCapteur.add(cap);    
            }
        }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
     
    /** Méthode permettant de récupérer la longeur de la file d'attente d'un groupe passée en paramètre
    *  @param grp: groupe de capteurs
    * @return retourne la longeur de la file d'attente à un instant t  */
    public int getLength(Groupe grp){ 
        int idGroupe = grp.getIdGroupe();
        int longueur=0;
            
        try {
            String query = "Select Min(l.position) " +
                            "from Capteur c, Localisation l , Mesure m " +
                            "where c.idGroupe = ? and  m.idCapteur= c.idCapteur and l.idCapteur = c.idCapteur and l.idGroupe=c.idGroupe " +
                            "and m.valeur >=c.distanceX";
            
                 
            // Construction de l'objet « requête parametrée »
            PreparedStatement ps = connection.prepareStatement(query);
            
            // transformation en requête statique
            ps.setInt(1,idGroupe) ; 
     
            //execution de la requete
            ResultSet rs = ps.executeQuery();
            System.out.println("requete executee ....");
            
            longueur= rs.getInt(query);
        }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return longueur;
    }
}
        

            
     
     
			

