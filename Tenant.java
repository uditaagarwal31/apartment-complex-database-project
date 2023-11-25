import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Tenant {

    public Tenant(){

    }

    public static boolean isValidDate(String date){
        if(date.length() != 10){
            return false;
        }
        // MM-DD-YYYY
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
        return true;
    }

    public static boolean card_expiry_validated(String date){
        // MMYY
        int month = Integer.valueOf(date.substring(0, 2));
        int year = Integer.valueOf(date.substring(2,4));
        if(date.length() != 4){
            return false;
        }
        if(month < 1 || month > 12){
            System.out.println("Invalid month. Please enter a value between 1 and 12.");
            return false;
        } 
        return true;
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
            return;
            }
        } 
        
        String payment_date = "";
        while(true){
            System.out.println("Enter today's date in the form MM-DD-YYYY");
            payment_date = scan.nextLine();
            boolean verdict = isValidDate(payment_date);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }
        System.out.println("Choose your payment method. Choose 1 - Card \n 2 - Cash");
        int payment_method_choice = scan.nextInt();
        scan.nextLine();
        int transaction_id = 0;
        String generatedColumns[] = { "transaction_id" };
        try{
            PreparedStatement insert_payment_method = conn.prepareStatement("Insert into PaymentMethod (invoice_num) values (?)", generatedColumns);
            insert_payment_method.setInt(1, invoice_num);
            insert_payment_method.executeUpdate();
             // card
            if(payment_method_choice == 1){
                System.out.println("Enter your 15-16 digit card number");
                String card_num = scan.nextLine();
                while(card_num.length() < 15 || card_num.length() > 16){
                    System.out.println("Invalid card number. Please enter a valid 15 or 16 digit number.");
                    card_num = scan.nextLine();
                }
                System.out.println("Enter the name on your card");
                String card_name = scan.nextLine();
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
                    se.printStackTrace();
                    return;
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
                    return;
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
                    return;
                }
            }   

            try{
                PreparedStatement update_paid_date = conn.prepareStatement("Update Payment set date_paid = to_date(?,'mm-dd-yyyy') WHERE invoice_num=?");
                update_paid_date.setString(1, payment_date);
                update_paid_date.setInt(2, invoice_num);
                update_paid_date.executeUpdate();
                update_paid_date.close();
            } catch(SQLException se){
                se.printStackTrace();
                return;
            }       
        } catch(SQLException se){
            se.printStackTrace();
            return;
        }
    }

    public static void updatePersonalData(Connection conn){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your tenant id");
        int tenant_id = scan.nextInt();
        scan.nextLine();
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
                se.printStackTrace();
            }
        }

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
                

                int choice = 0;
                while(true){
                    System.out.println("\nEnter 1 - If you want to update your information \n2 - To return to the tenant menu");
                    try{
                        if(scan.hasNextInt()){
                            choice = scan.nextInt();
                            if (choice == 1){
                                break;
                            } else if (choice == 2){
                                return;
                            } 
                        } else {
                            System.out.println("Invalid input. Try again. ");
                            choice = scan.nextInt();
                        }
                    } catch(InputMismatchException e){
                        scan.nextLine();
                        System.out.println(e.getMessage());
                        System.out.println("Please enter an integer value");
                    }
                }
                System.out.println("yo");
                while(true){
                    System.out.println("Select what information you'd like to update.\n 1 - Update first name \n 2 - Update middle name \n 3 - Update last name \n 4 - Update address \n 5 - Update city \n 6 - Update state \n 7 - Update country \n 8 - Update zipcode \n 9 - Update phone number \n 10 - Update email ID \n 11 - Update gender \n 12 - Update pet status \n 13 - Return to menu");
                    try{
                        if(scan.hasNextInt()){
                            choice = scan.nextInt();
                            scan.nextLine();
                            if (choice == 1){
                                System.out.println("Enter your updated first name");
                                first_name = scan.nextLine();
                                try{
                                    PreparedStatement update_first_name = conn.prepareStatement("Update ProspectiveTenant set first_name = ? WHERE tenant_id=?");
                                    update_first_name.setString(1, first_name);
                                    update_first_name.setInt(2, tenant_id);
                                    update_first_name.executeUpdate();
                                    update_first_name.close();
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
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }  
                            } else if (choice == 3){
                                System.out.println("Enter your updated last name");
                                last_name = scan.nextLine();
                                try{
                                    PreparedStatement update_last_name = conn.prepareStatement("Update ProspectiveTenant set last_name = ? WHERE tenant_id=?");
                                    update_last_name.setString(1, last_name);
                                    update_last_name.setInt(2, tenant_id);
                                    update_last_name.executeUpdate();
                                    update_last_name.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 4){
                                System.out.println("Enter your updated address");
                                address = scan.nextLine();
                                try{
                                    PreparedStatement update_address = conn.prepareStatement("Update ProspectiveTenant set address = ? WHERE tenant_id=?");
                                    update_address.setString(1, address);
                                    update_address.setInt(2, tenant_id);
                                    update_address.executeUpdate();
                                    update_address.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 5){
                                System.out.println("Enter your updated city");
                                city = scan.nextLine();
                                try{
                                    PreparedStatement update_city = conn.prepareStatement("Update ProspectiveTenant set city = ? WHERE tenant_id=?");
                                    update_city.setString(1, city);
                                    update_city.setInt(2, tenant_id);
                                    update_city.executeUpdate();
                                    update_city.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 6){
                                System.out.println("Enter your updated state");
                                state = scan.nextLine();
                                try{
                                    PreparedStatement update_state = conn.prepareStatement("Update ProspectiveTenant set state = ? WHERE tenant_id=?");
                                    update_state.setString(1, state);
                                    update_state.setInt(2, tenant_id);
                                    update_state.executeUpdate();
                                    update_state.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 7){
                                System.out.println("Enter your updated country");
                                country = scan.nextLine();
                                try{
                                    PreparedStatement update_country = conn.prepareStatement("Update ProspectiveTenant set country = ? WHERE tenant_id=?");
                                    update_country.setString(1, country);
                                    update_country.setInt(2, tenant_id);
                                    update_country.executeUpdate();
                                    update_country.close();
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
                                try{
                                    PreparedStatement update_email = conn.prepareStatement("Update ProspectiveTenant set email = ? WHERE tenant_id=?");
                                    update_email.setString(1, email_id);
                                    update_email.setInt(2, tenant_id);
                                    update_email.executeUpdate();
                                    update_email.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 11){
                                System.out.println("Enter your updated gender");
                                gender = scan.nextLine();
                                try{
                                    PreparedStatement update_gender = conn.prepareStatement("Update ProspectiveTenant set gender = ? WHERE tenant_id=?");
                                    update_gender.setString(1, gender);
                                    update_gender.setInt(2, tenant_id);
                                    update_gender.executeUpdate();
                                    update_gender.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            }
                            else if (choice == 12){
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
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }
                            } else if (choice == 13){
                                return;
                            }
                        } else {
                            System.out.println("Invalid input. Try again. ");
                            choice = scan.nextInt();
                        }
                    } catch(InputMismatchException e){
                        scan.nextLine();
                        System.out.println(e.getMessage());
                        System.out.println("Please enter an integer value");
                    }
                } 
            
        

    }

    public static void addAmenityToLease(Connection conn){
        Scanner scan = new Scanner(System.in);
        int lease_id = 0;
        
        while(true){
            System.out.println("Enter your lease id");
            lease_id = scan.nextInt();
            scan.nextLine();
            try{
                PreparedStatement check_lease_exists = conn.prepareStatement("SELECT * from Lease WHERE lease_id=?");
                check_lease_exists.setInt(1, lease_id);
                ResultSet result = check_lease_exists.executeQuery();
                
                if(!result.next()){
                    System.out.println("Invalid lease id. This lease does not exist. Please enter a valid id.");
                } else{
                    break;
                }
                result.close();
                check_lease_exists.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }

        int has_pet_walker = 0;
        int has_cleaning = 0;
        int has_babysitting = 0;

        try{
            PreparedStatement check_amenity_added = conn.prepareStatement("SELECT * from leaseamenities WHERE lease_id=?");
            check_amenity_added.setInt(1, lease_id);
            ResultSet result = check_amenity_added.executeQuery();
                
            while(result.next()){
                int amenity_existing = result.getInt("amenity_id");
                if(amenity_existing == 8){
                    System.out.println("You have the pet walker amenity added to your lease already");
                    has_pet_walker = 1;
                } else if(amenity_existing == 9){
                    System.out.println("You have the cleaning services amenity added to your lease already");
                    has_cleaning = 1;
                } else if(amenity_existing == 10){
                    System.out.println("You have the baby sitting amenity added to your lease already");
                    has_babysitting = 1;
                }
            }
            result.close();
            check_amenity_added.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        
        int choice = 0;
        while (true) {
            System.out.println("Enter the private amenity you want to add to your lease \n1 - Pet Walker at $200 per month \n2 - Cleaning Service at $400 per month \n3 - Babysitting service at $600 per month \n4 - To return to the menu");
            try{
                if(scan.hasNextInt()){
                    choice = scan.nextInt();
                    scan.nextLine();
                    if(choice == 1){
                        if(has_pet_walker == 0){
                            try{
                                PreparedStatement add_petwalker = conn.prepareStatement("Insert into leaseamenities (lease_id ,amenity_id) values (?, ?)");
                                add_petwalker.setInt(1, lease_id);
                                add_petwalker.setInt(2, 8);
                                add_petwalker.executeUpdate();                   
                                add_petwalker.close();
                                has_pet_walker = 1;
                                System.out.println("Pet walking amenity successfully added!");
                                double rent = 0;
                                try{
                                    PreparedStatement get_monthly_rent = conn.prepareStatement("SELECT total_due from payment WHERE lease_id=?");
                                    get_monthly_rent.setInt(1, lease_id);
                                    ResultSet result = get_monthly_rent.executeQuery();
                                    while (result.next()){
                                        rent = result.getDouble("total_due");
                                    }
                                    result.close();
                                    get_monthly_rent.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   

                                try{
                                    PreparedStatement update_payments_due = conn.prepareStatement("Update Payment set total_due = ? WHERE lease_id=? and date_paid is NULL");
                                    update_payments_due.setDouble(1, rent + 200);
                                    update_payments_due.setInt(2, lease_id);
                                    update_payments_due.executeUpdate();
                                    update_payments_due.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        } else {
                            System.out.println("You already have this amenity added to your lease.");
                        }    
                    } else if (choice == 2){
                        if(has_cleaning == 0){
                            try{
                                PreparedStatement add_cleaning = conn.prepareStatement("Insert into leaseamenities (lease_id ,amenity_id) values (?, ?)");
                                add_cleaning.setInt(1, lease_id);
                                add_cleaning.setInt(2, 9);
                                add_cleaning.executeUpdate();                   
                                add_cleaning.close();
                                has_cleaning = 1;
                                System.out.println("Cleaning amenity successfully added!");
                                double rent = 0;
                                try{
                                    PreparedStatement get_monthly_rent = conn.prepareStatement("SELECT total_due from payment WHERE lease_id=?");
                                    get_monthly_rent.setInt(1, lease_id);
                                    ResultSet result = get_monthly_rent.executeQuery();
                                    while (result.next()){
                                        rent = result.getDouble("total_due");
                                    }
                                    result.close();
                                    get_monthly_rent.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   

                                try{
                                    PreparedStatement update_payments_due = conn.prepareStatement("Update Payment set total_due = ? WHERE lease_id=? and date_paid is NULL");
                                    update_payments_due.setDouble(1, rent + 400);
                                    update_payments_due.setInt(2, lease_id);
                                    update_payments_due.executeUpdate();
                                    update_payments_due.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        } else {
                            System.out.println("You already have this amenity added to your lease.");
                        }      
                    } else if (choice == 3){
                        if(has_babysitting == 0){
                            try{
                                PreparedStatement add_babysitting = conn.prepareStatement("Insert into leaseamenities (lease_id ,amenity_id) values (?, ?)");
                                add_babysitting.setInt(1, lease_id);
                                add_babysitting.setInt(2, 10);
                                add_babysitting.executeUpdate();                   
                                add_babysitting.close();
                                has_babysitting = 1;
                                System.out.println("Baby sitting amenity successfully added!");
                                double rent = 0;
                                try{
                                    PreparedStatement get_monthly_rent = conn.prepareStatement("SELECT total_due from payment WHERE lease_id=?");
                                    get_monthly_rent.setInt(1, lease_id);
                                    ResultSet result = get_monthly_rent.executeQuery();
                                    while (result.next()){
                                        rent = result.getDouble("total_due");
                                    }
                                    result.close();
                                    get_monthly_rent.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   

                                try{
                                    PreparedStatement update_payments_due = conn.prepareStatement("Update Payment set total_due = ? WHERE lease_id=? and date_paid is NULL");
                                    update_payments_due.setDouble(1, rent + 600);
                                    update_payments_due.setInt(2, lease_id);
                                    update_payments_due.executeUpdate();
                                    update_payments_due.close();
                                } catch(SQLException se){
                                    se.printStackTrace();
                                }   
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        } else {
                            System.out.println("You already have this amenity added to your lease.");
                        }  
                    } else if(choice == 4){
                    break;
                }
            } else {
                System.out.println("Please enter a valid integer number between 1 and 4");
                choice = scan.nextInt();
            }
        } catch(InputMismatchException e){
            scan.nextLine();
            System.out.println(e.getMessage());
            System.out.println("Please enter an integer value");
        }
    }
        
    }
    
}


// TO DO: validate cash inputs 
// TO DO: check move out date set between lease expiry & sign date
// TO DO: check if even 1 paid_date isn't null, it still shows that u have payments due
// TO DO: add unique constraint to transaction_id/invoice_num 