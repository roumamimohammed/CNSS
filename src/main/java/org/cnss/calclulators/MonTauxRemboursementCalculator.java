package org.cnss.calclulators;

import org.cnss.Dao.DocumentsDAO;
import org.cnss.Ennum.Etat;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;

import java.util.ArrayList;
import java.util.List;

public class MonTauxRemboursementCalculator implements TauxRemboursementCalculator {
    DocumentsDAO documentsDAO=new  DocumentsDAO();
    @Override
    public double calculerTauxRemboursement(DossierRembouresement dossier) {
        ArrayList<Document> documentsdossier = documentsDAO.getDocumentPardossier(dossier);

        double totalRemboursement = 0.0;

        if (Etat.VALIDE.equals(dossier.getEtat())) {
            for (Document document : documentsdossier) {
                double montantDocument = document.getMontant();
                double tauxDocument = document.getTaux() / 100.0;
                double remboursementDocument = montantDocument * tauxDocument;
                totalRemboursement += remboursementDocument;
            } } else {
            return 0.0;
        }
    return totalRemboursement;
}
}
