package org.cnss;
import org.cnss.UI.AdminUI;
import org.cnss.UI.AgentCnssUI;
import org.cnss.UI.PatientUI;
import org.cnss.model.AgentCNSS;

import java.security.GeneralSecurityException;
import java.util.InputMismatchException;
import java.util.Scanner;

public  class  Main {
    public static void main(String[] args) throws GeneralSecurityException {
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Authenticate as Administrator");
            System.out.println("2. Authenticate as Agent CNSS");
            System.out.println("3. Afficher Mes dossiers");
            System.out.println("4. Quit");

            try {
                userChoice= 0;
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
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}
