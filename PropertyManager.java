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
            scan.nextLine();
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

        String date_expires = "";
        int month = Integer.valueOf(date_signed.substring(0, 2));
        int actual_date = Integer.valueOf(date_signed.substring(3, 5));
        int year = Integer.valueOf(date_signed.substring(6));
        String s_month = "";
        if(month < 10){
            s_month = "0" + Integer.toString(month);
        } else{
            s_month = Integer.toString(month);
        }

        date_expires = s_month + "-" + Integer.valueOf(actual_date) + "-" + Integer.valueOf(year + 1);
        System.out.println("Your lease will expire on " + date_expires);
        
        String date_move_out = "";
        String generatedColumns[] = { "lease_id" };
        int lease_id = 0;
        PreparedStatement insert_lease;
        try{
            insert_lease = conn.prepareStatement("Insert into Lease (monthly_rent, lease_term, security_deposit, date_signed, date_expires, date_move_out) values (?, ?, ?, to_date(?,'mm-dd-yyyy'), to_date(?,'mm-dd-yyyy'), ?)", generatedColumns);
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
                if(last_apartment_num_set == ""){
                    last_apartment_num_set =  apt_substr + "" + '0';
                }
                char c = last_apartment_num_set.charAt(2);
                int actual_apt_num = Character.getNumericValue(c);
                if (actual_apt_num >= 9) {
                    System.out.println("Sorry we don't have any apartments of this type available in this property at the moment. Choose a different apartment style or property or try again after a few months.");
                    return;
                } else {
                    apt_substr += (++actual_apt_num);
                    System.out.println("The assigned apartment is " + apt_substr + ".");
                }
                insert_lease.executeUpdate();
                try{
                    ResultSet generatedKeys = insert_lease.getGeneratedKeys();
                    if(generatedKeys.next()){
                        lease_id = generatedKeys.getInt(1);
                        System.out.println("The lease_id is " + lease_id + ".");
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

        System.out.println("Security deposit of $" + security_deposit + " and 1 month's rent of $" + rent + " is due today.");
        String date_paid = "";
        try{
            PreparedStatement security_deposit_pay = conn.prepareStatement("Insert into Payment (lease_id, date_due, date_paid, total_due) values (?, to_date(?,'mm-dd-yyyy'), ?,  ?)");
            security_deposit_pay.setInt(1, lease_id);
            security_deposit_pay.setString(2, date_signed);
            security_deposit_pay.setString(3, date_paid);
            security_deposit_pay.setDouble(4, security_deposit);
            security_deposit_pay.executeUpdate();                   
            security_deposit_pay.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        try{
            PreparedStatement first_month_rent_pay = conn.prepareStatement("Insert into Payment (lease_id, date_due, date_paid, total_due) values (?, to_date(?,'mm-dd-yyyy'), ?,  ?)");
            first_month_rent_pay.setInt(1, lease_id);
            first_month_rent_pay.setString(2, date_signed);
            first_month_rent_pay.setString(3, date_paid);
            first_month_rent_pay.setDouble(4, rent);
            first_month_rent_pay.executeUpdate();                   
            first_month_rent_pay.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        int month_copy_current_yr = month + 1;
        int month_copy_new_yr = 1;
        String monthly_rent_due = "";

        for(int i = 0; i < 11; i++){
            if(month_copy_current_yr <= 12){
                monthly_rent_due = Integer.valueOf(month_copy_current_yr) + "-" + "01" + "-" + Integer.valueOf(year);
                month_copy_current_yr++;
            } else if(month_copy_new_yr < month){
                monthly_rent_due = Integer.valueOf(month_copy_new_yr) + "-" + "01" + "-" + Integer.valueOf(year+1);
                month_copy_new_yr++;
            }
            try{
                PreparedStatement monthly_rent_pay = conn.prepareStatement("Insert into Payment (lease_id, date_due, date_paid, total_due) values (?, to_date(?,'mm-dd-yyyy'), ?,  ?)");
                monthly_rent_pay.setInt(1, lease_id);
                monthly_rent_pay.setString(2, monthly_rent_due);
                monthly_rent_pay.setString(3, date_paid);
                monthly_rent_pay.setDouble(4, rent);
                monthly_rent_pay.executeUpdate();                   
                monthly_rent_pay.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public void addDependant(Connection conn){
        Scanner scan = new Scanner(System.in);

         int tenant_id = 0;
        while(true){
            System.out.println("Input tenant id");
            tenant_id = scan.nextInt();
            scan.nextLine();
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
            System.out.println("Lease id is " + lease_id);
            result1.close();
            preparedStatement1.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        String date_move_out = "";
        while(true){
            System.out.println("Input move-out date in the form MM-DD-YYYY"); 
            date_move_out = scan.nextLine();
            boolean verdict = isValidDate(date_move_out);
            if (verdict) {
                break;
            } else{
                System.out.println("Invalid date format. Try again by entering a date in the form MM-DD-YYYY");
            }
        }

        try{
            PreparedStatement preparedStatement2 = conn.prepareStatement("Update Lease set date_move_out = to_date(?,'mm-dd-yyyy') WHERE lease_id=?");
            preparedStatement2.setString(1, date_move_out);
            preparedStatement2.setInt(2, lease_id);
            preparedStatement2.executeUpdate();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    // ASSUMPTION: all properties have standard 10 amenities 
    public void addAmenityToProperty(Connection conn){
        Scanner scan = new Scanner(System.in); 
        int property_id = 0;
        while(true){
            System.out.println("Enter the property number you want to add the amenity in");
            System.out.println("1 - Eastside Commons \n 2 - Oasis Lofts \n 3 - Riverfront Lofts \n 4 - Sunset Terrace \n 5 - Joyful Apartments");
            try{
                property_id = scan.nextInt();
                scan.nextLine();
                if(property_id < 1 || property_id > 5){
                    System.out.println("Please enter a valid integer number between 1 and 5");
                } else {
                    break;
                }
            } catch(InputMismatchException e){
                System.out.println("Please enter an integer value");
                scan.nextLine();
            }
        }

        // prints the amenities in that property
        String amenity_name = "";
        int amenity_id = 0;
        try{
            PreparedStatement get_property_amenity = conn.prepareStatement("select * from propertyamenities natural join amenity where property_id = ? order by amenity_id asc");
            get_property_amenity.setInt(1, property_id);
            ResultSet result = get_property_amenity.executeQuery();
            System.out.println("This property contains the following amenities");
            while(result.next()){
                amenity_id = result.getInt("amenity_id");
                amenity_name = result.getString("amenity_name");
                System.out.println(amenity_name);
            }
            result.close();
            get_property_amenity.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        // checks if this particular property has that amenity you want to add
        System.out.println("Enter the amenity you'd like to add in this property or enter 1 to return to the menu");
        String new_amenity = scan.nextLine();
        if(new_amenity.equals("1")){
            return;
        }
        try{
            PreparedStatement check_amenity_exists_in_property = conn.prepareStatement("select * from propertyamenities natural join amenity where property_id = ? and amenity_name = ?");
            check_amenity_exists_in_property.setInt(1, property_id);
            check_amenity_exists_in_property.setString(2, new_amenity);
            ResultSet result = check_amenity_exists_in_property.executeQuery();
            if(result.next()){
                System.out.println("This amenity exists already in this property.");
                return;
            } else {
                System.out.println("This amenity doesn't exist in this property.");
            }
            result.close();
            check_amenity_exists_in_property.close();
        } catch(SQLException se){
            se.printStackTrace();
        }

        // checks if the amenity you want to add exists in the amenity table
        try{
            PreparedStatement check_amenity_exists = conn.prepareStatement("select * from amenity where amenity_name = ?");
            check_amenity_exists.setString(1, new_amenity);
            ResultSet result = check_amenity_exists.executeQuery();
            // if it exists, adds that amenity directly to that property
            if(result.next()){
                amenity_id = result.getInt("amenity_id");
                try{
                    PreparedStatement insert_amenity_in_property = conn.prepareStatement("Insert into propertyamenities (property_id, amenity_id) values (?, ?)");
                    insert_amenity_in_property.setInt(1, property_id);
                    insert_amenity_in_property.setInt(2, amenity_id);
                    insert_amenity_in_property.executeUpdate();                   
                    insert_amenity_in_property.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
                System.out.println("Amenity successfully added to propetry");
                return;
            } else {
                String generatedColumns[] = { "amenity_id"};
                // inserts amenity in the amenity table
                try{
                    PreparedStatement insert_amenity_in_amenity_table = conn.prepareStatement("Insert into amenity (amenity_name) values (?)", generatedColumns);
                    insert_amenity_in_amenity_table.setString(1, new_amenity);
                    insert_amenity_in_amenity_table.executeUpdate();   
                    int new_amenity_id = 0;
                    try{
                        ResultSet generatedKeys = insert_amenity_in_amenity_table.getGeneratedKeys();
                        if(generatedKeys.next()){
                            new_amenity_id = generatedKeys.getInt(1);
                        }
                    } catch(SQLException se){
                        se.printStackTrace();
                    }
                    insert_amenity_in_amenity_table.close();
                    int amenity_type = 0;
                    String amenity_accessibility = "";
                    double monthly_cost = 0;
                    while(true){
                        System.out.println("Select if its 1 - Public Amenity \n2 - Private Amenity");
                        try{
                            amenity_type = scan.nextInt();
                                scan.nextLine();
                                // if its a public amenity, adds to public amenity table
                                if (amenity_type == 1){
                                    System.out.println("Please enter the amenity accessibility in the form {hour} AM - {hour} PM. If accessible all day, enter 24 hours");
                                    amenity_accessibility = scan.nextLine();
                                    try{
                                        PreparedStatement insert_public_amenity = conn.prepareStatement("Insert into PublicAmenity (amenity_id, accessibility) values (?, ?)");
                                        insert_public_amenity.setInt(1, new_amenity_id);
                                        insert_public_amenity.setString(2, amenity_accessibility);
                                        insert_public_amenity.executeUpdate();                   
                                        insert_public_amenity.close();
                                    } catch(SQLException se){
                                        se.printStackTrace();
                                    }
                                    try{
                                        PreparedStatement insert_amenity_in_property = conn.prepareStatement("Insert into propertyamenities (property_id, amenity_id) values (?, ?)");
                                        insert_amenity_in_property.setInt(1, property_id);
                                        insert_amenity_in_property.setInt(2, new_amenity_id);
                                        insert_amenity_in_property.executeUpdate();                   
                                        insert_amenity_in_property.close();
                                    } catch(SQLException se){
                                        se.printStackTrace();
                                    }
                                    System.out.println("Amenity successfully added to propetry");
                                    return;
                                } // if its a private amenity, adds to private amenity table
                                else if (amenity_type == 2){
                                    System.out.println("Please enter the monthly cost");
                                    while(true){
                                        try{
                                            monthly_cost = scan.nextDouble();
                                            break;
                                        } catch(InputMismatchException e){
                                            System.out.println(e.getMessage());
                                            System.out.println("Please enter numeric value");
                                            scan.next();
                                        }
                                    }
                                    try{
                                        PreparedStatement insert_private_amenity = conn.prepareStatement("Insert into PrivateAmenity (amenity_id, monthly_cost) values (?, ?)");
                                        insert_private_amenity.setInt(1, new_amenity_id);
                                        insert_private_amenity.setDouble(2, monthly_cost);
                                        insert_private_amenity.executeUpdate();                   
                                        insert_private_amenity.close();
                                    } catch(SQLException se){
                                        se.printStackTrace();
                                    }
                                    try{
                                        PreparedStatement insert_amenity_in_property = conn.prepareStatement("Insert into propertyamenities (property_id, amenity_id) values (?, ?)");
                                        insert_amenity_in_property.setInt(1, property_id);
                                        insert_amenity_in_property.setInt(2, new_amenity_id);
                                        insert_amenity_in_property.executeUpdate();                   
                                        insert_amenity_in_property.close();
                                    } catch(SQLException se){
                                        se.printStackTrace();
                                    }
                                    System.out.println("Amenity successfully added to property.");
                                    return;
                                } else {
                                    System.out.println("Please enter a valid integer number between 1 and 2");
                                }
                        } catch(InputMismatchException e){
                            System.out.println("Please enter an integer value");
                            scan.nextLine();
                        }
                    }   
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }
            result.close();
            check_amenity_exists.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }
}
