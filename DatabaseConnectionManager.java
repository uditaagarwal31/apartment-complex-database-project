import java.sql.*;
import java.util.*;

public class DatabaseConnectionManager {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
    static Connection conn = null;

    // connect to db 
    public static Connection connectToDatabase(){
        Scanner scan = new Scanner(System.in);
        do{
            try{
                System.out.println("Enter Oracle user id: ");
                String user = scan.nextLine();
                System.out.println("Enter Oracle user password: ");
                String pass = scan.nextLine();
                conn = DriverManager.getConnection(DB_URL, user,pass);
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }
        } while (conn == null);
        return conn;
    }

    // closes connection
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
