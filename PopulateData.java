//package 241-final-project;
import java.sql.*;
import java.util.*;
import java.util.Random;

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
                 
                Random rand = new Random();

                // populate Property table 
                String[] property_ids = new String[8];
                
                for(int i = 0; i < property_ids.length; i++){
                    property_ids[i] = String.valueOf(rand.nextInt(10000));
                }
                String[] names = {"Eastside Commons", "Oasis Lofts", "Riverfront Lofts", "Sunset Terrace", "Serenity Commons", "Joyful Apartments", "Lakeside Heaven", "Beach Bum"};
                String[] streetNums = {"9", "7", "32", "537", "881", "78", "31", "47"};
                String[] streetNames = {"Donec Street", "Eliot Road", "Tellus Ave", "Felis Road", "Blandit Street", "Diam Ave", "Velan Street", "Northside Ave"};
                String[] cities = {"Bethlehem", "Pittsburg", "Philadelphia", "San Fransisco", "New York City", "Hoboken", "Charlotte", "Jersey City"};
                String[] state = {"Pennsylvania", "Pennsylvania", "Pennsylvania", "California", "New York", "New Jersey", "North Carolina", "New Jersey"};
                String[] countries = {"US", "US", "US", "US", "US", "US", "US", "US"};
                String[] zipcodes = new String[8];
               
                for(int i = 0; i < zipcodes.length; i++){
                    zipcodes[i] = String.valueOf(rand.nextInt(100000));
                }

                String[][] data = {property_ids, names, streetNums, streetNames, cities, state, countries, zipcodes};
           
                for(int i = 0; i < data.length; i++){
                    PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into property (property_id, name, street_num, street_name, city, state, country, zipcode)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)");
                    preparedStatement1.setString(1, data[0][i]);
                    preparedStatement1.setString(2, data[1][i]);
                    preparedStatement1.setString(3, data[2][i]);
                    preparedStatement1.setString(4, data[3][i]);
                    preparedStatement1.setString(5, data[4][i]);
                    preparedStatement1.setString(6, data[5][i]);
                    preparedStatement1.setString(7, data[6][i]);
                    preparedStatement1.setString(8, data[7][i]);
                    ResultSet result1 = preparedStatement1.executeQuery();
                    result1.close();                      
                    preparedStatement1.close();
               }

            conn.close();
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }

        } while (conn == null);
    }
}
