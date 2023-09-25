package org.cnss.UI;

import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.Dao.DossierRembouresementDAO;
import org.cnss.Dao.PatientDAO;
import org.cnss.model.AgentCNSS;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Patient;
import java.util.Scanner;

import static org.cnss.UI.DocumentUI.AjouterDocument;

public class DossierUI {
    static DossierRembouresementDAO dossierRembouresementDAO = new DossierRembouresementDAO();
    PatientDAO patientDAO = new PatientDAO();
    AgentCNSSDAO agentCNSS=new AgentCNSSDAO();
    static Scanner scanner = new Scanner(System.in);
    public static void AfficherMenuDossiers(Scanner scanner, AgentCNSSDAO agentCNSS) {
        int choix;

        do {
            System.out.println("\nMenu Dossiers de Remboursement:");
            System.out.println("1. Ajouter un Dossier");
            System.out.println("2. Mettre à jour un Dossier");
            System.out.println("3. Supprimer un Dossier");
            System.out.println("4. Quitter le Menu Dossiers");
            System.out.print("Entrez votre choix: ");
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    AjouterDossier(agentCNSS);
                    break;
                case 2:
                    MettreAJourDossier();
                    break;
                case 3:
                    SupprimerDossier();
                    break;
                case 4:
                    System.out.println("Retour au Menu Principal.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 4);
    }

    public static void AjouterDossier(AgentCNSSDAO agentCNSSDAO) {
        Scanner scanner = new Scanner(System.in);
        // Obtenez l'agent CNSS actuellement connecté en utilisant la classe AgentCnssUI
        String agentConnecteEmail = AgentCnssUI.getAgentConnecteEmail();
        AgentCNSS agentCNSS = agentCNSSDAO.getAgentParEmail(agentConnecteEmail);
        PatientDAO patientDAO = new PatientDAO();
        System.out.println("Ajouter un Dossier:");
        System.out.println("Choisir le patient pour le dossier:");
        patientDAO.afficherTousLespatients();
        int codePatient = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Patient patientTrouve = patientDAO.getPatientParMatricule(codePatient);

        if (patientTrouve != null) {
            DossierRembouresement dossier = dossierRembouresementDAO.ajouterDossierRemboursement(agentCNSS, patientTrouve);
            if (dossier != null) {
                System.out.println("Dossier ajouté avec succès.");
                AjouterDocument();
            } else {
                System.out.println("Échec de l'ajout du dossier.");
            }
        } else {
            System.out.println("Patient non trouvé avec le code saisi.");
        }
    }


    public static void MettreAJourDossier() {
        System.out.println("Mettre à jour un Dossier:");
        System.out.print("Entrez le code du dossier que vous souhaitez mettre à jour: ");
        int codeDossier = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        DossierRembouresement dossier = recherchedossierparcode(codeDossier);
        if (dossier != null) {
            // Mettre à jour le dossier ici (état, montant, etc.)
            // dossierRembouresementDAO.updateDossierRemboursement(dossier);
            System.out.println("Dossier mis à jour avec succès.");
        }
    }

    public static void SupprimerDossier() {
        System.out.println("Supprimer un Dossier:");
        System.out.print("Entrez le code du dossier que vous souhaitez supprimer: ");
        int codeDossier = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        DossierRembouresement dossier = recherchedossierparcode(codeDossier);
        if (dossier != null) {
            boolean supprime = dossierRembouresementDAO.deleteDossierRemboursement(codeDossier);
            if (supprime) {
                System.out.println("Dossier supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du dossier.");
            }
        }
    }

    public static DossierRembouresement recherchedossierparcode(int code) {
        DossierRembouresement dossierRembouresement = dossierRembouresementDAO.getDossierParCode(code);
        if (dossierRembouresement == null) {
            System.out.println("Aucun dossier trouvé avec le code saisi.");
        }
        return dossierRembouresement;
    }
}
