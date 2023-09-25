package org.cnss.UI;

import org.cnss.Dao.PatientDAO;
import org.cnss.model.AgentCNSS;
import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.model.Patient;

import java.util.ArrayList;
import java.util.Scanner;

public class AgentCnssUI {
    private static String agentConnecteEmail;
    static Scanner scanner = new Scanner(System.in);
    static  AgentCNSSDAO agentDAO = new AgentCNSSDAO(); // Create an instance of AgentCNSSDAO
    static PatientDAO patientDAO = new PatientDAO();
    public static void Authentified(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre email : ");
        String enteredEmail = scanner.nextLine();
        System.out.print("Entrez votre mot de passe : ");
        String enteredPassword = scanner.nextLine();
        AgentCNSS authenticatedAgent = agentDAO.Authentified(enteredEmail,enteredPassword);
        if (authenticatedAgent != null) {
            // Enregistrez l'email de l'agent connecté
            agentConnecteEmail = enteredEmail;
            // Call the Agent CNSS Menu
            agentMenu(scanner, agentDAO, patientDAO); // Pass agentDAO and patientDAO as arguments
        }
    }
    public static String getAgentConnecteEmail() {
        return agentConnecteEmail;
    }
    public static void agentMenu(Scanner scanner, AgentCNSSDAO agentDAO, PatientDAO patientDAO) {
        int agentChoice;
        do {
            System.out.println("\nAgent CNSS Menu:");
            System.out.println("1. Patient Management");
            System.out.println("2. Dossier Management");
            System.out.println("3. Quit (Log Out)");
            System.out.print("Enter your choice: ");
            agentChoice = scanner.nextInt();
            scanner.nextLine();

            switch (agentChoice) {
                case 1:
                    // Patient Management Menu
                    int patientManagementChoice;
                    do {
                        System.out.println("\nPatient Management Menu:");
                        System.out.println("1. Add a new Patient");
                        System.out.println("2. Update Patient information");
                        System.out.println("3. Delete Patient");
                        System.out.println("4. View All Patients");
                        System.out.println("5. Quit (Log Out)");
                        System.out.print("Enter your choice: ");
                        patientManagementChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (patientManagementChoice) {
                            case 1:
                                // Add a new Patient
                                System.out.print("Enter Patient Name: ");
                                String patientName = scanner.nextLine();
                                System.out.print("Enter Patient Surname: ");
                                String patientSurname = scanner.nextLine();

                                Patient addedPatient = patientDAO.ajouterPatient(patientName, patientSurname);
                                if (addedPatient != null) {
                                    System.out.println("Patient added successfully: " + addedPatient.getNom() + " " + addedPatient.getPrenom());
                                } else {
                                    System.out.println("Failed to add Patient.");
                                }
                                break;
                            case 2:
                                // Update Patient information
                                System.out.print("Enter Patient Matricule to update: ");
                                int patientMatriculeToUpdate = scanner.nextInt();
                                scanner.nextLine();
                                System.out.print("Enter new Patient Name: ");
                                String newPatientName = scanner.nextLine();
                                System.out.print("Enter new Patient Surname: ");
                                String newPatientSurname = scanner.nextLine();

                                boolean updated = patientDAO.updatePatient(
                                        patientMatriculeToUpdate, newPatientName, newPatientSurname);

                                if (updated) {
                                    System.out.println("Patient updated successfully.");
                                } else {
                                    System.out.println("Failed to update Patient.");
                                }
                                break;
                            case 3:
                                // Delete Patient
                                System.out.print("Enter Patient Matricule to delete: ");
                                int patientMatriculeToDelete = scanner.nextInt();

                                boolean deleted = patientDAO.deletePatient(patientMatriculeToDelete);

                                if (deleted) {
                                    System.out.println("Patient deleted successfully.");
                                } else {
                                    System.out.println("Failed to delete Patient.");
                                }
                                break;
                            case 4:
                                // View All Patients
                                ArrayList<Patient> patients = patientDAO.afficherTousLespatients();
                                for (Patient patient : patients) {
                                    System.out.println("Matricule: " + patient.getMatricule() + ", Name: " + patient.getNom() + ", Surname: " + patient.getPrenom());
                                }
                                break;
                            case 5:
                                System.out.println("Logged out as Agent CNSS.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } while (patientManagementChoice != 5);
                    break;
                case 2:
                    if (agentDAO != null) {
                        DossierUI.AfficherMenuDossiers(scanner, agentDAO);
                    } else {
                        System.out.println("Veuillez vous connecter en tant qu'agent CNSS.");
                    }
                    break;
                case 3:
                    System.out.println("Logged out as Agent CNSS.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (agentChoice != 2);
    }


}
