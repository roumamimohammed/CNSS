package org.cnss;

import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.Dao.CategorieDAO;
import org.cnss.Dao.DocumentsDAO;
import org.cnss.Dao.PatientDAO;
import org.cnss.UI.AdminUI;
import org.cnss.UI.AgentCnssUI;
import org.cnss.UI.DossierUI;
import org.cnss.UI.PatientUI;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Scanner;

import static org.cnss.Dao.AgentCNSSDAO.sendMail;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException {
        sendMail("hol","bonjour","mohammedroumami2016@gmail.com");
        Scanner scanner = new Scanner(System.in);
        int userChoice;
        AgentCNSSDAO agentDAO = new AgentCNSSDAO(); // Create an instance of AgentCNSSDAO
        PatientDAO patientDAO = new PatientDAO(); // Create an instance of PatientDAO
        DocumentsDAO documentsDAO = new DocumentsDAO();
        do {
            System.out.println("Main Menu:");
            System.out.println("1. Authenticate as Administrator");
            System.out.println("2. Authenticate as Agent CNSS");
            System.out.println("3. Afficher Mes dossiers");
            System.out.println("4. Quit");

            System.out.print("Enter your choice: ");
            userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    AdminUI.Authentication();
                    break;
                case 2:
                    AgentCnssUI.Authentified();
                    break;
                case 3:
                    PatientUI.AfficherDossierPatient();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (userChoice != 4);
    }
}
