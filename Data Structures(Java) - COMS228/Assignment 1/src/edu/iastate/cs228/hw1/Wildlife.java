package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 *  
 * @author ncros
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife 
{
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain 
	 */
	public static void updatePlain(Plain pOld, Plain pNew)
	{
		// TODO 
		// 
		// For every life form (i.e., a Living object) in the grid pOld, generate  
		// a Living object in the grid pNew at the corresponding location such that 
		// the former life form changes into the latter life form. 
		// 
		// Employ the method next() of the Living class. 
		for(Living[] row: pOld.grid) 
		{
			for(Living cell : row) 
			{
				pNew.grid[cell.row][cell.column] = cell.next(pNew);
			}
		}
	}
	
	
	/**
	 * Helper Method for instructions and input in main
	 * @param sc
	 * @param instruction
	 * @return
	 */
	public static int getUserInput(Scanner sc, String instruction)
	{
		System.out.print(instruction);
		int input = sc.nextInt();
		return input;
	}
	
	
	
	
	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{	
		// TODO 
		// 
		// Generate wildlife simulations repeatedly like shown in the 
		// sample run in the project description. 
		// 
		// 1. Enter 1 to generate a random plain, 2 to read a plain from an input
		//    file, and 3 to end the simulation. (An input file always ends with 
		//    the suffix .txt.)
		// 
		// 2. Print out standard messages as given in the project description. 
		// 
		// 3. For convenience, you may define two plains even and odd as below. 
		//    In an even numbered cycle (starting at zero), generate the plain 
		//    odd from the plain even; in an odd numbered cycle, generate even 
		//    from odd. 
		
		//Plain even;   				 // the plain after an even number of cycles 
		//Plain odd;                   // the plain after an odd number of cycles
		
		// 4. Print out initial and final plains only.  No intermediate plains should
		//    appear in the standard output.  (When debugging your program, you can 
		//    print intermediate plains.)
		// 
		// 5. You may save some randomly generated plains as your own test cases. 
		// 
		// 6. It is not necessary to handle file input & output exceptions for this 
		//    project. Assume data in an input file to be correctly formated. 
		System.out.println("Simulation of the Plain");
		System.out.println("keys: 1 (random plain)  2 (file input)  3 (exit)");
		System.out.println();
		Scanner sc = new Scanner(System.in);
		Plain next;
		int num = 1;
		while(true)
		{
			int input = getUserInput(sc, "Trial " + num + ": ");
			Plain p;
			switch(input)
			{
			case 1:  // Random generate
				System.out.println("Random Plain");
				int width = getUserInput(sc, "Enter grid width:");
				p = new Plain(width);
				p.randomInit();  // initialize plain with randomInit from Plain
				break;
			case 2:  // Generate from file input
				System.out.println("Plain input from a file");
				System.out.print("File name: ");
				String filename = sc.next();
				p = new Plain(filename); // initialize plain with a txt file from constructor in Plain
				break;
			case 3:  // exit 
				return;
			default:
				System.out.println("Not a valid input");  // Catch for numbers not 1, 2, or 3
				return;
			}
			num++;
			int cycles = getUserInput(sc, "Enter the number of cycles:");  //Gets number of iterations
			next = new Plain(p.getWidth());
			System.out.println();
			System.out.println("Initial Plain:");   		// prints starting plain
			System.out.println();
			System.out.println(p.toString());
		
			for(int i = 0; i < cycles; i++) // loops for number of iterations 
			{
				next = new Plain(p.getWidth());  // For deep copy/clone
				updatePlain(p,next);
				p = next;
			}
		System.out.println("Final Plain: ");				//Prints final plain 
		System.out.println();
		System.out.println(p.toString());
		}
	}
}