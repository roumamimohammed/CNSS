package org.cnss.UI;

import org.cnss.Dao.CategorieDAO;
import org.cnss.Dao.DocumentsDAO;
import org.cnss.Dao.MedicamentDAO;
import org.cnss.Dao.PatientDAO;

import org.cnss.model.Categories;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Medicament;
import java.util.Scanner;

import static org.cnss.UI.AdminUI.scanner;

public class DocumentUI {
    static PatientDAO patientDAO = new PatientDAO();
    static CategorieDAO categorieDAO =new CategorieDAO();
    static MedicamentDAO medicamentDAO =new MedicamentDAO();
    static DocumentsDAO documentsDAO = new DocumentsDAO();

    public static void AjouterDocument() {
        Scanner scanner = new Scanner(System.in); // Create a new scanner instance
        System.out.println("Saisir le code du document:");
        String Code_document = scanner.nextLine();
        System.out.println("Saisir le code du dossier:");
        int Code_dossier = Integer.parseInt(scanner.nextLine()); // Parse the integer input

        DossierRembouresement dossier = DossierUI.recherchedossierparcode(Code_dossier);

        System.out.println("Choisir la catégorie du document:");
        CategoriesUI.afficherListCategories();
        int Code_categorie = Integer.parseInt(scanner.nextLine()); // Parse the integer input
        Categories categorie_document = categorieDAO.getCategoriesParcode(Code_categorie);

        if (categorie_document != null) {
            if ("MEDICAMENT".equals(categorie_document.getNom())) {
                System.out.println("Saisir le nom du médicament:");
                String Nom_medicament = scanner.nextLine();
                MedicamentUI.afficherListMedicamentRechercher(Nom_medicament);
                System.out.println("Choisir le médicament:");
                int Code_medicament = Integer.parseInt(scanner.nextLine()); // Parse the integer input
                Medicament medicament = medicamentDAO.rechercherMedicamentParCode(Code_medicament);

                if (medicament != null) { // Check if medicament is not null
                    int prix = medicament.getPrix();
                    int taux_remboursement = medicament.getTaux_remboursement();
                    Document document = new Document(Code_document, prix, categorie_document, dossier, taux_remboursement);
                    documentsDAO.ajouterDocument(document);
                } else {
                    System.out.println("Médicament invalide sélectionné.");
                }
            } else {
                int taux_remboursement = categorie_document.getTaux_de_rembourssement();
                System.out.println("Saisir le prix du document:");
                int prix = Integer.parseInt(scanner.nextLine()); // Parse the integer input
                Document document = new Document(Code_document, prix, categorie_document, dossier, taux_remboursement);
                documentsDAO.ajouterDocument(document);
            }
        } else {
            System.out.println("Catégorie invalide sélectionnée.");
        }

        // Close the scanner
        scanner.close();
    }







}


