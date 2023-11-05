//package 241-final-project;
import java.sql.*;
import java.util.*;

public class PopulateData {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    public static void main(String[] args){
        Connection conn = null;
        Scanner scan = new Scanner(System.in);
        do{
            try{
                // connect to db 
                System.out.println("Enter Oracle user id: ");
                String user = scan.nextLine();
                System.out.println("Enter Oracle user password: ");
                String pass = scan.nextLine();
                conn = DriverManager.getConnection(DB_URL, user, pass);
                System.out.println("yay connected");
                
                 
                


         

                conn.close();
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }

        } while (conn == null);
    }
}
