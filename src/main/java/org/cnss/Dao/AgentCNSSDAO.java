package org.cnss.Dao;

import com.sun.mail.util.MailSSLSocketFactory;
import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.AgentCNSS;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentCNSSDAO {
    private Connection connection;
    private int loginAttempts = 0;
    private boolean isBlocked = false;

    public AgentCNSSDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AgentCNSS Authentified(String enteredEmail,String enteredPassword) {
        // Verify credentials and check "Active" attribute
        // Verify credentials and check "Active" attribute
        AgentCNSS agent = getAgentByEmail(enteredEmail);
        if (isBlocked(enteredEmail)) {
            System.out.println("Votre compte est bloqué. Veuillez contacter l'administrateur.");
            return null;
        }
        if (agent != null && agent.getMotDePasse().equals(enteredPassword)) {
            if (isActive(agent.getEmail())) { // Check if the agent is active
                loginAttempts = 0;
                System.out.println("Authentification réussie.");
                return agent; // Return the authenticated agent
            } else {
                System.out.println("Votre compte est désactivé. Veuillez contacter l'administrateur.");
                return null;
            }
        } else {
            loginAttempts++;
            System.out.println("Authentification échouée.");

            if (loginAttempts >= 3) {
                isBlocked = true;
                System.out.println("Votre compte est maintenant bloqué après 3 échecs.");
                // Set the "Active" attribute to 0 in the database for this user
                deactivateAgent(agent.getEmail());
            }
            return null;
        }
    }


    // Méthode pour récupérer un agent par son email depuis la base de données
    private AgentCNSS getAgentByEmail(String email) {
        String sql = "SELECT * FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        AgentCNSS agent = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Email"); // Modifier ici pour correspondre à votre structure
                String motDePasse = resultSet.getString("Pass"); // Modifier ici pour correspondre à votre structure
                int codeVerification = resultSet.getInt("Code_Verification"); // Modifier ici pour correspondre à votre structure
                int active = resultSet.getInt("Active"); // Modifier ici pour correspondre à votre structure
                agent = new AgentCNSS(nom, motDePasse, email, codeVerification, active);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return agent;
    }

    // Méthode pour désactiver un agent en mettant "Active" à 0 dans la base de données
    private void deactivateAgent(String email) {
        String sql = "UPDATE agences SET Active = 0 WHERE Email = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Update the isBlocked method to send an email to the admin
    public boolean isBlocked(String email) {
        String sql = "SELECT Active FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int activeStatus = resultSet.getInt("Active");

                if (activeStatus == 0) {
                    // Agent is blocked, send email to admin
                    sendMail("hol","bonjour","mohammedroumami2016@gmail.com");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }



    public static Boolean sendMail(String body, String subject, String email) throws GeneralSecurityException {
        final String username = "roumamimohammed2@gmail.com";
        final String password = "ukon cvmx cmxd jpnn";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");

        // Add this line to trust all certificates (not recommended for production)
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isActive(String email) {
        String sql = "SELECT Active FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isActive = false;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int activeStatus = resultSet.getInt("Active");
                isActive = activeStatus == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isActive;
    }
    public String ajoutAgentCnss(String Nom, String email, String code) {
        String Nomagent = null;
        try {
            AgentCNSS existingAgent = getAgentParEmail(email);

            if (existingAgent != null) {
                System.out.println("Cette adresse e-mail est déjà associée à une agence existante");
                return null;
            }else {
                String insertQuery = "INSERT INTO agences (Email, Pass, Nom,Active) VALUES (?,?,?,1)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, code);
                preparedStatement.setString(3, Nom);

                preparedStatement.executeUpdate();

                Nomagent = Nom;


                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Nomagent;
    }
    public boolean updateAgentCnss( String email , String nouveauNom, String nouveauEmail) {
        try {
            AgentCNSS existingAgent = getAgentParEmail(email);

            if (existingAgent == null) {
                System.out.println("Cette Agent n'existe pas");
                return false;
            }
            String checkEmailQuery = "SELECT Email FROM agences WHERE Email=?";
            PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery);
            checkEmailStatement.setString(1, email);

            ResultSet emailResultSet = checkEmailStatement.executeQuery();

            if (!emailResultSet.next()) {
                return false;
            } else {
                existingAgent = getAgentParEmail(nouveauEmail);

                if (existingAgent != null) {
                    System.out.println("Cette adresse e-mail est déjà associée à une agence existante");
                    return false;
                }else {


                    String updateQuery = "UPDATE agences SET Nom=?, Email=? WHERE  Email=? ";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, nouveauNom);
                    preparedStatement.setString(2, nouveauEmail);
                    preparedStatement.setString(3, email);

                    int rowsUpdated = preparedStatement.executeUpdate();

                    preparedStatement.close();

                    return rowsUpdated > 0;}
            } } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteAgentCnss(String email) {
        try {
            String deleteQuery = "DELETE FROM agences WHERE  Email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, email);

            int rowsDeleted = preparedStatement.executeUpdate();

            preparedStatement.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<AgentCNSS> afficherTousLesAgents() {
        ArrayList<AgentCNSS> agents = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM agences";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String email = resultSet.getString("Email");
                String code = resultSet.getString("Pass");
                int Code_Verification = resultSet.getInt("Code_Verification");

                AgentCNSS agent = new AgentCNSS(nom,code,email,Code_Verification,1);
                agents.add(agent);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agents;
    }
    public AgentCNSS getAgentParEmail(String email) {
        AgentCNSS agent = null;
        try {
            String selectQuery = "SELECT * FROM agences WHERE Email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String code = resultSet.getString("Pass");
                int codeVerification = resultSet.getInt("Code_Verification");

                agent = new AgentCNSS(nom, code, email, codeVerification,1);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agent;
    }

}
