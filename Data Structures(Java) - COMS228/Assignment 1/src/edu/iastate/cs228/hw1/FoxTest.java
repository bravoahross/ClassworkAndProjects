package edu.iastate.cs228.hw1;
/**
 *  
 * @author ncros
 *
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FoxTest {
	
	/**
	 * Tests for if who method returns with the proper state
	 */
	@Test
	public void whoTest()
	{
		Plain p = new Plain(3);
		Fox f = new Fox(p, 1, 1, 0);
		assertTrue(State.FOX == f.who());
	}
	
	/**
	 * Tests if an animal dies from age
	 */
	@Test
	public void AgeCond()
	{
		Plain p = new Plain(3);
		Plain next = new Plain(3);
		Fox f = new Fox(p, 1, 1, 6);
		for(int r = 0; r < 3; r++)
		{
			for(int c =0; c < 3; c++)
			{
				p.grid[r][c] = f;
			}
		}
		Living subject = f.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/*
	 * If badgers > foxes then State = BADGER
	 */
	@Test
	public void BadgerCond()
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
		
		Living subject = f.next(next);
		assertTrue(State.BADGER == subject.who());
	}
	
	/*
	 * If badgers + foxes > rabbits then State = EMPTY
	 */
	@Test
	public void EmptyCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Empty e = new Empty(p, 1, 0);
		Fox f = new Fox(p, 1, 1, 0);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = e;
		p.grid[1][1] = f;
		
		Living subject = f.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * Keeps State the same, and Animal gets older
	 */
	@Test
	public void FoxLivesOn()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Fox f = new Fox(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Empty e1 = new Empty(p, 1, 0);
		Empty e2 = new Empty(p, 1, 1);
		p.grid[0][0] = f;
		p.grid[0][1] = r;
		p.grid[1][0] = e1;
		p.grid[1][1] = e2;
		
		Living subject = f.next(next);
		assertTrue(State.FOX == subject.who());
		assertEquals(1, ((Fox) subject).myAge());
	}
}
