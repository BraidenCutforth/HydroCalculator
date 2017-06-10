import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;


public class HydroCalculator{

	public static int BILLING_CYCLE = 60; //Billing cycle in days (approximate)
	public static double STAGE_TWO_DAY = 22.1918; //Approximate KWH a day where you can hit stage two prices
	public static double STAGE_ONE_PRICE = 0.0829; //Stage one pricing per KWH
	public static double STAGE_TWO_PRICE = 0.1243; //Stage two pricing per KWH
	public static double DAILY_CHARGE = 0.1835; //Daily surcharge
	public static double RIDER_FEE = 0.05; //Measured in percentage
	public static double TAX = 0.05; //Tax in percentage
	public static String FILE_NAME = "Bill.txt";

	public static class Data{ //Data object to read information from the file
		public int days;
		public double KWH;

		public Data(int a, double b){
			days = a;
			KWH = b;
		}


	}

	public static Data readFile() throws FileNotFoundException{
		System.out.println("Opening file.\n");
		FileInputStream file = new FileInputStream(FILE_NAME);

		Scanner input = new Scanner(file); //Creating the scanner to read the file

		Data info = new Data(0,0);

		while(input.hasNextDouble()){ //Loop for getting total KWH
			
			info.KWH = info.KWH + input.nextDouble(); //Continued to add the next double value
			info.days++; //Increment the day as you read more numbers

		}

		return info;
	}

	public static boolean meetingStageTwo(Data info){ //Returns true if we are meeting stage 2 prices, false otherwise

		double stage2 = (double)info.days*STAGE_TWO_DAY; //How much power you should use to stay below stage two prices

		if(info.KWH <= stage2){ //Finds out if we are meeting stage two prices or not
			return true;
		}else if (info.KWH > stage2){
			return false;
		}else{ //Case if it can't be determined, still returns false
			System.out.println("It cannot be determined if you are meeting the consumption goal for this period.");
			return false;
		}
	}

	public static double estimateTotalCost(Data info){ //Estimates the total cost at the end of the month
		double cost = 0;
		if(info.days > BILLING_CYCLE){ //If there are more days in the file than in the billing cycle, no need to predict
			return currentTotalCost(info);
		}
		double stage2 = (double)info.days * STAGE_TWO_DAY;
		double estKWH = (info.KWH/(double)info.days) * (double)BILLING_CYCLE;

		if(estKWH < stage2){
			cost = estKWH * STAGE_ONE_PRICE;
		}else{
			cost = stage2 * STAGE_ONE_PRICE;
			cost = cost + ((estKWH - stage2) * STAGE_TWO_PRICE);
		}

		cost = cost + (info.days * DAILY_CHARGE);
		cost = cost + (cost * RIDER_FEE);
		cost = cost + (cost * TAX);

		return cost;
	}

	public static double currentTotalCost(Data info){ //Calculates the current cost of hydro based on your usage to date
		double cost = 0;
		double stage2 = (double)info.days * STAGE_TWO_DAY;

		if(info.KWH < stage2){
			cost = info.KWH * STAGE_ONE_PRICE;
		}else{
			cost = stage2 * STAGE_ONE_PRICE;
			cost = cost + ((info.KWH - stage2) * STAGE_TWO_PRICE);
		}

		cost = cost + (info.days * DAILY_CHARGE);
		cost = cost + (cost * RIDER_FEE);
		cost = cost + (cost * TAX);

		return cost;
	}

	public static void main(String[] args){
		Data info;
		try{
			info = readFile();
		}catch(FileNotFoundException e){
			System.out.println("Couldn't read file. Error:" + e);
			return;
		}

		System.out.println("You are " + info.days + " into your billing cycle.");
		System.out.printf("You have used %.2f KWH to date.\n", info.KWH);
		System.out.printf("The current cost of your hydro to date is: $%.2f\n", currentTotalCost(info));
		if(meetingStageTwo(info)){
			System.out.println("You are meeting stage two goals.");
		}else{
			System.out.println("You are not meeting stage two goals.");
		}
		System.out.printf("The projected cost for your next bill is: $%.2f\n", estimateTotalCost(info));


	}

}


