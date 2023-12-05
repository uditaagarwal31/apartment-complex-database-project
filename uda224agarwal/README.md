# 241-final-project - Udita Agarwal

## Project Assumptions
* 5 properties and each property has nine 1 bedroom, 2 bedroom, 3 bedroom, and 4 bedroom apartments. Leases are 12 months long and rent is due on the 1st of every month. Security deposit and one time $300 pet fee due at time of signing lease. Late payment fee of $50 will be added if tenant pays late. 
    - 1 bedroom & 1 bathroom apartment with 1200 sq feet, base rent $1200 per month and security deposit $2400
    - 2 bedroom & 1 bathroom apartment with 1400 sq feet, base rent $1800 per month and security deposit $3600
    - 3 bedroom & 2 bathroom apartment with 1600 sq feet, base rent $2400 per month and security deposit $4800 
    - 4 bedroom & 2 bathroom apartment with 1800 sq feet, base rent $2800 per month and security deposit $5600

* Each property has 7 public amenities and 3 private amenities. Additional amenities can be added to each property.
    Public:
    1. Swimming Pool
    2. Gym
    3. Vending Machine
    4. Sauna
    5. Cafe
    6. Parking
    7. Office Space

    Private:
    1. Pet Walker 
    2. Cleaning
    3. Babysitting

* The monthly cost and accessibility is the same for all amenities across all properties at Eastside Uncommons.

* Each lease is associated with / signed by 1 human tenant. That tenant can have several dependants living with them in the same apartment. 

* A pet cannot own / sign the lease. 

* A tenant can have as many pets as they want. Irrespective of how many pets they own, they need to pay a one time pet fee of $300.

-------------------------------------------------------------------------------------------------------------------------------

## Financial Manager
### Interface Overview
This interface creates a read only financial report for all 5 properties associated with Eastside Uncommons. 
At each individual property level, it tells you
- total number of active leases
- total 1, 2, 3, and 4 bedroom apartments occupied
- total 1, 2, 3, and 4 bedroom apartments available 
- anticipated total revenue from the current 1, 2, 3, and 4 bedroom apartments occupied
- anticipated profits from the current 1, 2, 3, and 4 bedroom apartments occupied
- anticipated total revenue from that property
- anticipated total profits from that property

From a more zoomed out perspective of Eastside Uncommons as a whole, it tells you
- most profitable property with its anticipated profits 
- least profitable property with its anticipated profits 

### How to test 
* From Main Menu, choose Financial Manager role by entering 3
* From Financial Manager Menu, generate financial report by entering 1

Now, you will see the finanical report generated. As you can see, the least profitable property right now is Sunset Terrance. Keep the number of 4 bedroom apartments occupied and profits/revenue in mind! 

-------------------------------------------------------------------------------------------------------------------------------

## Property Manager
### Interface Overview
This interface allows you to accomplish the following tasks
1. Record visitor data
    - Records important information to validate if the prospective tenant meets the criteria to live here.

    - Error checking I'd like to point out
        * data type - string/int
        * null - forces user to enter something for certain required fields 
        * validity of zipcode, phone number, and email - length and regex 
        * age - required to be 18 to be the tenant (person who signs the lease)
        * credit score - required to be above 720 to sign a lease (else you can't live here)
        * validity of dates entered in the form MM-DD-YYYY 

    - Inserts data into ProspectiveTenant table


2. Record lease data
    - Allows user to choose the property they want to live in and the type of apartment (1/2/3/4 bedroom) and generates an apartment number for them if there's an apartment of that type available in that property. 5 properties and each property has 9 units available of each type. So there is a possibility that there may not be an unit available at that time.
        * All apartment numbers are 3 char long and generated in the form {property_id}{apartment_type}{available_unit_number}
        Eg: 213 means property_id = 2 (Oasis Lofts), apartment_type = 1 (1 bedroom apartment), available_unit_number = 3

    - After lease is signed and data is inserted into Apartment table, Lease table and Tenant table, invoices for security deposit, pet fee (if any), and the next 12 months of rent are generated in the Payments table.


3. Set move-out date
    - Allows user to enter a move-out date. If user already has a move-out date, allows them to update if desired.
        * Validates that the move-out date entered is between the date lease was signed and date it expires.

    - Updates data in Lease table. 


4. Add dependant to a lease
    - Allows user to add a dependant by entering the first name and last name. A tenant can have multiple dependants.

    - Inserts data into the Dependant table. 


5. Add amenity to a property
    - Allows user to add a private or public amenity to a property if that property doesn't currently have the amenity.
        * If the amenity you want to add exists in Eastside Uncommons in general (so exists in another property) but not that particular property then that existing amenity would be directly added to that property from the Amenity table. Data inserted in the Property Amenities table.
        * However, if no property in Eastside Uncommons contains that property, then the user is prompted to enter more information about that amenity such as accessibility (if public) and monthly cost (if private) and that Amenity would be inserted in the Amenity table, Public/Private Amenity table, and Property Amenities table.
        
        NOTE: I assume here that the monthly cost and accessibility is the same for all amenities across all properties at Eastside Uncommons.

### How to test 
* From Main Menu, choose Property Manager role by entering 1
* From Property Manager menu
1. Record visitor data 
- Enter 1 to record a new visitor. 
- Enter the information in all the fields.
- Please note that you are required to be above 18 and have a credit score of above 720 to be eligible to live here.
- Note down your tenant id.

2. Record lease data 
- Enter 2 to record lease data .
- Enter the tenant id from above.
- Enter the corresponding number to the apartment style you'd like. Let's enter 4.
- After entering lease sign date, choose the property you want to live in. Let's enter 4 for Sunset Terrace.
- Note down your apartment number, lease id, and lease sign date - you will need this soon!

3. Set move-out date
- Enter 3 to set a move-out date.
- Enter the apartment number from above.
- Enter the move-out date within lease signing and lease expiry window.
- If you want, you can update the move-out date that you just set a minute ago by performing the same actions again.

4. Add dependant to a lease
- Enter 4 to add a dependant
- Enter your tenant id
- Enter dependant's first and last names
- If you want, you can add more dependants by performing the same actions again.

5. Add amenity to a property
- Enter 5 to add an amenity
- Enter the corresponding number to your apartment. In this case, it would be 4 for Sunset Terrace.
- You will now see a list of amenities that the property already has. Enter the name of a new amenity you'd like to add.
- As mentioned in the Interface Overview section, if the amenity you want to add exists in another property in Eastside Uncommons but not in Sunset Terrace, then you don't need to provide any additional information. However, if that isn't the case, then you will have to provide more information such as:
    - Select if its a private or public amenity. 
    - If public, enter its accesibility.
    - If private, enter its monthly cost.
- If you want, you can add more amenities by performing the same actions again.

-------------------------------------------------------------------------------------------------------------------------------

## Tenant
### Interface Overview
This interface allows you to accomplish the following tasks
1. Check payment status and make rental payment 
- Allows user to check if they have any payments due based on today's date that they entered. If they have a payment due past the due date, a late fee of $50 would be added to the invoice. 

NOTE: rent is due on the 1st of every month.

- If there are payments due, allows the tenant to make the payment by cash or card. Validates that the invoice number you enter is actually one you have a payment due for. 
    * If paying by card, prompts user to enter card number, name, and expiry.
    * If paying by cash, enter the corresponding number of bills.

- Inserts data in Payment Method table, Cash/Card table, and updates date_paid in Payment table.


2. Update personal data
- Allows user to update personal data by printing their current data in the database and allowing them to choose which field they'd like to update.
    * If they updated their pet status from no to yes, then a one time pet fee of $300 would be due that day and added in the Payment table.

- Updates data in ProspectiveTenant table.


3. Add amenity to your lease
- Allows user to add a private amenity to their lease. Prints the existing private amenities in that property and the amenities the user currently has added to their lease. 
    * If the user has all the amenities that property has to offer added to their lease, that is printed.
    * If the user has certain property amenities they'd like to add to their lease, enables the user to do that by entering that amenity name.

- Inserts data in Lease Amenities table. 

### How to test 
* From Main Menu, choose Tenant role by entering 2
* From Tenant menu
1. Check payment status and make rental payment 
- Enter 1 to check payment status and make rental payment.
- Enter the tenant id.
- Enter the date you signed the lease (remember I asked you to note it down above!).
- If you enter the date you signed the lease, you will see you have invoices due for security deposit, first month of rent, and pet fees (if you have a pet). If you entered a regular date, you would either see an invoice due for your monthly rent payment or no payments due if you are upto date with all your payments.
- Enter 2 to make the payment now. If you have more than 1 invoice due, enter the corresponding number. 
- Enter 1 to pay by card and enter card details.
- Now, if you had more than 1 invoice due, repeat the steps above to make your payment. Enter the same date as above. Now, when you see the invoices whose payments are currently due, you will see that the invoice you completed the payment for is not printed. 
- This time, lets pay by cash. Enter 2 to pay by cash and enter corresponding number of bills used.
- You can repeat the same steps above if you'd like to make more payments.

2. Update personal data
- Enter 2 to update your personal data.
- Enter your tenant_id.
- Enter 1 if you'd like to update your information or 2 to return to the menu.
- Select what field you'd like to update. Enter the updated information.
- Now, I'd like to show you a feature I implemented. If you update your pet status from no to yes, you will have a one time pet fee of $300 to pay and an invoice for that generated in the Payment table. 
    * Lets test this by entering the tenant id 101. Now, you will see that the pet status is no. If you update it to yes by entering 12 and typing yes, you will see an invoice of $300 generated for you to pay that day.

3. Add amenity to your lease
- Enter 3 to add a private amenity to your lease.
- You will see a list of amenities available at that property. You will also see that you don't have any private amenities currently added to your lease. 
- Enter the name of the amenity you'd like to add to your lease from the given amenity list. You can add multiple if you'd like.
- Now, if you enter 1 to return to the main menu and then 3 again and enter your lease id, you will see the amenities you just added to your lease reflected. You can add more amenities if you'd like or return to the menu by entering 1.


-------------------------------------------------------------------------------------------------------------------------------
### Next Steps
- You have officially tested all the interfaces yay!
- Now, if you go back to the financial manager interface by selecting 3 in the main menu and 1 to generate a financial report, remember how I said to keep the availability of 4 bedroom apartments and revenue and profits in mind? You can now see the Sunset Terrance property has 1 more 4 bedroom apartment occupied and an incremental increase in its revenue and profits. (I know the increase in revenue/profits isn't anything too crazy but remember you made very few payments just now haha so if you want it to increase more you gotta pay more of your rent installments that will be due soon!)