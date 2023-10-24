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
                 
                Random rand = new Random();

                // populate Property table 
                String[] property_ids = {"7740", "6980", "4688", "5966", "2652", "2693", "8594", "2172"};
                String[] names = {"Eastside Commons", "Oasis Lofts", "Riverfront Lofts", "Sunset Terrace", "Serenity Commons", "Joyful Apartments", "Lakeside Heaven", "Beach Bum"};
                String[] streetNums = {"9", "7", "32", "537", "881", "78", "31", "47"};
                String[] streetNames = {"Donec Street", "Eliot Road", "Tellus Ave", "Felis Road", "Blandit Street", "Diam Ave", "Velan Street", "Northside Ave"};
                String[] cities = {"Bethlehem", "Pittsburg", "Philadelphia", "San Fransisco", "New York City", "Hoboken", "Charlotte", "Jersey City"};
                String[] state = {"Pennsylvania", "Pennsylvania", "Pennsylvania", "California", "New York", "New Jersey", "North Carolina", "New Jersey"};
                String[] countries = {"US", "US", "US", "US", "US", "US", "US", "US"};
                String[] zipcodes = {"98935", "42006", "85794", "91070", "59803", "35467", "30873", "13452"};
         
                String[][] property_data = {property_ids, names, streetNums, streetNames, cities, state, countries, zipcodes};
           
            //     for(int i = 0; i < property_data.length; i++){
            //         PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into property (property_id, name, street_num, street_name, city, state, country, zipcode)"
            //         + " values (?, ?, ?, ?, ?, ?, ?, ?)");
            //         preparedStatement1.setString(1, property_data[0][i]);
            //         preparedStatement1.setString(2, property_data[1][i]);
            //         preparedStatement1.setString(3, property_data[2][i]);
            //         preparedStatement1.setString(4, property_data[3][i]);
            //         preparedStatement1.setString(5, property_data[4][i]);
            //         preparedStatement1.setString(6, property_data[5][i]);
            //         preparedStatement1.setString(7, property_data[6][i]);
            //         preparedStatement1.setString(8, property_data[7][i]);
            //         ResultSet result1 = preparedStatement1.executeQuery();
            //         result1.close();                      
            //         preparedStatement1.close();
            //    }

               // populate Amenity table 
                String[] amenity_ids = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
                String[] amenity_names = {"swimming pool", "gym", "vending machine", "sauna", "cafe", "parking", "office space", "pet walker", "cleaning", "baby sitting"};
                String[][] amenity_data = {amenity_ids, amenity_names, property_ids};
                // for(int i = 0; i < property_ids.length; i++){
                //     for(int j = 0; j < amenity_data[0].length; j++){
                //         PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into amenity (amenity_id, amenity_name, property_id)"
                //         + " values (?, ?, ?)");
                //         preparedStatement1.setString(1, amenity_data[0][j]);
                //         preparedStatement1.setString(2, amenity_data[1][j]);
                //         preparedStatement1.setString(3, amenity_data[2][i]);
                //         ResultSet result1 = preparedStatement1.executeQuery();
                //         result1.close();                      
                //         preparedStatement1.close();
                //     }
                // }

                // populate Apartment table
                String[] apartment_nums = {"516", "312", "211", "444", "323", "119", "980", "435", "983", "231", "431", "943", "111", "632", "874", "911", "321"};
                int[] apt_sizes = {1000, 1200, 1500, 1800};
                int[] bedrooms = {1, 2, 3, 4};
                double[] bathrooms = {0.5, 1, 2};
                // PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[6]);
                // preparedStatement1.setInt(2, apt_sizes[0]);
                // preparedStatement1.setInt(3, bedrooms[1]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[4]);
                // ResultSet result1 = preparedStatement1.executeQuery();

                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[7]);
                // preparedStatement1.setInt(2, apt_sizes[0]);
                // preparedStatement1.setInt(3, bedrooms[1]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[4]);
                // result1 = preparedStatement1.executeQuery();

                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[8]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[2]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[4]);
                // result1 = preparedStatement1.executeQuery();

                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[9]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[2]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[5]);
                // result1 = preparedStatement1.executeQuery();
                
                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[10]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[0]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[6]);
                // result1 = preparedStatement1.executeQuery();

                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[11]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[0]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[6]);
                // result1 = preparedStatement1.executeQuery();

                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[12]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[2]);
                // preparedStatement1.setDouble(4, bathrooms[0]);
                // preparedStatement1.setString(5, property_ids[7]);
                // result1 = preparedStatement1.executeQuery();
                
                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[13]);
                // preparedStatement1.setInt(2, apt_sizes[2]);
                // preparedStatement1.setInt(3, bedrooms[0]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[7]);
                // result1 = preparedStatement1.executeQuery();
                
                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[14]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[0]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[7]);
                // result1 = preparedStatement1.executeQuery();
                
                // preparedStatement1 = conn.prepareStatement("INSERT into apartment (apartment_num, apt_size, bedroom, bathroom, property_id)"
                // + " values (?, ?, ?, ?, ?)");
                // preparedStatement1.setString(1, apartment_nums[15]);
                // preparedStatement1.setInt(2, apt_sizes[1]);
                // preparedStatement1.setInt(3, bedrooms[2]);
                // preparedStatement1.setDouble(4, bathrooms[1]);
                // preparedStatement1.setString(5, property_ids[7]);
                // result1 = preparedStatement1.executeQuery();
                
                // // // populate Tenant table
                  String[] tenant_id = {"32124", "98743", "89023", "33213", "44312", "87890", "33412", "22341", "44213"};
                // String[] first_name = {"Bella", "Tom", "Priya", "Anna", "Mo", "Elisa", "Roy", "Udita", "Tara"};
                // String[] middle_name = {"Grace", "Junior"};
                // String[] last_name = {"Hadid", "Hanks", "Singh", "Granger", "Hilton", "Kapoor", "Long", "Agarwal", "Sukumar"};
                // // String[] tenant_streetNums = {"9", "43", "7", "32", "537", "881", "78", "31", "47"};
                // String[] tenant_streetNames = {"Monec Street", "Beliot Road", "Fellus Ave", "Hauz Khas", "Delis Road", "Handley Street", "Liam Ave", "Telan Street", "Southside Ave"};
                // String[] tenant_aptNums = {"7", "23", "17", "132", "3537", "981", "38", "41", "57"};
                // // String[] tenant_cities = {"Bethlehem", "Pittsburg", "Philadelphia", "San Fransisco", "New York City", "Hoboken", "Charlotte", "Jersey City", "San Diego"};
                // String[] tenant_state = {"Pennsylvania", "Pennsylvania", "Pennsylvania", "California", "New York", "New Jersey", "North Carolina", "New Jersey", "California"};
                // String[] tenant_countries = {"US", "US", "US", "US", "US", "US", "US", "US", "US"};
                // String[] tenant_zipcodes = {"18935", "43006", "85784", "99070", "49803", "39467", "10873", "23452", "43190"};
                // String[] tenant_phone_nums = {"9584753210", "7042198365", "5279631480", "6318402975", "8261947530", "3596821470", "1029384756", "4857261093", "6748923105"};
                // int[] tenant_ages = {23, 59, 72, 42, 34, 66, 27, 49, 53};
                // int[] tenant_credit_scores = {752, 754, 757, 758, 760, 762, 765, 768, 770};
                // String[] has_pet = {"yes", "no", "yes", "no", "yes", "yes", "no", "yes", "no"};
                // String[][] tenant_data = {tenant_ids, first_name, middle_name, last_name, tenant_aptNums, tenant_streetNums, tenant_streetNames, tenant_cities, tenant_state, tenant_countries, tenant_zipcodes, tenant_phone_nums};
                // for(int i = 3; i < tenant_ids.length; i++){
                //     PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into tenant (tenant_id, first_name, middle_name, last_name, apt_num, street_num, street_name, city, state, country, zipcode, phone_num, age, credit_score, has_pet)"
                //     + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                //     preparedStatement1.setString(1, tenant_data[0][i]);
                //     preparedStatement1.setString(2, tenant_data[1][i]);
                //     if(i == 2){
                //         preparedStatement1.setString(3, tenant_data[2][0]);
                //     } else if(i == 7){
                //         preparedStatement1.setString(3, tenant_data[2][1]);
                //     } else {
                //         preparedStatement1.setString(3, "null");
                //     }
            //         preparedStatement1.setString(4, tenant_data[3][i]);
            //         preparedStatement1.setString(5, tenant_data[4][i]);
            //         preparedStatement1.setString(6, tenant_data[5][i]);
            //         preparedStatement1.setString(7, tenant_data[6][i]);
            //         preparedStatement1.setString(8, tenant_data[7][i]);
            //         preparedStatement1.setString(9, tenant_data[8][i]);
            //         preparedStatement1.setString(10, tenant_data[9][i]);
            //         preparedStatement1.setString(11, tenant_data[10][i]);
            //         preparedStatement1.setString(12, tenant_data[11][i]);
            //         preparedStatement1.setInt(13, tenant_ages[i]);
            //         preparedStatement1.setInt(14, tenant_credit_scores[i]);
            //         preparedStatement1.setString(15, has_pet[i]);
            //         ResultSet result1 = preparedStatement1.executeQuery();
            //         result1.close();                      
            //         preparedStatement1.close();
            //    }

            //    String[] visit_ids = {"591034", "827459", "346812", "913726", "208574", "649283", "752109", "485637", "123890"};
            //    String[] visit_dates = {"25-Jan-2023", "12-Sep-2022", "07-Nov-2020", "18-May-2021", "02-Aug-2023", "14-Oct-2020", "30-Jul-2022", "09-Apr-2021", "23-Jun-2023"};
            
            //     for(int i = 0; i < visit_ids.length; i++){
            //         PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into ProspectiveTenant (visit_id, visit_date)"
            //         + " values (?, ?)");
            //         preparedStatement1.setString(1, visit_ids[i]);
            //         preparedStatement1.setString(2, visit_dates[i]);
            //         ResultSet result1 = preparedStatement1.executeQuery();
            //         result1.close();                      
            //         preparedStatement1.close();
            //    }

               String[] dependant_names = {"Jai Singh", "Tia Bhatia", "Robby Hamerick", "Wendy Zhung", "Amiya Patel", "David Yuri", "Tammy Sahr", "Shrivats Agarwal"};
              
                    PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[1]);
                    preparedStatement1.setString(2, dependant_names[0]);
                    ResultSet result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[1]);
                    preparedStatement1.setString(2, dependant_names[1]);
                    result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[2]);
                    preparedStatement1.setString(2, dependant_names[2]);
                    result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[3]);
                    preparedStatement1.setString(2, dependant_names[3]);
                    result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[2]);
                    preparedStatement1.setString(2, dependant_names[4]);
                    result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[5]);
                    preparedStatement1.setString(2, dependant_names[5]);
                    result1 = preparedStatement1.executeQuery();

                    preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[6]);
                    preparedStatement1.setString(2, dependant_names[6]);
                    result1 = preparedStatement1.executeQuery();

                     preparedStatement1 = conn.prepareStatement("INSERT into Dependant (tenant_id, dependant_name)"
                    + " values (?, ?)");
                    preparedStatement1.setString(1, tenant_id[6]);
                    preparedStatement1.setString(2, dependant_names[7]);
                    result1 = preparedStatement1.executeQuery();

                    result1.close();                      
                    preparedStatement1.close();

                   // String[] lease_ids = {"8904732", "5619285", "3472156", "6928740", "1237894", "8756291", "4569012", "2345678", "9870432"};
                    // double[] monthly_rent = {2468, 3985, 2876, 4132, 4679, 3621, 1999, 3555, 4723};
                    // int lease_term = 12;
                    // double[] security_deposit = {4936, 7970, 5752, 8264, 9358, 7242, 3998, 7110, 9446};
                    // String[] date_signed = {"18-Jul-2021", "05-Nov-2020", "12-Mar-2019", "29-Sep-2022", "07-Dec-2018", "14-May-2021", "03-Oct-2022", "22-Feb-2020", "01-Jun-2017"};
                    // String[] date_expires = {"18-Jul-2022", "05-Nov-2021", "12-Mar-2020", "29-Sep-2023", "07-Dec-2019", "14-May-2022", "03-Oct-2023", "22-Feb-2021", "01-Jun-2018"};
                    // for(int i = 0; i < lease_ids.length; i++){
                    //     PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT into Lease (lease_id, tenant_id, monthly_rent, lease_term, security_deposit, date_signed, date_expires)"
                    //     + " values (?, ?, ?, ?, ?, ?, ?)");
                    //     preparedStatement1.setString(1, lease_ids[i]);
                    //     preparedStatement1.setString(2, tenant_id[i]);
                    //     preparedStatement1.setDouble(3, monthly_rent[i]);
                    //     preparedStatement1.setInt(4, lease_term);
                    //     preparedStatement1.setDouble(5, security_deposit[i]);
                    //     preparedStatement1.setString(6, date_signed[i]);
                    //     preparedStatement1.setString(7, date_expires[i]);

                    //     ResultSet result1 = preparedStatement1.executeQuery();
                    //     result1.close();                      
                    //     preparedStatement1.close();
                    // }


                conn.close();
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }

        } while (conn == null);
    }
}
