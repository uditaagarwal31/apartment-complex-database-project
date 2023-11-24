import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PropertyManager {
    
    public PropertyManager(){

    }

    // add which months are valid for which dates 
    public static boolean isValidDate(String date){
        if(date.length() != 10){
            return false;
        }
        // MM-DD-YYYY
        int month = Integer.valueOf(date.substring(0, 2));
        System.out.println("month " + month);
        int actual_date = Integer.valueOf(date.substring(3, 5));
        System.out.println("actual date " + actual_date);
        int year = Integer.valueOf(date.substring(6));
        System.out.println("year " + year);
        if(month < 1 || month > 12){
            System.out.println("Invalid month. Please enter a value between 1 and 12");
            return false;
        } else if (actual_date < 1 || actual_date > 31){
            System.out.println("Invalid date. Please enter a value between 1 and 31");
            return false;
        } else if (year < 1900 || year > 2025){
            System.out.println("Invalid year. Please enter a value between 1900 and 2025");
            return false;
        }
        return true;
    }

    public void recordVistorData(Connection conn){
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
        while(zipcode.length() != 5){
            System.out.println("Invalid zipcode. Please enter a valid 5 digit zipcode");
            zipcode = scan.nextLine();
        }

        System.out.println("Input prospective tenant's phone number (without any special characters)");
        String phone_number = scan.nextLine();
        phone_number = phone_number.replaceAll("[^0-9]","");
        while(phone_number.length() != 10){
            System.out.println("Invalid phone number. Please enter a valid 10 digit number");
            phone_number = scan.nextLine();
            phone_number = phone_number.replaceAll("[^0-9]","");
        }

        System.out.println("Input prospective tenant's email");
        String email = scan.nextLine();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$"; // reference: https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        Pattern pat = Pattern.compile(emailRegex); 
        while(!pat.matcher(email).matches()){
            System.out.println("Invalid email id. Please enter a valid email.");
            email = scan.nextLine();
        }

        System.out.println("Input prospective tenant's age");
        int age = scan.nextInt();
        scan.nextLine(); // to consume \n char 
        if(age < 18){
            System.out.println("You must be above the age of 18 to be eligible to live here. Please try again after a couple of years.");
            return;
        }

        System.out.println("Input prospective tenant's gender");
        String gender = scan.nextLine();

        System.out.println("Input prospective tenant's credit score");
        int credit_score = scan.nextInt();
        scan.nextLine(); // to consume \n char 
        while(credit_score < 300 || credit_score > 850){
            System.out.println("Invalid score. Please enter a value between 300 - 850");
            credit_score = scan.nextInt();
        }
        if(credit_score < 720){
            System.out.println("Your score has to be above 720 to be eligible to live here. Please try again after you've met our requirement.");
            return;
        }

        System.out.println("Input yes or no if the prospective tenant has a pet or not");
        String has_pet = scan.nextLine();
        while (true) {
            if(has_pet.equalsIgnoreCase("yes") || has_pet.equalsIgnoreCase("no")){
                break;
            } 
            System.out.println("Invalid input. Please enter yes or no");
            has_pet = scan.nextLine();
        }

        String visit_date = "";
        while(true){
            System.out.println("Input visit date in the form MM-DD-YYYY");
            visit_date = scan.nextLine();
            boolean verdict = isValidDate(visit_date);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }
        
        int tenant_id = 0;
        String generatedColumns[] = { "tenant_id" };
        try{
            // TO DO: ERROR HANDLING 
            PreparedStatement insert_prospective_tenant = conn.prepareStatement("Insert into ProspectiveTenant (first_name, middle_name, last_name, address, city, state, country, zipcode, phone_num, email, age, gender, credit_score, has_pet, visit_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'mm-dd-yyyy'))", generatedColumns);
            insert_prospective_tenant.setString(1, first_name);
            insert_prospective_tenant.setString(2, middle_name);
            insert_prospective_tenant.setString(3, last_name);
            insert_prospective_tenant.setString(4, address);
            insert_prospective_tenant.setString(5, city);
            insert_prospective_tenant.setString(6, state);
            insert_prospective_tenant.setString(7, country);
            insert_prospective_tenant.setString(8, zipcode);
            insert_prospective_tenant.setString(9, phone_number);
            insert_prospective_tenant.setString(10, email);
            insert_prospective_tenant.setInt(11, age);
            insert_prospective_tenant.setString(12, gender);
            insert_prospective_tenant.setInt(13, credit_score);
            insert_prospective_tenant.setString(14, has_pet);
            insert_prospective_tenant.setString(15, visit_date);
            insert_prospective_tenant.executeUpdate();                   
            
            try{
                ResultSet generatedKeys = insert_prospective_tenant.getGeneratedKeys();
                if(generatedKeys.next()){
                    tenant_id = generatedKeys.getInt(1);
                }
                System.out.println("The tenant id is " + tenant_id);
            } catch(SQLException se){
                se.printStackTrace();
            }
            insert_prospective_tenant.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    public void recordLeaseData(Connection conn){
        Scanner scan = new Scanner(System.in);
        int tenant_id = 0;
        while(true){
            System.out.println("Input tenant id");
            tenant_id = scan.nextInt();
            try{
                PreparedStatement check_tenant_exists = conn.prepareStatement("SELECT * from ProspectiveTenant WHERE tenant_id=?");
                check_tenant_exists.setInt(1, tenant_id);
                ResultSet result = check_tenant_exists.executeQuery();
                
                if(!result.next()){
                    System.out.println("Invalid tenant id. This tenant does not exist. Please enter a valid id.");
                } else{
                    break;
                }
                result.close();
                check_tenant_exists.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        


        // TO DO: DEBUG 
        // System.out.println("Input tenant's first name'");
        // String first_name = scan.nextLine();
        // System.out.println("Input tenant's last name'");
        // String last_name = scan.nextLine();
        // int tenant_id = 0;
       
        // try{
        //     PreparedStatement get_tenant_id = conn.prepareStatement("SELECT tenant_id from ProspectiveTenant WHERE first_name=? and last_name = ?");
        //     get_tenant_id.setString(1, first_name);
        //     get_tenant_id.setString(2, last_name);
        //     ResultSet result = get_tenant_id.executeQuery();
            
        //     while(result.next()){
        //         tenant_id = result.getInt("tenant_id");
        //         last_name = result.getString("last_name");
        //         System.out.println("in " + tenant_id);
        //     }
        //     System.out.println("tenant_id " + tenant_id);
        //     result.close();
        //     get_tenant_id.close();
        // } catch(SQLException se){
        //     se.printStackTrace();
        // }


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

        String date_signed = "";
        while(true){
            System.out.println("Input lease date signed in the form MM-DD-YYYY");
            date_signed = scan.nextLine();
            boolean verdict = isValidDate(date_signed);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }

        // TO DO: automatically generate expiry date 
        String date_expires = "";
        while(true){
            System.out.println("Input lease date expires in the form MM-DD-YYYY");
            date_expires = scan.nextLine();
            boolean verdict = isValidDate(date_expires);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }
        
        // TO DO: set move out date to null here 
        String date_move_out = "";
        while(true){
            System.out.println("Input move-out date if the tenant is super proactive and has that figured out already in the form MM-DD-YYYY"); 
            date_move_out = scan.nextLine();
            boolean verdict = isValidDate(date_move_out);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }

        String generatedColumns[] = { "lease_id" };
        int lease_id = 0;
        PreparedStatement insert_lease;
        try{
            // TO DO: ERROR HANDLING 
            insert_lease = conn.prepareStatement("Insert into Lease (monthly_rent, lease_term, security_deposit, date_signed, date_expires, date_move_out) values (?, ?, ?, to_date(?,'mm-dd-yyyy'), to_date(?,'mm-dd-yyyy'), to_date(?,'mm-dd-yyyy'))", generatedColumns);
            insert_lease.setDouble(1, rent);
            insert_lease.setInt(2, lease_term);
            insert_lease.setDouble(3, security_deposit);
            insert_lease.setString(4, date_signed);
            insert_lease.setString(5, date_expires);
            insert_lease.setString(6, date_move_out);
            System.out.println("Input property id which corresponds to the property name of your choice \n 1 - Eastside Commons \n 2 - Oasis Lofts \n 3 - Riverfront Lofts \n 4 - Sunset Terrace \n 5 - Joyful Apartments");
            int property_id = scan.nextInt();
            scan.nextLine();
            String apt_substr = property_id + "" + num_bedrooms + "";
            String last_apartment_num_set = "";
            try{
                PreparedStatement calculate_apt_num = conn.prepareStatement("select apartment_num from apartment where apartment_num LIKE ? ");
                calculate_apt_num.setString(1,apt_substr + "%");
                ResultSet result = calculate_apt_num.executeQuery();
                
                while(result.next()){
                    last_apartment_num_set = result.getString("apartment_num");
                }
                char c = last_apartment_num_set.charAt(2);
                int actual_apt_num = Character.getNumericValue(c);
                if (actual_apt_num >= 5) {
                    System.out.println("Sorry we don't have any apartments of this type available in this property at the moment. Choose a different apartment style or property or try again after a few months");
                } else {
                    apt_substr += (++actual_apt_num);
                    System.out.println("The assigned apartment is " + apt_substr);
                }
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
                try{
                    PreparedStatement preparedStatement2 = conn.prepareStatement("Insert into Apartment (apartment_num, apt_size, bedroom, bathroom, property_id, lease_id) values (?, ?, ?, ?, ?, ?)");
                    preparedStatement2.setString(1, apt_substr);
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
            } catch(SQLException se){
                se.printStackTrace();
            }
        } catch(SQLException se){
            se.printStackTrace();
        }

        try{
            PreparedStatement preparedStatement3 = conn.prepareStatement("Insert into tenant (lease_id ,tenant_id) values (?, ?)");
            preparedStatement3.setInt(1, lease_id);
            preparedStatement3.setInt(2, tenant_id);
            preparedStatement3.executeUpdate();                   
            preparedStatement3.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    public void addDependant(Connection conn){
        Scanner scan = new Scanner(System.in);

         int tenant_id = 0;
        while(true){
            System.out.println("Input tenant id");
            tenant_id = scan.nextInt();
            try{
                PreparedStatement check_tenant_exists = conn.prepareStatement("SELECT * from ProspectiveTenant WHERE tenant_id=?");
                check_tenant_exists.setInt(1, tenant_id);
                ResultSet result = check_tenant_exists.executeQuery();
                
                if(!result.next()){
                    System.out.println("Invalid tenant id. This tenant does not exist. Please enter a valid id.");
                } else{
                    break;
                }
                result.close();
                check_tenant_exists.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
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

    public void set_move_out_date(Connection conn){
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

        String date_move_out = "";
        while(true){
            System.out.println("Input move-out date if the tenant is super proactive and has that figured out already in the form MM-DD-YYYY"); 
            date_move_out = scan.nextLine();
            boolean verdict = isValidDate(date_move_out);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }

        try{
            
            PreparedStatement preparedStatement2 = conn.prepareStatement("Update Lease set date_move_out = ? WHERE lease_id=?");
            preparedStatement2.setString(1, date_move_out);
            preparedStatement2.setInt(2, lease_id);
            preparedStatement2.executeUpdate();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }
}

// TO DO: add exit to main menu
// TO DO: add exit to leave program