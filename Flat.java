package org.example;

import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Flat {
    String district;
    String address;
    int area;
    int rooms;
    int price;
    public Flat(){}

    public Flat(String district, String address, int area, int rooms, int price) {
        this.district = district;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.price = price;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flat{" +
                "district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", rooms=" + rooms +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return area == flat.area &&
                rooms == flat.rooms &&
                price == flat.price &&
                Objects.equals(district, flat.district) &&
                Objects.equals(address, flat.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(district, address, area, rooms, price);
    }

    static Flat info(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your district : ");
        String district = scanner.nextLine();
        System.out.print("Enter your address : ");
        String address = scanner.nextLine();
        System.out.print("Enter area of your flat : ");
        int area = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount of rooms : ");
        int rooms = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your price : ");
        int price = Integer.parseInt(scanner.nextLine());
        return new Flat(district, address, area, rooms, price);
    }

    static void add(Connection connection) throws SQLException {
        Flat flat = info();
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Flats (district, address, area, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, flat.district);
            ps.setString(2, flat.address);
            ps.setInt(3, flat.area);
            ps.setInt(4, flat.rooms);
            ps.setInt(5, flat.price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    static void addHardcode(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter flats count: ");
        int count = Integer.parseInt(scanner.nextLine());

        connection.setAutoCommit(false); // enable transactions
        try {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO Flats" +
                        "(district, address, area, rooms, price) "
                        +"VALUES(?, ?, ?, ?, ?)");
                try {
                    for (int i = 0; i < count; i++) {
                        ps.setString(1, "District" + i);
                        ps.setString(2, "Address" + i);
                        ps.setInt(3, (int)(Math.random()*50) + 10);
                        ps.setInt(4, (int)(Math.random()*4) + 1);
                        ps.setInt(5, (int)(Math.random()*50000));
                        ps.executeUpdate();
                    }
                    connection.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                connection.rollback();
            }
        } finally {
            connection.setAutoCommit(true); // return to default mode
        }
    }

    static void delete(Connection connection) throws SQLException {
        Flat flat = info();
        PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM Flats WHERE district = ?"  +
                        " AND address = ?"  +
                        " AND area = ?" +
                        " AND rooms = ?"  +
                        " AND price = ?");
        try {
            ps.setString(1, flat.district);
            ps.setString(2, flat.address);
            ps.setInt(3, flat.area);
            ps.setInt(4, flat.rooms);
            ps.setInt(5, flat.price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }

    }

    static void modify(Connection connection) throws SQLException {
        Flat flat = info();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new price : ");
        int newPrice = Integer.parseInt(scanner.nextLine());
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE Flats SET price = ? " +
                        "WHERE district = ? AND address = ? AND area = ? AND rooms = ?");
        try {
            ps.setInt(1, newPrice);
            ps.setString(2, flat.district);
            ps.setString(3, flat.address);
            ps.setInt(4, flat.area);
            ps.setInt(5, flat.rooms);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }


    static void viewAllFlats(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Flats");
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "         ");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "          ");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
    static void viewByParam(Connection connection, String param) throws SQLException {
        String res = "";
        int result = 0;
        PreparedStatement ps = null;

        if (param.equals("1")) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("You chose parameter \"district\". Enter needful district : ");
            res = scanner.nextLine();
            ps = connection.prepareStatement("SELECT * FROM Flats WHERE district = \"" + res + "\" ");
        }
        if (param.equals("2")){
            Scanner scanner = new Scanner(System.in);
            System.out.print("You chose parameter \"address\". Enter needful address : ");
            res = scanner.nextLine();
            ps = connection.prepareStatement("SELECT * FROM Flats WHERE address = \"" + res + "\" ");
        }
        if (param.equals("3")){
            Scanner scanner = new Scanner(System.in);
            System.out.print("You chose parameter \"area\". We will show you flats with entered area or larger." +
                    " Enter needful area : ");
            result = Integer.parseInt(scanner.nextLine());
            ps = connection.prepareStatement("SELECT * FROM Flats WHERE area >= " + result + " ");
        }
        if (param.equals("4")){
            Scanner scanner = new Scanner(System.in);
            System.out.print("You chose parameter \"rooms\". We will show you flats with entered amount of rooms." +
                    " Enter amount of rooms : ");
            result = Integer.parseInt(scanner.nextLine());
            ps = connection.prepareStatement("SELECT * FROM Flats WHERE rooms = " + result + " ");
        }
        if (param.equals("5")){
            Scanner scanner = new Scanner(System.in);
            System.out.print("You chose parameter \"price\". We will show you flats with entered price or more expensive." +
                    " Enter lowest price : ");
            result = Integer.parseInt(scanner.nextLine());
            ps = connection.prepareStatement("SELECT * FROM Flats WHERE price >= " + result + " ");
        }
        //PreparedStatement ps = connection.prepareStatement("SELECT district, address, " + choice + " FROM Flats" +
                //"WHERE " + choice + "=" + res);
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    static String choice(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choice number of your parameter");
        System.out.println("1: district");
        System.out.println("2: address");
        System.out.println("3: area");
        System.out.println("4: rooms");
        System.out.println("5: price");
        System.out.print("-> ");
        return scanner.nextLine();
    }
}
