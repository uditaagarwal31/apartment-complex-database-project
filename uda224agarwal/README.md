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

* Each lease is associated with / signed by 1 human tenant. That tenant can have several dependants living with them in the same apartment. 

* A pet cannot own / sign the lease. 

* A tenant can have as many pets as they want. Irrespective of how many pets they own, they need to pay a one time pet fee of $300.



## Financial Manager
### Interface Overview
This interface creates a read only financial report for all 5 properties associated with Eastside Uncommons. 
At each individual property level, it tells you
- total number of active leaes
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

Now, you will see the finanical report generated. As you can see, the least profitable property right now is Joyful Apartments. Keep this in mind! 



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
        * credit score - required to be above 720 to sign a lease 
2. Record lease data
3. Set move-out date
4. Add dependant to a lease
5. Add amenity to a property
