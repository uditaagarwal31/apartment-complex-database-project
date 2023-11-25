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
        Connection conn = DatabaseConnectionManager.connectToDatabase();
        Scanner scan = new Scanner(System.in);

        printRoleMenu(conn);
        // if(role_type == 1){  
        //     PropertyManager manager = new PropertyManager();  
        //     propertyManagerMenu(conn, manager);
        // } else if(role_type == 2){
        //     tenantMenu(conn);
        // } else if(role_type == 3){
        //     financialManagerMenu(conn);
        // } else if (role_type == 4){
        //     return;
        // }
        DatabaseConnectionManager.closeConnection();
        System.out.println("closed connection ");
        scan.close();
    }

    public static void printRoleMenu(Connection conn){
        Scanner scan = new Scanner(System.in);
        int role = 0;
        
        while(true){
            System.out.println("Welcome to Eastside Uncommons!\n Enter the option which fits your role \n 1 - Property Manager \n 2 - Tenant \n 3 - Financial Manager \n 4 - to exit the program");
            try{
                if(scan.hasNextInt()){
                    role = scan.nextInt();
                    if(role < 0 && role > 4){
                        System.out.println("Please enter a valid integer number between 1 and 4");
                        role = scan.nextInt();
                    } else {
                        break;
                    }
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
            
        if(role == 1){  
            PropertyManager manager = new PropertyManager();  
            propertyManagerMenu(conn, manager);
        } else if(role == 2){
            Tenant tenant = new Tenant();
            tenantMenu(conn, tenant);
        } else if(role == 3){
            financialManagerMenu(conn);
        } else if (role == 4){
            System.exit(0);
        }
        return;
    }

    public static void propertyManagerMenu(Connection conn, PropertyManager manager){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi property manager!\n Choose the task you want to accomplish today \n 1 - Record visitor data \n 2 - Record lease data \n 3 - Set move-out date \n 4 - Add dependant to a lease \n 5 - To return to the main menu \n 6 - To exit the program");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 7){
                        if(option == 1){
                            manager.recordVistorData(conn);
                        } else if (option == 2){
                            manager.recordLeaseData(conn);
                        } else if (option == 3){
                            manager.set_move_out_date(conn);
                        } else if(option == 4){
                            manager.addDependant(conn);
                        } else if (option == 5){
                            printRoleMenu(conn);
                        } else if (option == 6){
                            System.exit(0);
                        }
                    } else {
                        System.out.println("Please enter a valid integer number between 1 and 6");
                        option = scan.nextInt();
                    } 
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }

    


    public static void tenantMenu(Connection conn, Tenant tenant){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi tenant!\n Choose the task you want to accomplish today \n 1 - Check payment status and make rental payment \n 2 - Update personal data \n 3 - Add amenity to your lease \n 4 - To return to the main menu \n 5 - To exit the program");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option == 1) {
                        tenant.checkPaymentStatus(conn);
                    } else if (option == 2){
                        tenant.updatePersonalData(conn);
                    } else if (option == 3){
                        tenant.addAmenityToLease(conn);
                    } else if (option == 4){
                        printRoleMenu(conn);
                    } else if (option == 5){
                        System.exit(0);
                    }
                } else {
                    System.out.println("Please enter a valid integer number between 1 and 5");
                    option = scan.nextInt();
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }
    

    public static void financialManagerMenu(Connection conn){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi financial manager!\n Choose the task you want to accomplish today \n 1 - Generate financial report \n 2 - To return to the main menu \n 3 - To exit the program");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if (option == 1){
                        System.out.println("financial report");
                    } else if (option == 2){
                        printRoleMenu(conn);
                    } else if (option == 3){
                        System.exit(0);
                    }
                } else {
                    System.out.println("Please enter a valid integer number between 1 and 3");
                    option = scan.nextInt();
                }
            } catch(InputMismatchException e){
                scan.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please enter an integer value");
            }
        }
    }


}
