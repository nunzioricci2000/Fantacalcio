import Control.Controller;
import DAO.*;
import GUI.MainFrame;
import PostgresImplDAO.*;

public class Main {
    public static void main(String[] args) {
        try {
            CalciatoreDAO calciatoreDAO = new CalciatorePostgresImplDAO();
            SkillsDAO skillsDAO = new SkillsPostgresImplDAO();
            RuoliDAO ruoliDAO = new RuoliPostgresImplDAO();
            SquadraDAO squadraDAO = new SquadraPostgresImplDAO();
            MilitanzaDAO militanzaDAO = new MilitanzaPostgresImplDAO();
            TrofeoDAO trofeoDAO = new TrofeoPostgresImplDAO();
            
            
            Controller controller = new Controller(
                    new MainFrame(), calciatoreDAO, skillsDAO, ruoliDAO, 
                    squadraDAO, militanzaDAO, trofeoDAO);
            
            
            // Avvio dell'applicazione
            controller.startApplication();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'avvio dell'applicazione: " + e.getMessage());
            e.printStackTrace();
        }
    }
}