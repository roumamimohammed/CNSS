package org.cnss;

import org.cnss.UI.AdminUI;
import org.cnss.UI.AgentCnssUI;
import org.cnss.UI.PatientUI;

import java.security.GeneralSecurityException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws GeneralSecurityException {
        Scanner scanner = new Scanner(System.in);
        int userChoice;
        while (true){
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
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

    }
}}
