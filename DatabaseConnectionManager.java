import java.sql.*;
import java.util.*;

public class DatabaseConnectionManager {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
    static Connection conn = null;

    public static Connection connectToDatabase(){
        
        Scanner scan = new Scanner(System.in);
        do{
            try{
                // connect to db 
            //    System.out.println("Enter Oracle user id: ");
            //    String user = scan.nextLine();
            //    System.out.println("Enter Oracle user password: ");
            //    String pass = scan.nextLine();
                conn = DriverManager.getConnection(DB_URL, "uda224", "Golgappa/1234");
             //  conn = DriverManager.getConnection(DB_URL, user,pass);
                System.out.println("yay connected");
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }
        } while (conn == null);

        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
    }

    
}
