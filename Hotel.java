import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import  java.sql.Statement;
import  java.sql.ResultSet;
public class HotelSystem
{
    private static final String url="";
    private static final String username="";
    private static  final String password="";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Sorry cant load data error occcur "+e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
              while (true)
              {
                  System.out.println();
                  System.out.println("HOTEL MANAGEMENT SYSTEM");
                  Scanner sc = new Scanner(System.in);
                  System.out.println("1. RESERVE A ROOM");
                  System.out.println("2. VIEW RESERVATION");
                  System.out.println("3. GET ROOM NUMBER");
                  System.out.println("4. UPDATE REGISTRATION");
                  System.out.println("5. DELETE REGISTRATION");
                  System.out.println("6. EXIT........");
                  System.out.println("Please Enter Number to work from 1 to 61");
                  int choice=sc.nextInt();
                  switch (choice)
                  {
                      case 1:reserveroom(connection,sc);
                      break;
                      case 2:viewreservation(connection);
                      break;
                      case 3:getroomnumber(connection,sc);
                      break;
                      case 4:updatereservation(connection,sc);
                      break;
                      case 5:deleteregistration(connection,sc);
                      break;
                      case 6:System.exit(0);
                      default:System.err.println("SORRY PLEASE ENTER VALID INPUT");
                  }

              }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }


    }
    private static void reserveroom(Connection connection,Scanner sc)
    {

        System.out.println("WELCOME BACK TO HOTEL MANAGMENT SOFTWAR" );
        sc.nextLine();
        System.out.println("PLEASE ENTER GUEST NAME");
        String name=sc.nextLine().toUpperCase();
        System.out.println("PLEASE ENTER ROOM NUMBER");
        int roomnum=sc.nextInt();
        sc.nextLine();
        System.out.println("PLEASE ENTER CUSTOMER PHONE NUMBER / EMAIL");
        String contact=sc.nextLine();
        String query = "INSERT INTO reservation (guest_name, room_number, contact_number) VALUES ('"
                + name + "', " + roomnum + ", '" + contact + "')";
        try (Statement stmt = connection.createStatement())  {
            int rows = stmt.executeUpdate(query);
            if (rows > 0) {
                System.out.println("✅ Reservation successful for " + name);
            } else {
                System.out.println("❌ Reservation failed.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ SQL Error: " + e.getMessage());
        }
    }
    private  static  void viewreservation(Connection connection)
    {
        System.out.println("LIST OF ALL RESERVATION");
        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservation";

        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query))
        {
            System.out.println("+--------------------------------------------------------------------------------------+");
            System.out.println("| Reservation ID | Guest Name      | Room Number | Contact Number       | Date         |");
            System.out.println("+--------------------------------------------------------------------------------------+");
            while (resultSet.next())
            {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();
                System.out.printf("| %-14d | %-15s | %-11d | %-20s | %-12s |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("+--------------------------------------------------------------------------------------+");

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception s)
        {
            System.out.println(s.getMessage());
        }

    }



private static void getroomnumber(Connection connection,Scanner sc)
{
    System.out.println("Enter reservation id to find room details :");
    int resid=sc.nextInt();
    System.out.println("Enter Phone number to Find :");
    String guestname=sc.next();
    String query = "SELECT room_number FROM reservation " +
            "WHERE reservation_id = " + resid +
            " AND guest_name = '" + guestname + "'";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        if (rs.next()) {
            int roomNumber = rs.getInt("room_number");
            System.out.println("✅ Room Number: " + roomNumber);
        } else {
            System.out.println("❌ No matching reservation found.");
        }

    } catch (SQLException e) {
        System.out.println("⚠️ SQL Error: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("⚠️ General Error: " + e.getMessage());
    }
}
    private static void updatereservation(Connection connection, Scanner sc) {
        System.out.println("🔄 UPDATE RESERVATION DETAILS");

        System.out.print("Enter Reservation ID to update: ");
        int resid = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new Guest Name: ");
        String guestName = sc.nextLine().toUpperCase();

        System.out.print("Enter new Room Number: ");
        int roomNumber = sc.nextInt();

        System.out.print("Enter new Contact Number / Email: ");
        String contact = sc.nextLine();

        String query = "UPDATE reservation SET guest_name = '" + guestName +
                "', room_number = " + roomNumber +
                ", contact_number = '" + contact +
                "' WHERE reservation_id = " + resid;

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("✅ Reservation updated successfully!");
            } else {
                System.out.println("❌ Reservation ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ General Error: " + e.getMessage());
        }
    }

    private static void deleteregistration(Connection connection, Scanner sc) {
        System.out.println("🗑️ DELETE RESERVATION");

        System.out.print("Please Enter Reservation ID to delete: ");
        int resid = sc.nextInt();
        sc.nextLine(); // consume leftover newline

        System.out.print("Please Enter Contact Number of Customer: ");
        String contact = sc.nextLine().toUpperCase();

        String query = "DELETE FROM reservation WHERE reservation_id = " + resid +
                " AND contact_number = '" + contact + "'";

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("✅ Reservation deleted successfully!");
            } else {
                System.out.println("❌ No matching reservation found.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ General Error: " + e.getMessage());
        }
    }
}
