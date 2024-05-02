package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal 
{	
	/**
	 * Creates a Rabbit object.
	 * @param p: plain  
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Rabbit (Plain p, int r, int c, int a) 
	{
		super(p, r, c, a); 
	}
		
	// Rabbit occupies the square.
	public State who()
	{
		// TODO  
		return State.RABBIT; 
	}
	
	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a fox.  
	 * @param pNew     plain of the next cycle 
	 * @return Living  new life form occupying the same square
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a rabbit. 
		int population[] = new int [Living.NUM_LIFE_FORMS];
		census(population);
		
		if(myAge() == 3) //Empty if the Rabbit's current age is 3;
		{
			return new Empty(pNew,row,column);
		}
		else if(population[Living.GRASS] == 0) //otherwise, Empty if there is no Grass in the neighborhood (the rabbit needs food);
		{
			return new Empty(pNew,row,column);
		}
		/*
		 * otherwise, Fox if in the neighborhood there are at least as many Foxes and Badgers
		 *combined as Rabbits, and furthermore, if there are more Foxes than Badgers;
		 */
		else if((population[Living.BADGER] + population[Living.FOX] >= population[Living.RABBIT]) && (population[Living.FOX] > population[Living.BADGER]))
		{
			return new Fox(pNew,row,column,0);
		}
		else if(population[Living.BADGER] > population[Living.RABBIT])//otherwise, Badger if there are more Badgers than Rabbits in the neighborhood;
		{
			return new Badger(pNew,row,column,0);
		}
		else // otherwise, Rabbit (the rabbit will live on).
		{
			return new Rabbit(pNew,row,column,myAge() + 1);
		}
	}
}
