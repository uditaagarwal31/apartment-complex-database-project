//package 241-final-project;
import java.sql.*;
import java.util.*;

public class PopulateData {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    // OH QUESTIONS
    // 1. tenant select prepared statement not working for record visitor data & add dependant
    // 2. what exactly do you mean by record lease data?
    // 3. is it okay if i don't consider pets as tenants?
    // 4. is record move out done correctly?
    // 5. i'm not really using amenities here 
    public static void main(String[] args){
        Connection conn = null;
        Scanner scan = new Scanner(System.in);
        do{
            try{
                // connect to db 
             //   System.out.println("Enter Oracle user id: ");
             //   String user = scan.nextLine();
              //  System.out.println("Enter Oracle user password: ");
               // String pass = scan.nextLine();
                conn = DriverManager.getConnection(DB_URL, "uda224", "Golgappa/1234");
                System.out.println("yay connected");
                
                int role_type = printRoleMenu();
                if(role_type == 1){
                    propertyManagerMenu(conn);
                } else if(role_type == 2){
                    tenantMenu();
                } else if(role_type == 3){
                    financialManagerMenu();
                }

                conn.close();
            } catch(SQLException se){
                se.printStackTrace();
                System.out.println("Connect Error. Re-enter login details ");
            }

        } while (conn == null);
    }

    public static int printRoleMenu(){
        Scanner scan = new Scanner(System.in);
        int role;
        
        while(true){
            System.out.println("Welcome to Eastside Uncommons!\n Enter the option which fits your role \n 1 - Property Manager \n 2 - Tenant \n 3 - Financial Manager");
            try{
                if(scan.hasNextInt()){
                    role = scan.nextInt();
                    if(role > 0 && role < 4){
                        break;
                    }
                    System.out.println("Please enter a valid integer number between 1 and 3");
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
        return role;
    }

    public static void propertyManagerMenu(Connection conn){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi property manager!\n Choose the task you want to accomplish today \n 1 - Record visitor data \n 2 - Record lease data \n 3 - Record move out \n 4 - Add dependant to a lease \n 5- Set move-out date");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 6){
                        if(option == 1){
                            recordVistorData(conn);
                        } else if (option == 2){
                            recordLeaseData(conn);
                        } else if (option == 3){
                            recordMoveOut(conn);
                        } else if(option == 4){
                            addDependant(conn);
                        } else if (option == 5){
                            set_move_out_date(conn);
                        }
                    }
                 //   System.out.println("Please enter a valid integer number between 1 and 5");
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }


    public static void tenantMenu(){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi tenant!\n Choose the task you want to accomplish today \n 1 - Check payment status \n 2 - Make rental payment \n 3 - Update personal data");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 4){
                        break;
                    }
                    System.out.println("Please enter a valid integer number between 1 and 3");
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }

    public static void financialManagerMenu(){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi financial manager!\n Choose the task you want to accomplish today \n 1 - Generate financial report");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 2){
                        break;
                    }
                    System.out.println("Please enter a valid integer number between 1 and 1 lol");
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }

    public static void recordVistorData(Connection conn){
        Scanner scan = new Scanner(System.in);

        System.out.println("Input prospective tenant's first name");
        String first_name = scan.nextLine();

        System.out.println("Input prospective middle name");
        String middle_name = scan.nextLine();

        System.out.println("Input prospective tenant's last name");
        String last_name = scan.nextLine();

        System.out.println("Input prospective tenant's address");
        String address = scan.nextLine();

        System.out.println("Input prospective tenant's city");
        String city = scan.nextLine();

        System.out.println("Input prospective tenant's state");
        String state = scan.nextLine();

        System.out.println("Input prospective tenant's country");
        String country = scan.nextLine();

        System.out.println("Input prospective tenant's zipcode");
        String zipcode = scan.nextLine();

        System.out.println("Input prospective tenant's phone number");
        String phone_number = scan.nextLine();

        System.out.println("Input prospective tenant's email");
        String email = scan.nextLine();

        System.out.println("Input prospective tenant's age");
        int age = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input prospective tenant's gender");
        String gender = scan.nextLine();

        System.out.println("Input prospective tenant's credit score");
        int credit_score = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input yes or no if the prospective tenant has a pet or not");
        String has_pet = scan.nextLine();

        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement preparedStatement1 = conn.prepareStatement("Insert into Tenant (first_name, middle_name, last_name, address, city, state, country, zipcode, phone_num, email, age, gender, credit_score, has_pet) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement1.setString(1, first_name);
            preparedStatement1.setString(2, middle_name);
            preparedStatement1.setString(3, last_name);
            preparedStatement1.setString(4, address);
            preparedStatement1.setString(5, city);
            preparedStatement1.setString(6, state);
            preparedStatement1.setString(7, country);
            preparedStatement1.setString(8, zipcode);
            preparedStatement1.setString(9, phone_number);
            preparedStatement1.setString(10, email);
            preparedStatement1.setInt(11, age);
            preparedStatement1.setString(12, gender);
            preparedStatement1.setInt(13, credit_score);
            preparedStatement1.setString(14, has_pet);
            preparedStatement1.executeUpdate();                   
            preparedStatement1.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        System.out.println("Input visit date in the form DD-MMM-YYYY: ");
        String date = scan.nextLine();
        try{
            // TO DO: ERROR HANDLING 
            // OH: THIS QUERY WORKS IN SQL BUT NOT HERE WHY ??
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT tenant_id from Tenant where first_name = ? AND last_name = ? AND address = ? AND phone_num = ?");
        // PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from Tenant"); // OH: THIS WORKS! BUT ABOVE DOESN'T 
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone_number);
            ResultSet rs = preparedStatement.executeQuery();
            int tenant_id = 0;
            System.out.println("outside while loop before");
            while(rs.next()){
                System.out.println("inside while loop");
                tenant_id = rs.getInt("tenant_id"); 
                System.out.println(tenant_id + "tenant_id");
            }
            System.out.println("after while loop before");
            PreparedStatement preparedStatement2 = conn.prepareStatement("Insert into ProspectiveTenant (tenant_id, visit_date) values (?,?)");
            preparedStatement2.setInt(1, tenant_id);
            preparedStatement2.setString(2, date);
            preparedStatement2.executeUpdate();                   
            preparedStatement2.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    // OH: WHAT EXACTLY DO YOU MEAN BY RECORD LEASE DATA?
    public static void recordLeaseData(Connection conn){
        Scanner scan = new Scanner(System.in);

        System.out.println("Input lease's monthly rent");
        double rent = scan.nextDouble();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input lease term in months");
        int lease_term = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input lease's security deposit");
        double security_deposit = scan.nextDouble();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input lease date signed in the form DD-MMM-YYYY: ");
        String date_signed = scan.nextLine();

        System.out.println("Input lease date expires in the form DD-MMM-YYYY: ");
        String date_expires = scan.nextLine();

        System.out.println("Input move-out date if any in the form DD-MMM-YYYY: "); // OH: WHAT TO DO HERE?
        String date_move_out = scan.nextLine();       

        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement preparedStatement1 = conn.prepareStatement("Insert into Lease (monthly_rent, lease_term, security_deposit, date_signed, date_expires, date_move_out) values (?, ?, ?, ?, ?, ?)");
            preparedStatement1.setDouble(1, rent);
            preparedStatement1.setInt(2, lease_term);
            preparedStatement1.setDouble(3, security_deposit);
            preparedStatement1.setString(4, date_signed);
            preparedStatement1.setString(5, date_expires);
            preparedStatement1.setString(6, date_move_out);
            preparedStatement1.executeUpdate();                   
            preparedStatement1.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        System.out.println("Input apartment number associated with this lease");
        String apartment_num = scan.nextLine();

        System.out.println("Input apartment size in sq feet");
        int size = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input number of bedrooms in the apartment");
        int num_bedrooms = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        System.out.println("Input number of bathrooms in the apartment");
        int num_bathrooms = scan.nextInt();
        scan.nextLine(); // to consume \n char 

        // gets property_id
        System.out.println("Input property name");
        String property_name = scan.nextLine();
        int property_id = 0;
        try{
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT property_id from Property where name = ?");
            preparedStatement.setString(1, property_name);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                property_id = rs.getInt("property_id");
            }
        } catch(SQLException se){
            se.printStackTrace();
        }

        // gets lease_id
        int lease_id = 0;
        try{
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT lease_id from Lease where monthly_rent = ? AND lease_term = ? AND security_deposit = ? AND date_signed = ? AND date_expires = ?");
            preparedStatement.setDouble(1, rent);
            preparedStatement.setInt(2, lease_term);
            preparedStatement.setDouble(3, security_deposit);
            preparedStatement.setString(4, date_signed);
            preparedStatement.setString(5, date_expires);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                lease_id = rs.getInt("lease_id");
            }
        } catch(SQLException se){
            se.printStackTrace();
        }

        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement preparedStatement2 = conn.prepareStatement("Insert into Apartment (apartment_num, apt_size, bedroom, bathroom, property_id, lease_id) values (?, ?, ?, ?, ?, ?)");
            preparedStatement2.setString(1, apartment_num);
            preparedStatement2.setInt(2, size);
            preparedStatement2.setInt(3, num_bedrooms);
            preparedStatement2.setInt(4, num_bathrooms);
            preparedStatement2.setInt(5, property_id);
            preparedStatement2.setInt(6, lease_id); 
            preparedStatement2.executeUpdate();                   
            preparedStatement2.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    // OH: IS THIS CORRECT?
    public static void recordMoveOut(Connection conn){
        Scanner scan = new Scanner(System.in);

        System.out.println("Input lease id");
        String lease_id = scan.nextLine();
        try{
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM has_lease WHERE lease_id = ?");
            preparedStatement1.setString(1, lease_id);
            preparedStatement1.executeUpdate();                   
            preparedStatement1.close();
            PreparedStatement preparedStatement2 = conn.prepareStatement("DELETE FROM apartment WHERE lease_id = ?");
            preparedStatement2.setString(1, lease_id);
            preparedStatement2.executeUpdate();                   
            preparedStatement2.close();
            PreparedStatement preparedStatement3 = conn.prepareStatement("DELETE FROM lease WHERE lease_id = ?");
            preparedStatement3.setString(1, lease_id);
            preparedStatement3.executeUpdate();                   
            preparedStatement3.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

    }

    // OH: WHY NOT WORKING??
    public static void addDependant(Connection conn){
        Scanner scan = new Scanner(System.in);

        System.out.println("Input tenant first name");
        String first_name = scan.nextLine();

        System.out.println("Input tenant last name");
        String last_name = scan.nextLine();
        int tenant_id = 0;
        try{
            // OH: PREPARED STATEMENT NOT WORKING 
            PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT tenant_id from tenant WHERE first_name=? AND last_name = ?");
            preparedStatement1.setString(1, first_name);
            preparedStatement1.setString(2, last_name);
            ResultSet result1 = preparedStatement1.executeQuery();
            
            while(result1.next()){
                tenant_id = result1.getInt("tenant_id");
            }
            System.out.println("tenant_id " + tenant_id);
            result1.close();
            preparedStatement1.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    
        System.out.println("Input dependant's first name");
        String dependant_first_name = scan.nextLine();

        System.out.println("Input dependant's last name");
        String dependant_last_name = scan.nextLine();

        try{
            PreparedStatement preparedStatement2 = conn.prepareStatement("Insert into Dependant (tenant_id, first_name, last_name) values (?, ?, ?)");
            preparedStatement2.setInt(1, tenant_id);
            preparedStatement2.setString(2, dependant_first_name);
            preparedStatement2.setString(3, dependant_last_name);
            preparedStatement2.executeUpdate();                   
            preparedStatement2.close();

        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    public static void set_move_out_date(Connection conn){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the apartment number associated with move out");
        String apartment_num = scan.nextLine();
        int lease_id = 0;
        try{
            PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT lease_id from Apartment WHERE apartment_num=?");
            preparedStatement1.setString(1, apartment_num);
            ResultSet result1 = preparedStatement1.executeQuery();
            
            while(result1.next()){
                lease_id = result1.getInt("lease_id");
            }
            System.out.println("tenant_id " + lease_id);
            result1.close();
            preparedStatement1.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        try{
            System.out.println("Enter the move out date");
            String date_move_out = scan.nextLine();
            PreparedStatement preparedStatement2 = conn.prepareStatement("Update Lease set date_move_out = ? WHERE lease_id=?");
            preparedStatement2.setString(1, date_move_out);
            preparedStatement2.setInt(2, lease_id);
            preparedStatement2.executeUpdate();
        } catch(SQLException se){
            se.printStackTrace();
        }

    }
}
