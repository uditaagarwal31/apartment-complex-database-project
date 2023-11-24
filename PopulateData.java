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

        int role_type = printRoleMenu();
        if(role_type == 1){  
            PropertyManager manager = new PropertyManager();  
            propertyManagerMenu(conn, manager);
        } else if(role_type == 2){
            tenantMenu(conn);
        } else if(role_type == 3){
            financialManagerMenu(conn);
        }
        DatabaseConnectionManager.closeConnection();
        System.out.println("closed connection ");
        scan.close();
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

    public static void propertyManagerMenu(Connection conn, PropertyManager manager){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi property manager!\n Choose the task you want to accomplish today \n 1 - Record visitor data \n 2 - Record lease data \n 3 - Set move-out date \n 4 - Add dependant to a lease \n ");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option > 0 && option < 6){
                        if(option == 1){
                            manager.recordVistorData(conn);
                        } else if (option == 2){
                            manager.recordLeaseData(conn);
                        } else if (option == 3){
                            manager.set_move_out_date(conn);
                        } else if(option == 4){
                            manager.addDependant(conn);
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

    


    public static void tenantMenu(Connection conn){
        int option;
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi tenant!\n Choose the task you want to accomplish today \n 1 - Check payment status and make rental payment \n 2 - Update personal data \n 3 - Add amenity to your lease");
            try{
                if(scan.hasNextInt()){
                    option = scan.nextInt();
                    if(option == 1) {
                        checkPaymentStatus(conn);
                    } else if (option == 2){
                        updatePersonalData(conn);
                    } else if (option == 3){
                        addAmenityToLease(conn);
                    } else{
                        System.out.println("Please enter a valid integer number between 1 and 3");
                    }
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




    public static void checkPaymentStatus(Connection conn){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your tenant id");
        int tenant_id = scan.nextInt();
        String date_due = "";
        String date_paid = "";
        double total_due = 0;
        int has_payment_due = 0;
        int user_wants_to_pay_now = 0;
        int invoice_num = 0;
        
        try{
            PreparedStatement getAllPayments = conn.prepareStatement("SELECT * from Payment natural join tenant WHERE tenant_id = ?");
            getAllPayments.setInt(1, tenant_id);
            ResultSet payments = getAllPayments.executeQuery();
            
            while(payments.next()){
                date_due = payments.getString("date_due");
                date_paid = payments.getString("date_paid");
                total_due = payments.getDouble("total_due");
                invoice_num = payments.getInt("invoice_num");
                has_payment_due = 1;
            }
            
            if(date_paid != null){
                System.out.println("You don't have any payments due at this time. You're upto date with all your payments!");
            } else {
                System.out.println("You have $" + total_due + " due by " + date_due + ".");
                System.out.println("Enter 2 to make the payment now");
                user_wants_to_pay_now = scan.nextInt();
                if(user_wants_to_pay_now == 2){
                    makeRentalPayment(conn, invoice_num, total_due);
                }
            }
            payments.close();
            getAllPayments.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    public static void makeRentalPayment(Connection conn, int invoice_num, double total_due){
        Scanner scan = new Scanner(System.in);
        if(invoice_num == 0 || total_due == 0){
            System.out.println("Enter your tenant id");
            int tenant_id = scan.nextInt();
            try{
                PreparedStatement getAllPayments = conn.prepareStatement("SELECT * from Payment natural join tenant WHERE tenant_id = ?");
                getAllPayments.setInt(1, tenant_id);
                ResultSet payments = getAllPayments.executeQuery();
            
                while(payments.next()){                  
                    invoice_num = payments.getInt("invoice_num");
                    total_due = payments.getDouble("total_due");
                }
                payments.close();
                getAllPayments.close();
            } catch(SQLException se){
            se.printStackTrace();
            }
        } 
        System.out.println("Enter today's date in the form DD-MMM-YYYY:");
        String payment_date = scan.nextLine();
        System.out.println("Choose your payment method. Choose 1 - Card \n 2 - Cash");
        int payment_method_choice = scan.nextInt();
        scan.nextLine();
        int transaction_id = 0;
        String generatedColumns[] = { "transaction_id" };
        try{
            PreparedStatement insert_payment_method = conn.prepareStatement("Insert into PaymentMethod (invoice_num) values (?)", generatedColumns);
            insert_payment_method.setInt(1, invoice_num);
             // card
            if(payment_method_choice == 1){
                System.out.println("Enter your card number");
                String card_num = scan.nextLine();
                System.out.println("Enter the name on your card");
                String card_name = scan.nextLine();
                System.out.println("Enter the card expiry in the form MMYY");
                String card_expiry = scan.nextLine();
                insert_payment_method.executeUpdate();  
                try{
                    ResultSet generatedKeys = insert_payment_method.getGeneratedKeys();
                    if(generatedKeys.next()){
                        transaction_id = generatedKeys.getInt(1);
                    }
                } catch(SQLException se){
                    se.printStackTrace();
                }
                insert_payment_method.close();

                try{
                    PreparedStatement insert_card = conn.prepareStatement("Insert into Card (transaction_id, card_num, card_name, expiry) values (?, ?, ?, ?)");
                    insert_card.setInt(1, transaction_id);
                    insert_card.setString(2, card_num);
                    insert_card.setString(3, card_name);
                    insert_card.setString(4, card_expiry);
                    insert_card.executeUpdate();                   
                    insert_card.close();
                    System.out.println("Payment successful!");
                } catch(SQLException se){
                    se.printStackTrace();
                }

            } 
            // cash
            else if(payment_method_choice == 2){
                int num_hundred_bills = 0;
                int num_fifty_bills = 0;
                int num_twenty_bills = 0;
                int num_ten_bills = 0;
                int num_five_bills = 0;
                int num_two_bills = 0;

                while(true){
                    System.out.println("Enter number of $100 bills");
                    num_hundred_bills = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter number of $50 bills");
                    num_fifty_bills = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter number of $20 bills");
                    num_twenty_bills = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter number of $10 bills");
                    num_ten_bills = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter number of $5 bills");
                    num_five_bills = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter number of $2 bills");
                    num_two_bills = scan.nextInt();
                    scan.nextLine();
                    int amount = num_hundred_bills * 100 + num_fifty_bills * 50 + num_twenty_bills * 20 + num_ten_bills * 10 + num_five_bills * 5 + num_two_bills * 2;
                    if(amount == total_due){
                        break;
                    } else if (amount < total_due){
                        System.out.println("Incorrect input. The cash entered is less than the amount due. Please try again");
                    } else {
                        System.out.println("Incorrect input. The cash entered is more than the amount due. Please try again");
                    }
                }
                
                insert_payment_method.executeUpdate();  
                try{
                    ResultSet generatedKeys = insert_payment_method.getGeneratedKeys();
                    if(generatedKeys.next()){
                        transaction_id = generatedKeys.getInt(1);
                    }
                } catch(SQLException se){
                    se.printStackTrace();
                }
                insert_payment_method.close();
        
                try{
                    PreparedStatement insert_cash = conn.prepareStatement("Insert into Cash (transaction_id, num_hundred_bills, num_fifty_bills, num_twenty_bills, num_ten_bills, num_five_bills, num_two_bills) values (?, ?, ?, ?, ?, ?, ?)");
                    insert_cash.setInt(1, transaction_id);
                    insert_cash.setInt(2, num_hundred_bills);
                    insert_cash.setInt(3, num_fifty_bills);
                    insert_cash.setInt(4, num_twenty_bills);
                    insert_cash.setInt(5, num_ten_bills);
                    insert_cash.setInt(6, num_five_bills);
                    insert_cash.setInt(7, num_two_bills);  
                    insert_cash.executeUpdate();                   
                    insert_cash.close();
                    System.out.println("Payment successful!");
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }   

            try{
                PreparedStatement update_paid_date = conn.prepareStatement("Update Payment set date_paid = ? WHERE invoice_num=?");
                update_paid_date.setString(1, payment_date);
                update_paid_date.setInt(2, invoice_num);
                update_paid_date.executeUpdate();
                update_paid_date.close();
            } catch(SQLException se){
                se.printStackTrace();
            }       
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    public static void updatePersonalData(Connection conn){
        System.out.println("hi ");
    }

    public static void addAmenityToLease(Connection conn){
        System.out.println("hi ");
    }

}
