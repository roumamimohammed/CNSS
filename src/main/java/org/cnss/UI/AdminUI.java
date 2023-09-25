package org.cnss.UI;

import org.cnss.Dao.AdministrateurDAO;
import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.model.Administrateur;
import org.cnss.model.AgentCNSS;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminUI {
    static AdministrateurDAO administrateurDAO = new AdministrateurDAO();
    static AgentCNSSDAO agentDAO = new AgentCNSSDAO();
    static Scanner scanner = new Scanner(System.in);
    public static void Authentication(){

        System.out.print("Entrez votre email : ");
        String email = scanner.nextLine();
        System.out.print("Entrez votre mot de passe : ");
        String code = scanner.nextLine();
        Administrateur authenticatedAdmin = administrateurDAO.authentifier(email,code);
        if (authenticatedAdmin != null) {
            // Call the Administrator Menu
            administratorMenu(scanner, agentDAO);
        }
    }
    public static void administratorMenu(Scanner scanner, AgentCNSSDAO agentDAO) {
        int adminChoice;
        do {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. Agent Management");
            System.out.println("2. Quit (Log Out)");
            System.out.print("Enter your choice: ");
            adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    // Agent CNSS Management Menu
                    int agentManagementChoice;
                    do {
                        System.out.println("\nAgent CNSS Management Menu:");
                        System.out.println("1. Add a new Agent");
                        System.out.println("2. Update Agent information");
                        System.out.println("3. Delete Agent");
                        System.out.println("4. View All Agents");
                        System.out.println("5. Quit (Log Out)");
                        System.out.print("Enter your choice: ");
                        agentManagementChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (agentManagementChoice) {
                            case 1:
                                // Add a new Agent
                                System.out.print("Enter Agent Name: ");
                                String agentName = scanner.nextLine();
                                System.out.print("Enter Agent Email: ");
                                String agentEmail = scanner.nextLine();
                                System.out.print("Enter Agent Code: ");
                                String agentCode = scanner.nextLine();

                                String addedAgent = agentDAO.ajoutAgentCnss(agentName, agentEmail, agentCode);
                                if (addedAgent != null) {
                                    System.out.println("Agent added successfully: " + addedAgent);
                                } else {
                                    System.out.println("Failed to add Agent.");
                                }
                                break;
                            case 2:
                                // Update Agent information

                                System.out.print("Enter Agent Email to update: ");
                                String agentToUpdateEmail = scanner.nextLine();
                                System.out.print("Enter new Agent Name: ");
                                String newAgentName = scanner.nextLine();
                                System.out.print("Enter new Agent Email: ");
                                String newAgentEmail = scanner.nextLine();

                                boolean updated = agentDAO.updateAgentCnss(agentToUpdateEmail, newAgentName,newAgentEmail);

                                if (updated) {
                                    System.out.println("Agent updated successfully.");
                                } else {
                                    System.out.println("Failed to update Agent.");
                                }
                                break;
                            case 3:
                                // Delete Agent
                                System.out.print("Enter Agent Email to delete: ");
                                String agentToDeleteEmail = scanner.nextLine();

                                boolean deleted = agentDAO.deleteAgentCnss(agentToDeleteEmail);

                                if (deleted) {
                                    System.out.println("Agent deleted successfully.");
                                } else {
                                    System.out.println("Failed to delete Agent.");
                                }
                                break;
                            case 4:
                                // View All Agents
                                ArrayList<AgentCNSS> agents = agentDAO.afficherTousLesAgents();
                                for (AgentCNSS agent : agents) {
                                    System.out.println("Name: " + agent.getNom() + ", Email: " + agent.getEmail());
                                }
                                break;
                            case 5:
                                System.out.println("Logged out as Agent CNSS.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } while (agentManagementChoice != 5);
                    break;
                case 2:
                    System.out.println("Logged out as Administrator.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (adminChoice != 2);
    }

}
