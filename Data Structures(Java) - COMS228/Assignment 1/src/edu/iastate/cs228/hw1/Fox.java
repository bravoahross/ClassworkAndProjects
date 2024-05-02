package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

/**
 * A fox eats rabbits and competes against a badger. 
 */
public class Fox extends Animal 
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Fox (Plain p, int r, int c, int a) 
	{
		super(p, r, c, a);
	}
		
	/**
	 * A fox occupies the square. 	 
	 */
	public State who()
	{
		// TODO 
		return State.FOX; 
	}
	
	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior badgers. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a fox. 
		int population[] = new int [Living.NUM_LIFE_FORMS];
		census(population);
		
		if(myAge() == 6) //Empty if the Fox is currently at age 6;
		{
			return new Empty(pNew,row,column);
		}
		else if(population[Living.BADGER] > population[Living.FOX]) //otherwise, Badger, if there are more Badgers than Foxes in the neighborhood;
		{
			return new Badger(pNew,row,column, 0);
		}
		// otherwise, Empty, if Badgers and Foxes together outnumber Rabbits in the neighborhood;
		else if(population[Living.BADGER] + population[Living.FOX] > population[Living.RABBIT])
		{
			return new Empty(pNew,row,column);
		}
		else // otherwise, Fox (the fox will live on).
		{
			return new Fox(pNew,row,column,myAge() + 1);
		}
	}
}
