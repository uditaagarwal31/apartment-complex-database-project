//package 241-final-project;
import java.sql.*;
import java.util.*;

public class PopulateData {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    // OH QUESTIONS
    // 1. tenant select prepared statement not working for record visitor data & add dependant
    // 2. what exactly do you mean by record lease data? show the available apartments such as 2 bedroom, etc and the property manager can choose
    // in record moveout do i just drop that row containing that lease_id from apartment - no don't drop
    // 3. is it okay if i don't consider pets as tenants? yes 
    // 4. i'm not really using amenities here 
    // property manager can add amenities. you list existing ones & can add. for tenant tenant can add private amenities 


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
            System.out.println("Hi property manager!\n Choose the task you want to accomplish today \n 1 - Record visitor data \n 2 - Record lease data \n 3 - Set move-out date \n 4 - Add dependant to a lease \n ");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 6){
                        if(option == 1){
                            recordVistorData(conn);
                        } else if (option == 2){
                            recordLeaseData(conn);
                        } else if (option == 3){
                            set_move_out_date(conn);
                        } else if(option == 4){
                            addDependant(conn);
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
        int tenant_id = 0;
        String generatedColumns[] = { "tenant_id" };
        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement insert_tenant = conn.prepareStatement("Insert into Tenant (first_name, middle_name, last_name, address, city, state, country, zipcode, phone_num, email, age, gender, credit_score, has_pet) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", generatedColumns);
            insert_tenant.setString(1, first_name);
            insert_tenant.setString(2, middle_name);
            insert_tenant.setString(3, last_name);
            insert_tenant.setString(4, address);
            insert_tenant.setString(5, city);
            insert_tenant.setString(6, state);
            insert_tenant.setString(7, country);
            insert_tenant.setString(8, zipcode);
            insert_tenant.setString(9, phone_number);
            insert_tenant.setString(10, email);
            insert_tenant.setInt(11, age);
            insert_tenant.setString(12, gender);
            insert_tenant.setInt(13, credit_score);
            insert_tenant.setString(14, has_pet);
            insert_tenant.executeUpdate();                   
            
            try{
                ResultSet generatedKeys = insert_tenant.getGeneratedKeys();
                if(generatedKeys.next()){
                    tenant_id = generatedKeys.getInt(1);
                }
            } catch(SQLException se){
                se.printStackTrace();
            }
            insert_tenant.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        System.out.println("Input visit date in the form DD-MMM-YYYY: ");
        String date = scan.nextLine();
        try{ 
            PreparedStatement insert_prospective_tenant = conn.prepareStatement("Insert into ProspectiveTenant (tenant_id, visit_date) values (?,?)");
            insert_prospective_tenant.setInt(1, tenant_id);
            insert_prospective_tenant.setString(2, date);
            insert_prospective_tenant.executeUpdate();                   
            insert_prospective_tenant.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    // OH: WHAT EXACTLY DO YOU MEAN BY RECORD LEASE DATA?
    public static void recordLeaseData(Connection conn){
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose which apartment style you'd like. All leases are for 12 months. \n 1 - 1 bedroom & 1 bathroom apartment with 1200 sq feet, base rent $1200 per month and security deposit $2400 \n 2 - 2 bedroom & 1 bathroom apartment with 1400 sq feet, base rent $1800 per month and security deposit $3600 \n 3 - 3 bedroom & 2 bathroom apartment with 1600 sq feet, base rent $2400 per month and security deposit $4800 \n 4 - 4 bedroom & 2 bathroom apartment with 1800 sq feet, base rent $2800 per month and security deposit $5600");
        int apartment_style_choice = scan.nextInt();
        scan.nextLine();
        double rent = 0;
        int lease_term = 12;
        double security_deposit = 0;
        int apartment_size = 0;
        int num_bathrooms = 0;
        int num_bedrooms = 0;

        switch (apartment_style_choice) {
            case 1:
                rent = 1200;
                security_deposit = 2400;
                apartment_size = 1200;
                num_bathrooms = 1;
                num_bedrooms = 1;
                break;
            case 2:
                rent = 1800;
                security_deposit = 3600;
                apartment_size = 1400;
                num_bathrooms = 1;
                num_bedrooms = 2;
                break;
            case 3:
                rent = 2400;
                security_deposit = 4800;
                apartment_size = 1600;
                num_bathrooms = 2;
                num_bedrooms = 3;
                break;
            case 4:
                rent = 2800;
                security_deposit = 5600;
                apartment_size = 1800;
                num_bathrooms = 2;
                num_bedrooms = 4;
                break;
            default:
                break;
        }

        System.out.println("Input lease date signed in the form DD-MMM-YYYY: ");
        String date_signed = scan.nextLine();

        System.out.println("Input lease date expires in the form DD-MMM-YYYY: ");
        String date_expires = scan.nextLine();

        System.out.println("Input move-out date if the tenant is super proactive and has that figured out already in the form DD-MMM-YYYY: "); // OH: WHAT TO DO HERE?
        String date_move_out = scan.nextLine();       
        String generatedColumns[] = { "lease_id" };
        int lease_id = 0;
        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement insert_lease = conn.prepareStatement("Insert into Lease (monthly_rent, lease_term, security_deposit, date_signed, date_expires, date_move_out) values (?, ?, ?, ?, ?, ?)", generatedColumns);
            insert_lease.setDouble(1, rent);
            insert_lease.setInt(2, lease_term);
            insert_lease.setDouble(3, security_deposit);
            insert_lease.setString(4, date_signed);
            insert_lease.setString(5, date_expires);
            insert_lease.setString(6, date_move_out);
            insert_lease.executeUpdate();
             try{
                ResultSet generatedKeys = insert_lease.getGeneratedKeys();
                if(generatedKeys.next()){
                    lease_id = generatedKeys.getInt(1);
                    System.out.println("lease_id here" + lease_id);
                }
            } catch(SQLException se){
                se.printStackTrace();
            }                   
            insert_lease.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        System.out.println("Input apartment number associated with this lease");
        String apartment_num = scan.nextLine();

        // gets property_id
        System.out.println("Input property name");
        String property_name = scan.nextLine();
        int property_id = 0;
        try{
            PreparedStatement get_property_id = conn.prepareStatement("SELECT property_id from Property where name = ?");
            get_property_id.setString(1, property_name);
            ResultSet rs = get_property_id.executeQuery();
            while (rs.next()) {
                property_id = rs.getInt("property_id");
            }
        } catch(SQLException se){
            se.printStackTrace();
        }


        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement preparedStatement2 = conn.prepareStatement("Insert into Apartment (apartment_num, apt_size, bedroom, bathroom, property_id, lease_id) values (?, ?, ?, ?, ?, ?)");
            preparedStatement2.setString(1, apartment_num);
            preparedStatement2.setInt(2, apartment_size);
            preparedStatement2.setInt(3, num_bedrooms);
            preparedStatement2.setInt(4, num_bathrooms);
            preparedStatement2.setInt(5, property_id);
            preparedStatement2.setInt(6, lease_id); 
            preparedStatement2.executeUpdate();                   
            preparedStatement2.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        // TO DO: replace w tenant name 
        System.out.println("Input tenant id");
        int tenant_id = scan.nextInt();
        scan.nextLine(); // to consume \n char 
        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement preparedStatement3 = conn.prepareStatement("Insert into has_lease (lease_id ,tenant_id) values (?, ?)");
            preparedStatement3.setInt(1, lease_id);
            preparedStatement3.setInt(2, tenant_id);
            preparedStatement3.executeUpdate();                   
            preparedStatement3.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

    }

    public static void addDependant(Connection conn){
        Scanner scan = new Scanner(System.in);

        System.out.println("Input tenant id");
        int tenant_id = scan.nextInt();
        scan.nextLine();

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
