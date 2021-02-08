package org.example;

import java.sql.*;
import java.util.Scanner;

public class App {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb1?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "password1234";

    static Connection connection;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            try {
                // create connection
                connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add flat to database");
                    System.out.println("2: add random flat");
                    System.out.println("3: remove flat");
                    System.out.println("4: modify flat price");
                    System.out.println("5: view all flats");
                    System.out.println("6: view by parameter");
                    System.out.print("-> ");

                    String s = scanner.nextLine();
                    switch (s) {
                        case "1":
                            Flat.add(connection);
                            break;
                        case "2":
                            Flat.addHardcode(connection);
                            break;
                        case "3":
                            Flat.delete(connection);
                            break;
                        case "4":
                            Flat.modify(connection);
                            break;
                        case "5":
                            Flat.viewAllFlats(connection);
                            break;
                        case "6":
                            Flat.viewByParam(connection, Flat.choice());
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                scanner.close();
                if (connection != null) connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    " district VARCHAR(30) NOT NULL, " +
                    " address VARCHAR(40) NOT NULL, " +
                    " area INT, " +
                    " rooms INT NOT NULL, " +
                    " price INT NOT NULL)");
        } finally {
            st.close();
        }
        try (Statement st1 = connection.createStatement()) {
            st1.execute("DROP TABLE IF EXISTS Flats");
            st1.execute("CREATE TABLE Flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    " district VARCHAR(30) NOT NULL, " +
                    " address VARCHAR(40) NOT NULL, " +
                    " area INT, " +
                    " rooms INT NOT NULL, " +
                    " price INT NOT NULL)");
        }
    }
}
