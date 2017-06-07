import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;


public class hydroCalculator{

	public static int STAGE_TWO = 1350;
	public static double DAY_STAGE_TWO = 22.1918;

	public static void main(String[] args) throws FileNotFoundException{

	System.out.println("System is now opening file.\n");
	FileInputStream file = new FileInputStream("Bill.txt"); //Open file name Bill.txt
	
	Scanner input = new Scanner(file);
	int days = 0; //Day count starts at 0
	double KWH = 0; //KWH used
	double cost = 0; //Cost with current usage
	double stage2 = 0; // Variable for holding your stage 2 limit based on days in text file


	while(input.hasNextDouble()){ //Loop for getting total KWH
		
		KWH = KWH + input.nextDouble();
		days++;

	}

	stage2 = days*DAY_STAGE_TWO;

	System.out.println("There are " + days + " days in this period so far.");
	System.out.printf("The total is %.2f KWH for %d days.\n", KWH, days);
	System.out.printf("The usage should be under %.2f KWH to meet step 2 prices.\n", stage2);
	if(KWH <= stage2){
		System.out.println("You are meeting the consumption goal for this period.\n");
	}else if (KWH > stage2){
		System.out.println("You are not meeting the consumption goal for this period.\n");
	} else {
		System.out.println("It cannot be determined if you are meeting the consumption goal for this period.");
	}


	if(KWH > 0 && KWH <= stage2){
		cost = KWH*0.0829;
	}else if(KWH > stage2){
		cost = STAGE_TWO*0.0829 + (KWH-stage2)*0.1243;
	}else{
		System.out.println("Error occured, empty file or values sum to a negative");
		cost = 0;
	}


	cost = cost + days*0.1835; //Adding the daily "Basic Charge"
	cost = cost*1.05; //Adding the 5% Rider fee
	cost = cost*1.05; //Adding the 5% GST
	System.out.print("The current cost with the 5% fee and taxes is: ");
	System.out.printf("$%.2f\n\n", cost);

	int daysRemaining = 60 - days; //Days remaining in billing cycle
	double projectedCost = (cost/days)*daysRemaining + cost; //Projected cost based on monthly average to date
	System.out.print("The projected cost for 60 days is: ");
	System.out.printf("$%.2f\n\n", projectedCost);

	}

	}


