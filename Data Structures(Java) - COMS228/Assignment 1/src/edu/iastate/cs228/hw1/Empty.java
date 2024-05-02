package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

/** 
 * Empty squares are competed by various forms of life.
 */
public class Empty extends Living 
{
	public Empty (Plain p, int r, int c) 
	{
		super(p, r, c); 
	}
	
	public State who()
	{
		// TODO 
		return State.EMPTY; 
	}
	
	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or Grass, or remain empty. 
	 * @param pNew     plain of the next life cycle.
	 * @return Living  life form in the next cycle.   
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for corresponding survival rules. 
		int population[] = new int [Living.NUM_LIFE_FORMS];
		census(population);
		
		if(population[Living.RABBIT] > 1) // Rabbit, if more than one neighboring Rabbit;
		{
			return new Rabbit(pNew,row,column,0);
		}
		else if(population[Living.FOX] > 1) //otherwise, Fox, if more than one neighboring Fox;
		{
			return new Fox(pNew,row,column,0); 
		}
		else if(population[Living.BADGER] > 1) //otherwise, Badger, if more than one neighboring Badger;
		{
			return new Badger(pNew,row,column,0); 
		}
		else if(population[Living.GRASS] > 0) //otherwise, Grass, if at least one neighboring Grass;
		{
			return new Grass(pNew,row,column); 
		}
		else //otherwise, Empty.
		{
			return new Empty(pNew,row,column);
		}
	}
}
