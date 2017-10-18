import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;


import javax.swing.JOptionPane;

public class GymMedlemmar 
{

	public static void main(String[] args) 
	{
		
		
		String line1;
		String line2;
		String [] namnOchPersonnr;
		boolean found = false;     
		String namn ="";
		String personNr="";
		
		String input = JOptionPane.showInputDialog("Skriv in namn eller personnr på personen i fråga");
		File file = new File("BetaldaKunder.txt");  //skapa ny fil för betalda medlemmar
		
		try 
		{
			
			BufferedReader buffin = new BufferedReader(new FileReader("customers.txt")); //läsa in lista av medlemmar
			

			while((line1 = buffin.readLine()) != null) //läsa in raden med pesonNr & namn
			{
				line2 = buffin.readLine();			//läsa in raden med datum för senast betalt
				
				if(line1.contains(",")){
					namnOchPersonnr = line1.split(",");  //skapa separata strängar för namn respektive personnr utifrån line1
					namn = namnOchPersonnr[1].trim();
					personNr = namnOchPersonnr[0].trim();
				}
				
				if (namn.equalsIgnoreCase(input) || personNr.equalsIgnoreCase(input)) //om namn el. personnr överstämmer med input
				{
					found = true; 		//sant om input överstämmer med namn eller personnr från customers.txt
					
					LocalDate senastBetalt = LocalDate.parse(line2); //skapa localdate objekt av line2 dvs senast betalt datum
					LocalDate dagensDatum = LocalDate.now();
					LocalDate ettÅrTillbakaFrånIdag = dagensDatum.minusYears(1);
					if(senastBetalt.isBefore(ettÅrTillbakaFrånIdag)) 			//om senast betalt datum är >1år, hamnar vi här
					{
						JOptionPane.showMessageDialog(null, namn + " är befintlig icke-betald kund! \n Betalt senast: "+line2);
						break;
					}
					else if(senastBetalt.isAfter(ettÅrTillbakaFrånIdag))		//om senast betalt datum är <1år, hamnar vi här
					{
						
						JOptionPane.showMessageDialog(null, namn + " är befintlig betald kund! \n Betalt senast: "+line2);
						BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
						output.write(line1); 				//skriver namn och personnr till filen skapad ovan (betaldakunder.txt) 
						output.newLine();
						output.write("Var här senast: "+ dagensDatum.toString());	//samt dagens datum, när de senast va här
						output.newLine();
						output.flush();
						break;
					}
		
				}
			}
		}

			
		catch (Exception e) 
		{
			System.out.println("Något gick snett!");
			e.printStackTrace();
			
		}
			
		if(!found) 		//om input inte finns i customers.txt, dvs aldrig vart medlem
		{
			JOptionPane.showMessageDialog(null, "Den sökta personen, "+input+", har aldrig varit medlem och är obehörig!");
			
		}
		
	}

}
