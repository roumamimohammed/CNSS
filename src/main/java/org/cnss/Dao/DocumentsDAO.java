package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentsDAO {
    private static Connection connection;

    public DocumentsDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Document> getDocumentPardossier(DossierRembouresement dossier) {
        List<Document> documents = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM document WHERE dossier = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, dossier.getCode());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String documentcode = resultSet.getString("Code");
                int montant = resultSet.getInt("Montant");
                int categorycode = resultSet.getInt("Category");

                int taux = resultSet.getInt("taux");

                CategorieDAO categorieDAO =new CategorieDAO();
                Categories categorie = categorieDAO.getCategoriesParcode(categorycode);

                Document document = new Document(documentcode, montant, categorie, dossier,taux);
                documents.add(document);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }
    public boolean ajouterDocument(Document document) {
        try {
            String insertQuery = "INSERT INTO document (Code, Montant, Category, dossier,taux) VALUES (?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,document.getCode());
            preparedStatement.setFloat(2, document.getMontant());
            preparedStatement.setInt(3, document.getCategory().getCode_Categories());
            preparedStatement.setInt(4, document.getDossierRembouresement().getCode());
            preparedStatement.setInt(5, document.getTaux());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
