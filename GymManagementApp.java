package com.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GymManagementApp {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gym_db";
        String username = "root";
        String password = "Narayanan@2006";
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Main menu loop
            while (true) {
                System.out.println("Select an option:");
                System.out.println("1. Insert Member");
                System.out.println("2. View All Members");
                System.out.println("3. Update Member");
                System.out.println("4. Delete Member");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left over

                switch (choice) {
                    case 1:
                        // Insert new member
                        insertMember(scanner, connection);
                        break;
                    case 2:
                        // View all members
                        readData(connection);
                        break;
                    case 3:
                        // Update existing member
                        System.out.print("Enter member ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new plan name: ");
                        String newPlanName = scanner.nextLine();
                        System.out.print("Enter new plan status: ");
                        String newPlanStatus = scanner.nextLine();
                        updateData(connection, updateId, newPlanName, newPlanStatus);
                        break;
                    case 4:
                        // Delete member
                        System.out.print("Enter member ID to delete: ");
                        int deleteId = scanner.nextInt();
                        deleteData(connection, deleteId);
                        break;
                    case 5:
                        // Exit
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Insert new member
    public static void insertMember(Scanner scanner, Connection connection) {
        String insertSQL = "INSERT INTO gym_memberships (id, name, gender, dob, phone_number, address, " +
                "membership_start_date, membership_end_date, plan_status, plan_name, plan_duration, plan_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Get user input
            System.out.println("Enter member details:");

            System.out.print("ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Gender (Male/Female): ");
            String gender = scanner.nextLine();

            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dobString = scanner.nextLine();
            Date dob = Date.valueOf(dobString);  // Convert String to Date

            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Address: ");
            String address = scanner.nextLine();

            System.out.print("Membership Start Date (YYYY-MM-DD): ");
            String startDateString = scanner.nextLine();
            Date startDate = Date.valueOf(startDateString);

            System.out.print("Membership End Date (YYYY-MM-DD): ");
            String endDateString = scanner.nextLine();
            Date endDate = Date.valueOf(endDateString);

            System.out.print("Plan Status (active/expired): ");
            String planStatus = scanner.nextLine();

            System.out.print("Plan Name (Diamond/Gold/Silver): ");
            String planName = scanner.nextLine();

            System.out.print("Plan Duration (3/6/12 months): ");
            int planDuration = scanner.nextInt();

            System.out.print("Plan Price (399/699/1199): ");
            double planPrice = scanner.nextDouble();

            // Set parameters for the insert statement
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, gender);
            preparedStatement.setDate(4, dob);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, address);
            preparedStatement.setDate(7, startDate);
            preparedStatement.setDate(8, endDate);
            preparedStatement.setString(9, planStatus);
            preparedStatement.setString(10, planName);
            preparedStatement.setInt(11, planDuration);
            preparedStatement.setDouble(12, planPrice);

            // Execute the insert
            preparedStatement.executeUpdate();
            System.out.println("New membership record inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all members
    public static void readData(Connection connection) {
        String selectSQL = "SELECT * FROM gym_memberships";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                Date dob = rs.getDate("dob");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                Date startDate = rs.getDate("membership_start_date");
                Date endDate = rs.getDate("membership_end_date");
                String planStatus = rs.getString("plan_status");
                String planName = rs.getString("plan_name");
                int planDuration = rs.getInt("plan_duration");
                double planPrice = rs.getDouble("plan_price");

                // Display the retrieved data
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Gender: " + gender);
                System.out.println("Date of Birth: " + dob);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Address: " + address);
                System.out.println("Start Date: " + startDate);
                System.out.println("End Date: " + endDate);
                System.out.println("Plan Status: " + planStatus);
                System.out.println("Plan Name: " + planName);
                System.out.println("Plan Duration: " + planDuration);
                System.out.println("Plan Price: " + planPrice);
                System.out.println("-----------------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update existing member
    public static void updateData(Connection connection, int id, String newPlanName, String newPlanStatus) {
        String updateSQL = "UPDATE gym_memberships SET plan_name = ?, plan_status = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newPlanName);
            preparedStatement.setString(2, newPlanStatus);
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record found with ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete member
    public static void deleteData(Connection connection, int id) {
        String deleteSQL = "DELETE FROM gym_memberships WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No record found with ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}-
