package edu.iastate.cs228.hw1;
/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BadgerTest {
	
	/**
	 * Tests for if who method returns with the proper state
	 */
	@Test
	public void whoTest()
	{
		Plain p = new Plain(3);
		Badger b = new Badger(p, 1, 1, 0);
		assertTrue(State.BADGER == b.who());
	}
	
	/**
	 * Tests if an animal dies from age
	 */
	@Test
	public void AgeCond()
	{
		Plain p = new Plain(3);
		Plain next = new Plain(3);
		Badger b = new Badger(p, 1, 1, 4);
		for(int r = 0; r < 3; r++)
		{
			for(int c =0; c < 3; c++)
			{
				p.grid[r][c] = b;
			}
		}
		Living subject = b.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * Changes the State to FOX if more foxes than badgers
	 */
	
	@Test
	public void FoxCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Fox f1 = new Fox(p, 1, 0, 0);
		Fox f2 = new Fox(p, 1, 1, 0);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = f1;
		p.grid[1][1] = f2;
		
		Living subject = b.next(next);
		assertTrue(State.FOX == subject.who());
	}
	
	/**
	 * Changes State to EMPTY if more foxes + badgers than rabbits
	 */
	@Test
	public void EmptyCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Empty e = new Empty(p, 1, 0);
		Fox f2 = new Fox(p, 1, 1, 0);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = e;
		p.grid[1][1] = f2;
		
		Living subject = b.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * Keeps State the same, and Animal gets older
	 */
	@Test
	public void BadgerLivesOn()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Empty e1 = new Empty(p, 1, 0);
		Empty e2 = new Empty(p, 1, 1);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = e1;
		p.grid[1][1] = e2;
		
		Living subject = b.next(next);
		assertTrue(State.BADGER == subject.who());
		assertEquals(1, ((Badger) subject).myAge());
	}
	
}
