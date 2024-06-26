import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Tenant {

    public Tenant(){

    }

    // validates the date entered by user in the form MM-DD-YYYY
    public static boolean isValidDate(String date){
        try{
            if(date.length() != 10){
                return false;
            }
            
            int month = Integer.valueOf(date.substring(0, 2));
            int actual_date = Integer.valueOf(date.substring(3, 5));
            int year = Integer.valueOf(date.substring(6));
            if(month < 1 || month > 12){
                System.out.println("Invalid month. Please enter a value between 1 and 12.");
                return false;
            } else if (actual_date < 1 || actual_date > 31){
                System.out.println("Invalid date. Please enter a value between 1 and 31.");
                return false;
            } else if (year < 1900 || year > 2025){
                System.out.println("Invalid year. Please enter a value between 1900 and 2025.");
                return false;
            }

            // validates date entry for February
            if(month == 2 && actual_date > 28){
                System.out.println("Invalid date. Please enter a date between 1 - 28 for February.");
                return false;
            }

            // validates date entry for months with 30 days 
            if((month == 4 || month == 6 || month == 9 || month == 11) && actual_date > 30){
                System.out.println("Invalid date. Please enter a date between 1 - 30 for this month.");
                return false;
            }

        } catch(Exception e){
            return false;
        }
        return true;
    }

    // prints current tenant ids in the system 
    public static void print_tenant_ids(Connection conn){
        System.out.println("Current tenant ids are");
        int tenant_id = 0;
        try{
            PreparedStatement get_all_tenants = conn.prepareStatement("SELECT * from ProspectiveTenant");
            ResultSet result = get_all_tenants.executeQuery();
            while(result.next()){
                tenant_id = result.getInt("tenant_id");    
                System.out.println(tenant_id);            
            }
            result.close();
            get_all_tenants.close();
            } catch(SQLException se){
                System.out.println("Server error. Please try again later.");
                return;
            }
    }

    // prints current lease ids in the system 
    public static void print_lease_ids(Connection conn){
        System.out.println("Current lease ids are");
        int lease_id = 0;
        try{
            PreparedStatement get_all_leases = conn.prepareStatement("SELECT * from Lease");
            ResultSet result = get_all_leases.executeQuery();
            while(result.next()){
                lease_id = result.getInt("lease_id");    
                System.out.println(lease_id);            
            }
            result.close();
            get_all_leases.close();
            } catch(SQLException se){
                System.out.println("Server error. Please try again later.");
                return;
            }
    }

    // validates card expiry entered by user in the form MMYY
    public static boolean card_expiry_validated(String date){
        try{
            if(date.length() != 4){
                return false;
            }
            int month = Integer.valueOf(date.substring(0, 2));
            int year = Integer.valueOf(date.substring(2,4));
            if(date.length() != 4){
                return false;
            }
            if(month < 1 || month > 12){
                System.out.println("Invalid month. Please enter a value between 1 and 12.");
                return false;
            } 

        } catch(Exception e){
            return false;
        }
        return true;
    }

    // checks if there are any payments due 
    public static void checkPaymentStatus(Connection conn){
        print_tenant_ids(conn);
        try{
            Scanner scan = new Scanner(System.in);
            int tenant_id = 0;
            while(true){
                try{
                    System.out.println("Enter your tenant id ");
                    tenant_id = scan.nextInt();
                    scan.nextLine();
                    break;
                } catch(Exception e){
                    System.out.println("Please enter an integer value.");
                    scan.nextLine();
                }
            }
        
            String date_due = "";
            String date_paid = "";
            double total_due = 0;
            int user_wants_to_pay_now = 0;
            int invoice_num = 0;

            // validates if this tenant exists
            while(true){
                try{
                    PreparedStatement get_tenant_info = conn.prepareStatement("SELECT * from ProspectiveTenant WHERE tenant_id = ?");
                    get_tenant_info.setInt(1, tenant_id);
                    ResultSet tenant_info = get_tenant_info.executeQuery();
                    if(tenant_info.next()){
                        get_tenant_info.close();
                        tenant_info.close();
                        break;
                    } else {
                        System.out.println("Invalid tenant id. This tenant does not exist. Please enter a valid id.");
                        tenant_id = scan.nextInt();
                        scan.nextLine();
                    }
                    get_tenant_info.close();
                    tenant_info.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }

            // prompts user to enter the payment date 
            String payment_date = "";
            while(true){
                try{    
                    System.out.println("Enter today's date in the form MM-DD-YYYY");
                    payment_date = scan.nextLine();
                    boolean verdict = isValidDate(payment_date);
                    if (verdict) {
                        break;
                    } else{
                        System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
                    }
                } catch(Exception e){
                    return;
                }
            }
            String date_due_check = "";
            int month = Integer.valueOf(payment_date.substring(0, 2));
            int actual_date = Integer.valueOf(payment_date.substring(3, 5));
            int year = Integer.valueOf(payment_date.substring(6));
            HashSet<Integer> invoices_for_tenant = new HashSet<>();

            // checks payments due based on date entered by user and prints which payments are due
            try{
                PreparedStatement getAllPayments = conn.prepareStatement("SELECT * from Payment natural join tenant WHERE tenant_id = ?");
                getAllPayments.setInt(1, tenant_id);
                ResultSet payments = getAllPayments.executeQuery();
                int has_payment_due = 0;
                while(payments.next()){
                    date_due = payments.getString("date_due");
                    date_paid = payments.getString("date_paid");
                    total_due = payments.getDouble("total_due");
                    invoice_num = payments.getInt("invoice_num");
                    // checks if any payments are due based on date user entered and prints them
                    // since there may be multiple invoices due, adds them in a hashset
                    if(Integer.valueOf(date_due.substring(5, 7)) == month && date_paid == null && year == Integer.valueOf(date_due.substring(0, 4))){
                        System.out.println("Invoice: " + invoice_num + " $" + total_due + " due by " + date_due + ".");
                        invoices_for_tenant.add(invoice_num);
                        date_due_check = date_due;
                        has_payment_due = 1;
                        // if user is paying late, adds late fees 
                        if(Integer.valueOf(date_due.substring(8, 10)) < actual_date){
                            System.out.println("You are paying late. Late fee of $50 will be added to this invoice.");
                            System.out.println("New balance for invoice: " + invoice_num + " $" + (total_due+50));
                            try{
                                total_due = total_due+50;
                                PreparedStatement late_payment_update = conn.prepareStatement("Update Payment set total_due = ? WHERE invoice_num=?");
                                late_payment_update.setDouble(1, total_due);
                                late_payment_update.setInt(2, invoice_num);
                                late_payment_update.executeUpdate();
                                late_payment_update.close();
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        }
                        System.out.println();
                    }
                }
                payments.close();
                getAllPayments.close();
                
                // if user doesnt have any payments due, says that else prompts user to make payment 
                while(true){
                    try{
                        if(has_payment_due == 0){
                            System.out.println("You don't have any payments due at this time for this month. You're upto date with all your payments!");
                            break;
                        } else {
                            System.out.println("Enter 2 to make the payment now");
                            user_wants_to_pay_now = scan.nextInt();
                            scan.nextLine();
                            if(user_wants_to_pay_now == 2){
                                makeRentalPayment(conn, payment_date, invoices_for_tenant);
                                break;
                            }
                        }
                    } catch(Exception e){
                        System.out.println("Please enter an integer value.");
                        scan.nextLine();
                    }
                }
            } catch(SQLException se){
                se.printStackTrace();
            }
        } catch(Exception e){
            System.out.println("Server error. Please try again.");
            return;
        }
    }

    // allows user to make a payment 
    public static void makeRentalPayment(Connection conn, String payment_date, HashSet<Integer> invoices_for_tenant){
        // prompts user to enter the invoice number user wants to make payment for and validates it 
        try{
            Scanner scan = new Scanner(System.in);
            int invoice_num_payment_for = 0;
            while(true){
                System.out.println("Enter the invoice number you want to make the payment for");           
                try{
                    invoice_num_payment_for = scan.nextInt();
                    scan.nextLine();
                    if(!invoices_for_tenant.contains(invoice_num_payment_for)){
                        System.out.println("Please enter a valid invoice number from the following:");
                        for(int i: invoices_for_tenant){
                            System.out.println(i);
                        }
                    } else {
                        break;
                    }
                } catch(Exception e){
                    System.out.println("Please enter an integer value.");
                    scan.nextLine();
                }
            }
            
            int payment_method_choice = 0;
            while(true){
                try{
                    System.out.println("Choose your payment method. Choose 1 - Card \n 2 - Cash");
                    payment_method_choice = scan.nextInt();
                    scan.nextLine();
                    if(payment_method_choice == 1 || payment_method_choice == 2){
                        break;
                    } else {
                        System.out.println("Please enter 1 or 2.");
                    }
                } catch(Exception e){
                    System.out.println("Please enter an integer value.");
                    scan.nextLine();
                }
            }
            
            int transaction_id = 0;
            String generatedColumns[] = { "transaction_id" };
            try{
                PreparedStatement insert_payment_method = conn.prepareStatement("Insert into PaymentMethod (invoice_num) values (?)", generatedColumns);
                insert_payment_method.setInt(1, invoice_num_payment_for);
                insert_payment_method.executeUpdate();
                // card payment so prompts user to fill in required fields 
                if(payment_method_choice == 1){
                    System.out.println("Enter your 15-16 digit card number");
                    String card_num = scan.nextLine();
                    while(card_num.length() < 15 || card_num.length() > 16){
                        System.out.println("Invalid card number. Please enter a valid 15 or 16 digit number.");
                        card_num = scan.nextLine();
                    }
                    System.out.println("Enter the name on your card");
                    String card_name = scan.nextLine();
                    while (card_name.length() == 0) {
                        System.out.println("This field can't be empty. Please enter a valid value.s");
                        card_name = scan.nextLine();
                    }
                    String card_expiry = "";
                    
                    while(true){
                        System.out.println("Enter the card expiry in the form MMYY");
                        card_expiry = scan.nextLine();
                        boolean verdict = card_expiry_validated(card_expiry);
                        if (verdict) {
                            break;
                        } else{
                            System.out.println("Invalid date format. Try again by entering a date in the form MMYY");
                        }
                    }

                    try{
                        ResultSet generatedKeys = insert_payment_method.getGeneratedKeys();
                        if(generatedKeys.next()){
                            transaction_id = generatedKeys.getInt(1);
                        }
                    } catch(SQLException se){
                        System.out.println("Server error. Please try again later.");
                        return;
                    }
                    insert_payment_method.close();

                    // inserts payment details into Card table 
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
                        System.out.println("Server error. Please try again later.");
                        return;
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
                    double total_due = 0;
                    try{
                        PreparedStatement get_total_due = conn.prepareStatement("SELECT total_due from Payment WHERE invoice_num = ?");
                        get_total_due.setInt(1, invoice_num_payment_for);
                        ResultSet balance_info = get_total_due.executeQuery();
                        while(balance_info.next()){
                            total_due = balance_info.getDouble("total_due");
                        
                        } 
                        get_total_due.close();
                        balance_info.close();
                    } catch(SQLException se){
                        System.out.println("Server error. Please try again later.");
                    }

                    // prompts user to enter corresponding number of $ bills to make payment 
                    System.out.println("Total due:  $" + total_due);
                    while(true){ 
                        while(true){ 
                            System.out.println("Enter number of $100 bills");
                            try{
                                num_hundred_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }

                        while(true){ 
                            System.out.println("Enter number of $50 bills");
                            try{
                                num_fifty_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }
                        
                        while(true){ 
                            System.out.println("Enter number of $20 bills");
                            try{
                                num_twenty_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }
                        
                        while(true){ 
                            System.out.println("Enter number of $10 bills");
                            try{
                                num_ten_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }
                        
                        while(true){ 
                            System.out.println("Enter number of $5 bills");
                            try{
                                num_five_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }
                        
                        while(true){ 
                            System.out.println("Enter number of $2 bills");
                            try{
                                num_two_bills = scan.nextInt();
                                scan.nextLine();
                                break;
                            } catch(InputMismatchException e){
                                System.out.println("Please enter an integer value");
                                scan.nextLine();
                            }
                        }
                        
                        // validates that the amount entered == amount due and prints corresponding error messages
                        int amount = num_hundred_bills * 100 + num_fifty_bills * 50 + num_twenty_bills * 20 + num_ten_bills * 10 + num_five_bills * 5 + num_two_bills * 2;
                        System.out.println("Amount entered: $" + amount);
                        System.out.println("Total due:  $" + total_due);
                        if(amount == total_due){
                            break;
                        } else if (amount < total_due){
                            System.out.println("Incorrect input. The cash entered is less than the amount due. Please try again.");
                        } else {
                            System.out.println("Incorrect input. The cash entered is more than the amount due. Please try again.");
                        }
                    }
                    
                    insert_payment_method.executeUpdate();  
                    try{
                        ResultSet generatedKeys = insert_payment_method.getGeneratedKeys();
                        if(generatedKeys.next()){
                            transaction_id = generatedKeys.getInt(1);
                        }
                    } catch(SQLException se){
                        System.out.println("Server error. Please try again later.");
                        return;
                    }
                    insert_payment_method.close();
                    
                    // inserts payment details into Cash table 
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
                        System.out.println("Server error. Please try again later.");                        
                        return;
                    }
                }   

                // updates payment date 
                try{
                    PreparedStatement update_paid_date = conn.prepareStatement("Update Payment set date_paid = to_date(?,'mm-dd-yyyy') WHERE invoice_num=?");
                    update_paid_date.setString(1, payment_date);
                    update_paid_date.setInt(2, invoice_num_payment_for);
                    update_paid_date.executeUpdate();
                    update_paid_date.close();
                } catch(SQLException se){
                    return;
                }       
            } catch(SQLException se){
                return;
            }
        } catch(Exception e){
            System.out.println("Server error. Please try again.");
            return;
        }
    }

    // allows user to update their personal data 
    public static void updatePersonalData(Connection conn){
        print_tenant_ids(conn);
        try{
            Scanner scan = new Scanner(System.in);
            int tenant_id = 0;
            while(true){
                try{
                    System.out.println("Enter your tenant id ");
                    tenant_id = scan.nextInt();
                    scan.nextLine();
                    break;
                } catch(Exception e){
                    System.out.println("Please enter an integer value.");
                    scan.nextLine();
                }
            }
            String first_name = "";
            String middle_name = "";
            String last_name = "";
            String address = "";
            String city = "";
            String state = "";
            String country = "";
            String zipcode = "";
            String phone_num = "";
            String email_id =  "";
            String gender = "";
            String has_pet = "";

            while(true){
                try{
                    PreparedStatement get_tenant_info = conn.prepareStatement("SELECT * from ProspectiveTenant WHERE tenant_id = ?");
                    get_tenant_info.setInt(1, tenant_id);
                    ResultSet tenant_info = get_tenant_info.executeQuery();
                    if(tenant_info.next()){
                        first_name = tenant_info.getString("first_name");
                        middle_name = tenant_info.getString("middle_name");
                        last_name = tenant_info.getString("last_name");
                        address = tenant_info.getString("address");
                        city = tenant_info.getString("city");
                        state = tenant_info.getString("state");
                        country = tenant_info.getString("country");
                        zipcode = tenant_info.getString("zipcode");
                        phone_num = tenant_info.getString("phone_num");
                        email_id = tenant_info.getString("email");
                        gender = tenant_info.getString("gender");
                        has_pet = tenant_info.getString("has_pet");
                        get_tenant_info.close();
                        tenant_info.close();
                        break;
                    } else {
                        System.out.println("Invalid tenant id. This tenant does not exist. Please enter a valid id.");
                        tenant_id = scan.nextInt();
                        scan.nextLine();
                    }
                    get_tenant_info.close();
                    tenant_info.close();
                } catch(SQLException se){
                    System.out.println("Server error. Please try again later.");   
                }
            }
                    // prints current user information in the database 
                    System.out.println("In our system, your first name is " + first_name);
                    System.out.println("In our system, your middle name is " + middle_name);
                    System.out.println("In our system, your last name is " + last_name);
                    System.out.println("In our system, your address is " + address);
                    System.out.println("In our system, your city is " + city);
                    System.out.println("In our system, your state is " + state);
                    System.out.println("In our system, your country is " + country);
                    System.out.println("In our system, your zipcode is " +zipcode);
                    System.out.println("In our system, your phone number is " + phone_num);
                    System.out.println("In our system, your email ID is " + email_id);
                    System.out.println("In our system, your gender is " + gender);
                    System.out.println("In our system, your pet status is " + has_pet);
                    
                    // prompts user to choose what info they want to update and validates info entered 
                    // updates corresponding field in the table 
                    int choice = 0;
                    while(true){
                        System.out.println("\nEnter 1 - If you want to update your information \n2 - To return to the tenant menu");
                        try{
                            choice = scan.nextInt();
                            scan.nextLine();
                            if (choice == 1){
                                break;
                            } else if (choice == 2){
                                return;
                            } else {
                                System.out.println("Invalid input. Try again. ");
                            }
                        } catch(InputMismatchException e){
                            System.out.println("Please enter an integer value");
                            scan.nextLine();
                        }
                    }
                    while(true){
                        System.out.println("Select what information you'd like to update.\n 1 - Update first name \n 2 - Update middle name \n 3 - Update last name \n 4 - Update address \n 5 - Update city \n 6 - Update state \n 7 - Update country \n 8 - Update zipcode \n 9 - Update phone number \n 10 - Update email ID \n 11 - Update gender \n 12 - Update pet status \n 13 - Return to menu");
                        try{
                            choice = scan.nextInt();
                            scan.nextLine();
                            if (choice == 1){
                                System.out.println("Enter your updated first name");
                                first_name = scan.nextLine();
                                while(first_name.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    first_name = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_first_name = conn.prepareStatement("Update ProspectiveTenant set first_name = ? WHERE tenant_id=?");
                                    update_first_name.setString(1, first_name);
                                    update_first_name.setInt(2, tenant_id);
                                    update_first_name.executeUpdate();
                                    update_first_name.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }      
                            } else if (choice == 2){
                                System.out.println("Enter your updated middle name");
                                middle_name = scan.nextLine();
                                try{
                                    PreparedStatement update_middle_name = conn.prepareStatement("Update ProspectiveTenant set middle_name = ? WHERE tenant_id=?");
                                    update_middle_name.setString(1, middle_name);
                                    update_middle_name.setInt(2, tenant_id);
                                    update_middle_name.executeUpdate();
                                    update_middle_name.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }  
                            } else if (choice == 3){
                                System.out.println("Enter your updated last name");
                                last_name = scan.nextLine();
                                while(last_name.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    last_name = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_last_name = conn.prepareStatement("Update ProspectiveTenant set last_name = ? WHERE tenant_id=?");
                                    update_last_name.setString(1, last_name);
                                    update_last_name.setInt(2, tenant_id);
                                    update_last_name.executeUpdate();
                                    update_last_name.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 4){
                                System.out.println("Enter your updated address");
                                address = scan.nextLine();
                                while(address.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    address = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_address = conn.prepareStatement("Update ProspectiveTenant set address = ? WHERE tenant_id=?");
                                    update_address.setString(1, address);
                                    update_address.setInt(2, tenant_id);
                                    update_address.executeUpdate();
                                    update_address.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 5){
                                System.out.println("Enter your updated city");
                                city = scan.nextLine();
                                while(city.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    city = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_city = conn.prepareStatement("Update ProspectiveTenant set city = ? WHERE tenant_id=?");
                                    update_city.setString(1, city);
                                    update_city.setInt(2, tenant_id);
                                    update_city.executeUpdate();
                                    update_city.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 6){
                                System.out.println("Enter your updated state");
                                state = scan.nextLine();
                                while(state.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    state = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_state = conn.prepareStatement("Update ProspectiveTenant set state = ? WHERE tenant_id=?");
                                    update_state.setString(1, state);
                                    update_state.setInt(2, tenant_id);
                                    update_state.executeUpdate();
                                    update_state.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 7){
                                System.out.println("Enter your updated country");
                                country = scan.nextLine();
                                while(country.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    country = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_country = conn.prepareStatement("Update ProspectiveTenant set country = ? WHERE tenant_id=?");
                                    update_country.setString(1, country);
                                    update_country.setInt(2, tenant_id);
                                    update_country.executeUpdate();
                                    update_country.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 8){
                                System.out.println("Enter your updated zipcode");
                                zipcode = scan.nextLine();
                                while(zipcode.length() != 5){
                                    System.out.println("Invalid zipcode. Please enter a valid 5 digit zipcode");
                                    zipcode = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_zipcode = conn.prepareStatement("Update ProspectiveTenant set zipcode = ? WHERE tenant_id=?");
                                    update_zipcode.setString(1, zipcode);
                                    update_zipcode.setInt(2, tenant_id);
                                    update_zipcode.executeUpdate();
                                    update_zipcode.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 9){
                                System.out.println("Enter your updated phone number (without any special characters)");
                                phone_num = scan.nextLine();
                                phone_num = phone_num.replaceAll("[^0-9]","");
                                while(phone_num.length() != 10){
                                    System.out.println("Invalid phone number. Please enter a valid 10 digit number");
                                    phone_num = scan.nextLine();
                                    phone_num = phone_num.replaceAll("[^0-9]","");
                                }
                                try{
                                    PreparedStatement update_phone_num = conn.prepareStatement("Update ProspectiveTenant set phone_num = ? WHERE tenant_id=?");
                                    update_phone_num.setString(1, phone_num);
                                    update_phone_num.setInt(2, tenant_id);
                                    update_phone_num.executeUpdate();
                                    update_phone_num.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if(choice == 10){
                                System.out.println("Enter your updated email ID");
                                email_id = scan.nextLine();
                                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$"; // reference: https://www.geeksforgeeks.org/check-email-address-valid-not-java/
                                Pattern pat = Pattern.compile(emailRegex); 
                                while(!pat.matcher(email_id).matches()){
                                    System.out.println("Invalid email id. Please enter a valid email.");
                                    email_id = scan.nextLine();
                                }
                                while(email_id.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    email_id = scan.nextLine();
                                }                    
                                try{
                                    PreparedStatement update_email = conn.prepareStatement("Update ProspectiveTenant set email = ? WHERE tenant_id=?");
                                    update_email.setString(1, email_id);
                                    update_email.setInt(2, tenant_id);
                                    update_email.executeUpdate();
                                    update_email.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 11){
                                System.out.println("Enter your updated gender");
                                gender = scan.nextLine();
                                while(gender.length() == 0){ 
                                    System.out.println("This field can't be empty. Please enter a valid value.");
                                    gender = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_gender = conn.prepareStatement("Update ProspectiveTenant set gender = ? WHERE tenant_id=?");
                                    update_gender.setString(1, gender);
                                    update_gender.setInt(2, tenant_id);
                                    update_gender.executeUpdate();
                                    update_gender.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 12){
                                System.out.println("Enter your updated pet status");
                                has_pet = scan.nextLine();
                                while (true) {
                                    if(has_pet.equalsIgnoreCase("yes") || has_pet.equalsIgnoreCase("no")){
                                        break;
                                    } 
                                    System.out.println("Invalid input. Please enter yes or no");
                                    has_pet = scan.nextLine();
                                }
                                try{
                                    PreparedStatement update_pet_status = conn.prepareStatement("Update ProspectiveTenant set has_pet = ? WHERE tenant_id=?");
                                    update_pet_status.setString(1, has_pet);
                                    update_pet_status.setInt(2, tenant_id);
                                    update_pet_status.executeUpdate();
                                    update_pet_status.close();
                                    System.out.println("Update successful!");
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                                // if user updated pet_status to having a pet, generates pet fees invoice in Payments table due that day 
                                if(has_pet.equalsIgnoreCase("yes")){
                                    int lease_id = 0;
                                    try{
                                        PreparedStatement check_lease_exists = conn.prepareStatement("SELECT * from Tenant WHERE tenant_id=?");
                                        check_lease_exists.setInt(1, tenant_id);
                                        ResultSet result = check_lease_exists.executeQuery();
                                        if(result.next()){
                                            lease_id = result.getInt("lease_id");
                                            result.close();
                                            check_lease_exists.close();
                                        }
                                        result.close();
                                        check_lease_exists.close();
                                    } catch(SQLException se){
                                        se.printStackTrace();
                                    }
                                    if(lease_id != 0){
                                        System.out.println("Since you have updated your pet status to having a pet and have a lease, you will have to pay a one time pet fee of $300 due today.");
                                        String today_date = "";
                                        while(true){
                                            try{    
                                                System.out.println("Enter today's date in the form MM-DD-YYYY");
                                                today_date = scan.nextLine();
                                                boolean verdict = isValidDate(today_date);
                                                if (verdict) {
                                                    break;
                                                } else{
                                                    System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
                                                }
                                            } catch(Exception e){
                                                return;
                                            }
                                        }   
                                        try{
                                            PreparedStatement pet_fees_pay = conn.prepareStatement("Insert into Payment (lease_id, date_due, date_paid, total_due) values (?, to_date(?,'mm-dd-yyyy'), ?,  ?)");
                                            pet_fees_pay.setInt(1, lease_id);
                                            pet_fees_pay.setString(2, today_date);
                                            pet_fees_pay.setString(3, "");
                                            pet_fees_pay.setDouble(4, 300);
                                            pet_fees_pay.executeUpdate();                   
                                            pet_fees_pay.close();
                                        } catch(SQLException se){
                                            se.printStackTrace();
                                        }
                                    }            
                                }
                            } else if (choice == 13){
                                return;
                            } else {
                                System.out.println("Invalid input. Try again. ");
                            }
                        } catch(InputMismatchException e){
                            System.out.println("Please enter an integer value.");
                            scan.nextLine();
                        }
                    }
        } catch(Exception e){
            System.out.println("Server error. Please try again.");
            return;
        }
    }

    // allows user to add an amenity to their lease 
    public static void addAmenityToLease(Connection conn){
        print_lease_ids(conn);
        try{
             Scanner scan = new Scanner(System.in);
            int lease_id = 0;
            int property_id = 0;

            while(true){
                try{
                    System.out.println("Enter your lease id");
                    lease_id = scan.nextInt();
                    scan.nextLine();
                    break;
                } catch(Exception e){
                    System.out.println("Please enter an integer value.");
                    scan.nextLine();
                }
            }
            
            // checks if lease exists and gets associated property id
            while(true){  
                try{
                    PreparedStatement check_lease_exists = conn.prepareStatement("SELECT * from Lease natural join apartment WHERE lease_id=?");
                    check_lease_exists.setInt(1, lease_id);
                    ResultSet result = check_lease_exists.executeQuery();
                    if(result.next()){
                        property_id = result.getInt("property_id");
                        result.close();
                        check_lease_exists.close();
                        break;
                    } else {
                        System.out.println("Invalid lease id. This lease does not exist.");
                        lease_id = scan.nextInt();
                        scan.nextLine();
                    } 
                    result.close();
                    check_lease_exists.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }

            // prints the amenities in that property
            String amenity_name = "";
            int amenity_id = 0;
            double amenity_cost = 0;
            HashMap<String, Integer> property_amenities_set = new HashMap<>();

            try{
                PreparedStatement get_property_amenity = conn.prepareStatement("select * from propertyamenities natural join privateamenity natural join amenity where property_id = ? order by amenity_id asc");
                get_property_amenity.setInt(1, property_id);
                ResultSet result = get_property_amenity.executeQuery();
                System.out.println("This property contains the following amenities");
                while(result.next()){
                    amenity_id = result.getInt("amenity_id");
                    amenity_name = result.getString("amenity_name");
                    amenity_cost = result.getDouble("monthly_cost");
                    property_amenities_set.put(amenity_name, amenity_id);
                    System.out.println(amenity_name + " at a monthly cost of $" + amenity_cost);
                }
                result.close();
                get_property_amenity.close();
            } catch(SQLException se){
                se.printStackTrace();
            }

            // gets existing amenities added to the lease and stores in a hashset
            // prints amenities you currently have added to your lease 
            HashSet<String> existing_lease_amenities_set = new HashSet<>();
            String existing_lease_amenity = "";
            try{
                PreparedStatement check_amenity_added = conn.prepareStatement("SELECT * from leaseamenities natural join amenity WHERE lease_id= ?");
                check_amenity_added.setInt(1, lease_id);
                ResultSet result = check_amenity_added.executeQuery();
                    
                while(result.next()){
                    existing_lease_amenity = result.getString("amenity_name");
                    existing_lease_amenities_set.add(existing_lease_amenity);
                    System.out.println("You have the " + existing_lease_amenity + " amenity added to your lease already");
                }
                result.close();
                check_amenity_added.close();
            } catch(SQLException se){
                se.printStackTrace();
            }

            if(existing_lease_amenity == ""){
                System.out.println("You don't have any private amenities added to your lease currently.");
            } 

            if(existing_lease_amenities_set.size() == property_amenities_set.size()){
                System.out.println("You have all the private amenities at this property added to your lease already.");
                return;
            }
            
            // prompts user to enter amenity they'd like to add 
            // checks if user has it and if not inserts it 
            while(true){
                System.out.println("Enter the amenity name you'd like to add in your lease or enter 1 to return to the menu");
                String new_amenity = scan.nextLine();
                if(new_amenity.equalsIgnoreCase("1")){
                    return;
                }
                if(existing_lease_amenities_set.contains(new_amenity)){
                    System.out.println("Your lease has this amenity added already.");
                } else if (!property_amenities_set.containsKey(new_amenity)){
                    System.out.println("This property doesn't currently have this amenity.");
                } else {
                    try{
                        PreparedStatement new_amenity_to_lease = conn.prepareStatement("Insert into leaseamenities (lease_id ,amenity_id) values (?, ?)");
                        new_amenity_to_lease.setInt(1, lease_id);
                        new_amenity_to_lease.setInt(2, property_amenities_set.get(new_amenity));
                        new_amenity_to_lease.executeUpdate();                   
                        new_amenity_to_lease.close();
                        existing_lease_amenities_set.add(new_amenity);
                        System.out.println(new_amenity + " added successfully.");
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
                if(existing_lease_amenities_set.size() == property_amenities_set.size()){
                    System.out.println("You have all the private amenities at this property added to your lease already.");
                    return;
                }
            }
        } catch(Exception e){
            System.out.println("Server error. Please try again.");
            return;
        }
    }
}

