package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RabbitTest {
	
	/**
	 * Tests for if who method returns with the proper state
	 */
	@Test
	public void whoTest()
	{
		Plain p = new Plain(3);
		Rabbit r = new Rabbit(p, 1, 1, 0);
		assertTrue(State.RABBIT == r.who());
	}
	
	/**
	 * Tests if an animal dies from age
	 */
	@Test
	public void AgeCond()
	{
		Plain p = new Plain(3);
		Plain next = new Plain(3);
		Rabbit r = new Rabbit(p, 1, 1, 3);
		for(int row = 0; row < 3; row++)
		{
			for(int c =0; c < 3; c++)
			{
				p.grid[row][c] = r;
			}
		}
		Living subject = r.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * If there is no Grass, rabbit starves and State = EMPTY
	 */
	@Test
	public void StarveCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Fox f = new Fox(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Badger b1 = new Badger(p, 1, 0, 0);
		Badger b2 = new Badger(p, 1, 1, 0);
		p.grid[0][0] = f;
		p.grid[0][1] = r;
		p.grid[1][0] = b1;
		p.grid[1][1] = b2;
		
		Living subject = r.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * If foxes + badger > rabbits and foxes > badgers then State = FOX
	 */
	@Test
	public void FoxCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Fox f1= new Fox(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Fox f2 = new Fox(p, 1, 0, 0);
		Grass g = new Grass(p, 1, 1);
		p.grid[0][0] = f1;
		p.grid[0][1] = r;
		p.grid[1][0] = f2;
		p.grid[1][1] = g;
		
		Living subject = r.next(next);
		assertTrue(State.FOX == subject.who());
	}
	
	/**
	 * If badgers > Rabbits then State = BADGER
	 */
	@Test
	public void BadgerCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Badger b1= new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Badger b2 = new Badger(p, 1, 0, 0);
		Grass g = new Grass(p, 1, 1);
		p.grid[0][0] = b1;
		p.grid[0][1] = r;
		p.grid[1][0] = b2;
		p.grid[1][1] = g;
		
		Living subject = r.next(next);
		assertTrue(State.BADGER == subject.who());
	}
	
	/**
	 * Keeps State the same, and Animal gets older
	 */
	@Test
	public void RabbitLivesOn()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Grass g = new Grass(p, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Empty e1 = new Empty(p, 1, 0);
		Empty e2 = new Empty(p, 1, 1);
		p.grid[0][0] = g;
		p.grid[0][1] = r;
		p.grid[1][0] = e1;
		p.grid[1][1] = e2;
		
		Living subject = r.next(next);
		assertTrue(State.RABBIT == subject.who());
		assertEquals(1, ((Rabbit) subject).myAge());
	}
}