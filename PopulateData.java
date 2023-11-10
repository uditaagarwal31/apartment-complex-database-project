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
                
                int role_type = printRoleMenu();
                if(role_type == 1){
                    propertyManagerMenu();
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

    public static void propertyManagerMenu(){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi property manager!\n Choose the task you want to accomplish today \n 1 - Record visitor data \n 2 - Record lease data \n 3 - Record move out \n 4 - Add dependant to a lease \n 5- Set move-out date");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 6){
                        break;
                    }
                    System.out.println("Please enter a valid integer number between 1 and 5");
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

}
