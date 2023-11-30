import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;

public class FinancialManager {

    public FinancialManager(){

    }

    // for each property-id & get occupancy of 1BD, 2BR, 3BR, 4BR apts ; total revenue, total profit  
    public static void financialReport(Connection conn){
        try{
            HashMap<Double, String> financial_analysis = new HashMap<>();
            String[] property_names = {"Eastside Commons", "Oasis Lofts", "Riverfront Lofts", "Sunset Terrace", "Joyful Apartments"};
            double[] property_profits = new double[5];
            int total_occupancy = 0;
            int one_BR = 0;
            int two_BR = 0;
            int three_BR = 0;
            int four_BR = 0;
            int lease_id = 0;

            for(int i = 1; i < 6; i++){
                System.out.println(property_names[i-1]);
                HashSet<Integer> one_br_set = new HashSet<>();
                HashSet<Integer> two_br_set = new HashSet<>();
                HashSet<Integer> three_br_set = new HashSet<>();
                HashSet<Integer> four_br_set = new HashSet<>();
                double one_br_revenue = 0;
                double two_br_revenue = 0;
                double three_br_revenue = 0;
                double four_br_revenue = 0;
                double one_br_profits = 0;
                double two_br_profits = 0;
                double three_br_profits = 0;
                double four_br_profits = 0;
                
                try{
                    PreparedStatement get_total_occupancy = conn.prepareStatement("select count(*) from apartment where property_id = ?");
                    get_total_occupancy.setInt(1, i);
                    ResultSet result1 = get_total_occupancy.executeQuery();
                    while(result1.next()){
                        total_occupancy = result1.getInt(1);
                    }
                    if(total_occupancy == 0){
                        System.out.println("There aren't any active leases currently");
                    } else {
                        System.out.println("There are " + total_occupancy + " active leases currently");
                        total_occupancy = 0;
                        try{
                            PreparedStatement get_1BR = conn.prepareStatement("select lease_id from apartment where property_id = ? and bedroom = ?");
                            get_1BR.setInt(1, i);
                            get_1BR.setInt(2, 1);
                            ResultSet result2 = get_1BR.executeQuery();
                            while(result2.next()){
                                lease_id = result2.getInt("lease_id");
                                one_br_set.add(lease_id);
                            }
                            System.out.println("Total 1 bedroom apartments occupied: " + one_br_set.size());
                            System.out.println("Total 1 bedroom apartments available: " + (9 - one_br_set.size()));
                            
                            result2.close();
                            get_1BR.close();
                        } catch(SQLException se){
                            se.printStackTrace();
                        }
                        
                        for(int l: one_br_set){
                            try{
                                PreparedStatement get_revenue = conn.prepareStatement("select total_due from payment where lease_id = ?");
                                get_revenue.setInt(1, l);
                                ResultSet result_revenue = get_revenue.executeQuery();
                                while(result_revenue.next()){
                                    double current_revenue = result_revenue.getDouble("total_due");
                                    one_br_revenue = one_br_revenue + current_revenue;
                                }
                                get_revenue.close();
                                result_revenue.close();
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        }
                        one_br_profits = one_br_revenue - one_br_set.size() * 700;
                        System.out.println("Anticipated total revenue from the current active 1 bedroom leases: $" + one_br_revenue);
                        System.out.println("Anticipated profits: $" + one_br_profits);
                        System.out.println();

                        try{
                            PreparedStatement get_2BR = conn.prepareStatement("select lease_id from apartment where property_id = ? and bedroom = ?");
                            get_2BR.setInt(1, i);
                            get_2BR.setInt(2, 2);
                            ResultSet result3 = get_2BR.executeQuery();
                            while(result3.next()){
                                lease_id = result3.getInt("lease_id");
                                two_br_set.add(lease_id);
                            }
                            System.out.println("Total 2 bedroom apartments occupied: " + two_br_set.size());
                            System.out.println("Total 2 bedroom apartments available: " + (9 - two_br_set.size()));
                            result3.close();
                            get_2BR.close();
                        } catch(SQLException se){
                            se.printStackTrace();
                        }

                        for(int l: two_br_set){
                            try{
                                PreparedStatement get_revenue = conn.prepareStatement("select total_due from payment where lease_id = ?");
                                get_revenue.setInt(1, l);
                                ResultSet result_revenue = get_revenue.executeQuery();
                                while(result_revenue.next()){
                                    double current_revenue = result_revenue.getDouble("total_due");
                                    two_br_revenue = two_br_revenue + current_revenue;
                                }
                                get_revenue.close();
                                result_revenue.close();
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        }
                        two_br_profits = two_br_revenue - two_br_set.size() * 750;
                        System.out.println("Anticipated total revenue from the current active 2 bedroom leases: $" + two_br_revenue);
                        System.out.println("Anticipated profits: $" + two_br_profits);
                        System.out.println();

                        try{
                            PreparedStatement get_3BR = conn.prepareStatement("select lease_id from apartment where property_id = ? and bedroom = ?");
                            get_3BR.setInt(1, i);
                            get_3BR.setInt(2, 3);
                            ResultSet result4 = get_3BR.executeQuery();
                            while(result4.next()){
                                lease_id = result4.getInt("lease_id");
                                three_br_set.add(lease_id);
                            }
                            System.out.println("Total 3 bedroom apartments occupied: " + three_br_set.size());
                            System.out.println("Total 3 bedroom apartments available: " + (9 - three_br_set.size()));
                            three_BR = 0;
                            result4.close();
                            get_3BR.close();
                        } catch(SQLException se){
                            se.printStackTrace();
                        }

                        
                        for(int l: three_br_set){
                            try{
                                PreparedStatement get_revenue = conn.prepareStatement("select total_due from payment where lease_id = ?");
                                get_revenue.setInt(1, l);
                                ResultSet result_revenue = get_revenue.executeQuery();
                                while(result_revenue.next()){
                                    double current_revenue = result_revenue.getDouble("total_due");
                                    three_br_revenue = three_br_revenue + current_revenue;
                                }
                                get_revenue.close();
                                result_revenue.close();
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        }
                        three_br_profits = three_br_revenue - three_br_set.size() * 800;
                        System.out.println("Anticipated total revenue from the current active 3 bedroom leases: $" + three_br_revenue);
                        System.out.println("Anticipated profits: $" + three_br_profits);
                        System.out.println();

                        try{
                            PreparedStatement get_4BR = conn.prepareStatement("select lease_id from apartment where property_id = ? and bedroom = ?");
                            get_4BR.setInt(1, i);
                            get_4BR.setInt(2, 4);
                            ResultSet result5 = get_4BR.executeQuery();
                            while(result5.next()){
                                lease_id = result5.getInt("lease_id");
                                four_br_set.add(lease_id);
                            }
                            System.out.println("Total 4 bedroom apartments occupied: " + four_br_set.size());
                            System.out.println("Total 4 bedroom apartments available: " + (9 - four_br_set.size()));
                            result5.close();
                            get_4BR.close();
                        } catch(SQLException se){
                            se.printStackTrace();
                        }
                        
                        for(int l: four_br_set){
                            try{
                                PreparedStatement get_revenue = conn.prepareStatement("select total_due from payment where lease_id = ?");
                                get_revenue.setInt(1, l);
                                ResultSet result_revenue = get_revenue.executeQuery();
                                while(result_revenue.next()){
                                    double current_revenue = result_revenue.getDouble("total_due");
                                    four_br_revenue = four_br_revenue + current_revenue;
                                }
                                get_revenue.close();
                                result_revenue.close();
                            } catch(SQLException se){
                                se.printStackTrace();
                            }
                        }
                        System.out.println("Anticipated total revenue from the current active 4 bedroom leases: $" + four_br_revenue);
                        four_br_profits = four_br_revenue - four_br_set.size() * 850;
                        System.out.println("Anticipated profits: $" + four_br_profits);
                        System.out.println();
                    }
                    result1.close();
                    get_total_occupancy.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }

                System.out.println("Total revenue anticipated from this property: $" + (one_br_revenue + two_br_revenue + three_br_revenue + four_br_revenue));
                System.out.println("Total profits anticipated from this property: $" + (one_br_profits + two_br_profits + three_br_profits + four_br_profits));
                financial_analysis.put(one_br_revenue + two_br_revenue + three_br_revenue + four_br_revenue, property_names[i-1]);
                property_profits[i-1] = one_br_revenue + two_br_revenue + three_br_revenue + four_br_revenue;
                System.out.println();
                System.out.println();
            }

            Arrays.sort(property_profits);
            System.out.println("The most profitable property is " + financial_analysis.get(property_profits[4]) + " with profits of $" + property_profits[4]);
            System.out.println("The least profitable property is " + financial_analysis.get(property_profits[0]) + " with profits of $" + property_profits[0]);
        } catch(Exception e){
            System.out.println("Server error. Please try again.");
            return;
        }
    }
}
