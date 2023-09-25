package org.cnss.calclulators;

import org.cnss.Dao.DocumentsDAO;
import org.cnss.Ennum.Etat;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;
import java.util.List;

public class MonTauxRemboursementCalculator implements TauxRemboursementCalculator {
    @Override
    public double calculerTauxRemboursement(DossierRembouresement dossier) {
        List<Document> documentsdossier = DocumentsDAO.getDocumentPardossier(dossier);
        Document[] documentsDossier = new Document[0];

        double totalRemboursement = 0.0;

        if (dossier.getEtat() == Etat.VALIDE) {
            for (Document document : documentsDossier) {
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
