import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Sandy@04";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
                System.out.println("1. Reserve a room");
                System.out.println("2. View reservations");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> reserveRoom(connection, scanner);
                    case 2 -> viewReservations(connection);
                    case 3 -> getRoomNumber(connection, scanner);
                    case 4 -> updateReservation(connection, scanner);
                    case 5 -> deleteReservation(connection, scanner);
                    case 0 -> {
                        exit();
                        scanner.close();
                        connection.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- RESERVE ROOM ----------------
    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine(); // consume leftover newline

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine().trim();

            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.nextLine().trim();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number, reservation_date) " +
                    "VALUES (?, ?, ?, NOW())";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guestName);
            ps.setInt(2, roomNumber);
            ps.setString(3, contactNumber);

            ps.executeUpdate();
            System.out.println("Reservation successful!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- VIEW RESERVATIONS ----------------
    private static void viewReservations(Connection connection) {
        try {
            String sql = "SELECT * FROM reservations";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            System.out.println("\n----- CURRENT RESERVATIONS -----");

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", meta.getColumnName(i));
            }
            System.out.println();

            // Print rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", rs.getString(i));
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- GET ROOM NUMBER ----------------
    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine().trim();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.nextLine().trim();

            String sql = "SELECT room_number FROM reservations WHERE guest_name = ? AND contact_number = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guestName);
            ps.setString(2, contactNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Room Number: " + rs.getInt("room_number"));
            } else {
                System.out.println("Reservation not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- UPDATE RESERVATION ----------------
    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();

            System.out.print("Enter current guest name: ");
            String oldGuestName = scanner.nextLine().trim();

            System.out.print("Enter current room number: ");
            int oldRoomNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter current contact number: ");
            String oldContactNumber = scanner.nextLine().trim();

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine().trim();

            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.nextLine().trim();

            String sql = "UPDATE reservations SET guest_name=?, room_number=?, contact_number=? " +
                    "WHERE guest_name=? AND room_number=? AND contact_number=?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newGuestName);
            ps.setInt(2, newRoomNumber);
            ps.setString(3, newContactNumber);
            ps.setString(4, oldGuestName);
            ps.setInt(5, oldRoomNumber);
            ps.setString(6, oldContactNumber);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? " Reservation updated successfully!" : " No matching reservation found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- DELETE RESERVATION ----------------
    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine().trim();

            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.nextLine().trim();

            String sql = "DELETE FROM reservations WHERE guest_name=? AND room_number=? AND contact_number=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guestName);
            ps.setInt(2, roomNumber);
            ps.setString(3, contactNumber);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? " Reservation deleted successfully!" : " No matching reservation found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- EXIT ----------------
    private static void exit() throws InterruptedException {
        System.out.print("Exiting");
        for (int i = 0; i < 3; i++) {
            Thread.sleep(400);
            System.out.print(".");
        }
        System.out.println("\n Thank you for using Hotel Management System!");
    }
}
