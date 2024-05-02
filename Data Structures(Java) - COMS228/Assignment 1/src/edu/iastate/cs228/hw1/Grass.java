package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is eaten. 
 *
 */
public class Grass extends Living 
{
	public Grass (Plain p, int r, int c) 
	{
		super(p, r, c);
	}
	
	public State who()
	{
		// TODO  
		return State.GRASS; 
	}
	
	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast enough to take over Grass.
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for grass.
		int population[] = new int [Living.NUM_LIFE_FORMS];
		census(population);
		
		if(population[Living.GRASS] * 3 <= population[Living.RABBIT]) //Empty if at least three times as many Rabbits as Grasses in the neighborhood;
		{
			return new Empty(pNew,row,column);
		}
		else if(population[Living.RABBIT] >= 3) //otherwise, Rabbit if there are at least three Rabbits in the neighborhood;
		{
			return new Rabbit(pNew,row,column,0);
		}
		else // otherwise, Grass.
		{
			return new Grass(pNew,row,column);
		}
	}
}
