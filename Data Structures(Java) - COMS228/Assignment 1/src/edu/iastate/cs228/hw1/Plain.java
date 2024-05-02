package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner; 
import java.util.Random; 

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain 
{
	private int width; // grid size: width X width 
	
	public Living[][] grid; 
	
	/**
	 *  Default constructor reads from a file 
	 */
	public Plain(String inputFileName) throws FileNotFoundException
	{		
        // TODO 
		// 
		// Assumption: The input file is in correct format. 
		// 
		// You may create the grid plain in the following steps: 
		// 
		// 1) Reads the first line to determine the width of the grid.
		// 
		// 2) Creates a grid object. 
		// 
		// 3) Fills in the grid according to the input file. 
		// 
		// Be sure to close the input file when you are done. 
		width = -1;
		File text = new File(inputFileName);  //Read in File
		Scanner sc = new Scanner(text);
		int row = 0;
		while (sc.hasNextLine()) // While there is unread data in the file 
		{
			int col = 0;
			String line = sc.nextLine(); //Get the next line of data
			if (width == -1) 
			{
			int size = line.length();		
			width = size / 3;					//Set the width based off of the first line length
			grid = new Living[width][width];
			}
			Scanner scan = new Scanner(line);
			while (scan.hasNext()) 
			{
				String c = scan.next();	//Gets first letter of State, and if animal assigns age too
				int age;
				switch(c.charAt(0)) {
				case 'E':
					grid[row][col] = new Empty(this,row,col);
					break;
				case 'B':
					age = Character.getNumericValue(c.charAt(1));
					grid[row][col] = new Badger(this,row,col,age);
					break;
				case 'F':
					age = Character.getNumericValue(c.charAt(1));
					grid[row][col] = new Fox(this,row,col,age);
					break;
				case 'G':
					grid[row][col] = new Grass(this,row,col);
					break;
				case 'R':
					age = Character.getNumericValue(c.charAt(1));
					grid[row][col] = new Rabbit(this,row,col,age);
					break;
				}
				col++;
			}
			row++;
			scan.close();
		}
		sc.close();
	}
	
	/**
	 * Constructor that builds a w x w grid without initializing it. 
	 * @param width  the grid 
	 */
	public Plain(int w)
	{
		// TODO 
		grid = new Living[w][w];
		width = w;
	}
	
	
	public int getWidth()
	{
		return width;  
	}
	
	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, FOX, RABBIT, GRASS, or EMPTY.  
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit()
	{
		Random generator = new Random(); 
		// TODO 
		for(int r = 0; r < getWidth(); r++)
		{
			for(int c = 0; c < getWidth(); c++)
			{
				int temp = generator.nextInt(Living.NUM_LIFE_FORMS); //Random number 0-4. Correlates to a State that will be assigned
				switch(temp) {
				case Living.BADGER:
					grid[r][c] = new Badger(this,r,c,0);
					break;
				case Living.EMPTY:
					grid[r][c] = new Empty(this,r,c);
					break;
				case Living.FOX:
					grid[r][c] = new Fox(this,r,c,0);
					break;
				case Living.GRASS:
					grid[r][c] = new Grass(this,r,c);
					break;
				case Living.RABBIT:
					grid[r][c] = new Rabbit(this,r,c,0);
					break;
				}
			}
		}
	}
	
	
	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.  
	 */
	public String toString()
	{
		String current = "";
		for(Living[] row: grid) 
		{
			for(Living cell : row) 
			{
				current += cell.who().name().charAt(0);	//Adds first letter of State
				if (cell instanceof Animal) 
				{
					current += ((Animal) cell).myAge(); //Adds age if animal
				}
				else {
					current += " "; // Adds extra space if not animal
				}
				current += " "; // Adds space 
			}
			current += "\n"; //Adds new line at the end of a row
		}
		return current; 
	}

	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose. 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws IOException
	{
		// TODO 
		// 
		// 1. Open the file. 
		// 
		// 2. Write to the file. The five life forms are represented by characters 
		//    B, E, F, G, R. Leave one blank space in between. Examples are given in
		//    the project description. 
		// 
		// 3. Close the file. 
		
		FileOutputStream out = new FileOutputStream (outputFileName); // Open File
		out.write(toString().getBytes());
		out.close();												// Close File
	}			
}
