package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		super(p, r, c, a);
	}
	
	/**
	 * A badger occupies the square. 	 
	 */
	public State who()
	{
		return State.BADGER; 
	}
	
	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a badger. 
		int population[] = new int [Living.NUM_LIFE_FORMS];
		census(population);
		
		if(myAge() == 4)								// If age is 4, next cycle dies and State = EMPTY
		{
			return new Empty(pNew,row,column);
		}
		else if(population[Living.BADGER] == 1 && population[Living.FOX] > 1)   //If Foxes outnumber lone badger
		{
			return new Fox(pNew,row,column, 0);
		}
		else if(population[Living.BADGER] + population[Living.FOX] > population[Living.RABBIT]) // If Badgers + fox outnumber Rabbits
		{
			return new Empty(pNew,row,column);
		}
		else //Badger gets a year older
		{
			return new Badger(pNew,row,column,myAge() + 1);
		}
	}
}
