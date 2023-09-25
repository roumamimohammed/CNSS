package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.Administrateur;
import org.cnss.model.AgentCNSS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class AdministrateurDAO {
  private Connection connection;

  public AdministrateurDAO() {
    try {
      connection = DatabaseConnection.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Administrateur authentifier(String email,String code) {
    Administrateur admin = null;
    String sql = "SELECT * FROM admin WHERE Email = ? AND Pass = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, email);
      statement.setString(2, code);

      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        String emailResult = resultSet.getString("Email");
        String passResult = resultSet.getString("Pass");
        admin = new Administrateur(emailResult, passResult);
      }

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return admin;
  }
  }
